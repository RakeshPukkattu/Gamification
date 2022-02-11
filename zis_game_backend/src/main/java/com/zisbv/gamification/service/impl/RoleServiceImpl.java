package com.zisbv.gamification.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.entities.Role;
import com.zisbv.gamification.repositories.RolesRepository;
import com.zisbv.gamification.service.RoleService;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RolesRepository roleDao;

    @Override
    public Role findByName(String name) {
        Role role = roleDao.findRoleByName(name);
        return role;
    }
}