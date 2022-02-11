package com.zisbv.gamification.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.Avathars;

@Repository("avatharsRepository")
public interface AvatharsRepository extends CrudRepository<Avathars, Long> {}
