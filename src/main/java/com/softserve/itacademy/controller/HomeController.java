package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.security.SecurityUser;
import com.softserve.itacademy.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private boolean isUser = false;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/home"})
    @PreAuthorize("hasAuthority('user:read')")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("users", getUsers(authentication));
        model.addAttribute("isUser", isUser);
        return "home";
    }

    @GetMapping("/")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    private List<User> getUsers(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser) {
            User currentUser = userService.readById(((SecurityUser) principal).getId());
            if (currentUser.getRole().getName().equals("USER")) {
                isUser = true;
                return Arrays.asList(currentUser);
            }
        }
        isUser = false;
        return userService.getAll();
    }

}
