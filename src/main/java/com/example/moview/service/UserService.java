package com.example.moview.service;

import com.example.moview.model.RoleName;
import com.example.moview.model.User;

public interface UserService extends BaseService<User, Long> {

    void addRoleToUser(Long userId, RoleName roleName);

}