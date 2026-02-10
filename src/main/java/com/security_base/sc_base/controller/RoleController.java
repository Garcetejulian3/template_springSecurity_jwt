package com.security_base.sc_base.controller;

import com.security_base.sc_base.models.Permission;
import com.security_base.sc_base.models.Role;
import com.security_base.sc_base.service.impl.IPermissionService;
import com.security_base.sc_base.service.impl.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("permitAll()")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {

        return roleService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {

        Set<Permission> permissions = new HashSet<>();

        if (role.getPermissionsList() != null) {
            for (Permission per : role.getPermissionsList()) {
                permissionService.findById(per.getId())
                        .ifPresent(permissions::add);
            }
        }

        role.setPermissionsList(permissions);
        return ResponseEntity.ok(roleService.save(role));
    }

}