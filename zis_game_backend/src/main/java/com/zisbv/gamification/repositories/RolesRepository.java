package com.zisbv.gamification.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.Role;

@Repository
public interface RolesRepository extends CrudRepository<Role, Long> {
  Role findRoleByName(String name);
}
