package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('user:write')")
    public String create(Model model) {
        model.addAttribute("user", new User());
        return "create-user";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "create-user";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.readById(2));
        User newUser = userService.create(user);
        return "redirect:/todos/all/users/" + newUser.getId();
    }

    @GetMapping("/{id}/read")
    @PreAuthorize("hasAuthority('user:read')")
    public String read(@PathVariable long id, Model model) {
        User user = userService.readById(id);
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("hasAuthority('user:write') or #id==authentication.principal.id")
    public String update(@PathVariable @Param("id") long id, Model model) {
        User user = userService.readById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAll());
        return "update-user";
    }


    @PostMapping("/{id}/update")
    @PreAuthorize("hasAuthority('user:write') or #id==authentication.principal.id")
    public String update(@PathVariable @Param("id") long id, Model model, @Validated @ModelAttribute("user") User user, @RequestParam("roleId") long roleId, BindingResult result) {
        User oldUser = userService.readById(id);
        if (result.hasErrors()) {
            user.setRole(oldUser.getRole());
            model.addAttribute("roles", roleService.getAll());
            return "update-user";
        }
        if (oldUser.getRole().getName().equals("USER")) {
            user.setRole(oldUser.getRole());
        } else {
            user.setRole(roleService.readById(roleId));
        }
        userService.update(user);
        return "redirect:/users/" + id + "/read";
    }


    @GetMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('user:write')")
    public String delete(@PathVariable("id") @Param("id") long id) {
        userService.delete(id);
        return "redirect:/users/all";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('user:write')")
    public String getAll(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users-list";
    }

    @GetMapping("/registration")
    public String newUserRegistration(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String saveNewUser(@Validated @ModelAttribute("user") User user, BindingResult result){
            if (result.hasErrors()) {
                return "registration";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(roleService.readById(2));
            User newUser = userService.create(user);
            return "redirect:/?success";
        }
}
