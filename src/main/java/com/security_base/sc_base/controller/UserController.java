package com.security_base.sc_base.controller;

import com.security_base.sc_base.models.Role;
import com.security_base.sc_base.models.UserSec;
import com.security_base.sc_base.service.impl.IRoleService;
import com.security_base.sc_base.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("permitAll()")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<List<UserSec>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSec> getUserById(@PathVariable Long id) {

        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserSec> createUser(@RequestBody UserSec userSec) {

        Set<Role> roles = new HashSet<>();
        userSec.setPassword(userService.encriptPassword(userSec.getPassword()));

        if (userSec.getRolesList() != null) {
            for (Role role : userSec.getRolesList()) {
                roleService.findById(role.getId())
                        .ifPresent(roles::add);
            }
        }

        if (roles.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        userSec.setRolesList(roles);
        return ResponseEntity.ok(userService.save(userSec));
    }
}
