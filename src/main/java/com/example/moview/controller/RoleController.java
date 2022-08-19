package com.example.moview.controller;

import com.example.moview.model.Role;
import com.example.moview.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Role>> readAll() {
        final List<Role> roleList = roleService.readAll();
        return ResponseEntity.status(HttpStatus.CREATED).body(roleList);
    }
}
