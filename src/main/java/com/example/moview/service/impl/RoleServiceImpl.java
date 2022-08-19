package com.example.moview.service.impl;

import com.example.moview.model.Role;
import com.example.moview.repository.RoleRepository;
import com.example.moview.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> readAll() {
        return roleRepository.findAll();
    }
}
