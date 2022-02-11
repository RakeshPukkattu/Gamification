package com.zisbv.gamification.service;

import com.zisbv.gamification.entities.Role;

public interface RoleService {
    Role findByName(String name);
}
