package com.softserve.itacademy.model;

import com.google.common.collect.Sets;
import com.softserve.itacademy.security.Permissions;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softserve.itacademy.security.Permissions.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "The 'name' cannot be empty")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    public Role() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role {" +
                "id = " + id +
                ", name = '" + name + '\'' +
                "} ";
    }

    public List<SimpleGrantedAuthority> getPermissions() {
        Set<Permissions> permissions = Sets.newHashSet();
        if (name.equals("ADMIN")){
            permissions = Sets.newHashSet(USER_READ, USER_WRITE, TASK_WRITE, TASK_READ, TODO_READ, TODO_WRITE, ROLE_READ, ROLE_WRITE);
        }
        if (name.equals("USER")){
            permissions = Sets.newHashSet(USER_READ, TASK_READ, TASK_WRITE, TODO_READ);
        }
        return permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toList());
    }
}
