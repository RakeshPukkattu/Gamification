package com.zisbv.gamification.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.Domains;

@Repository
public interface DomainsRepository extends CrudRepository<Domains, Long> {

}
