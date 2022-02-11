package com.zisbv.gamification.service;

import java.util.List;

import com.zisbv.gamification.entities.Domains;

public interface DomainService {

  //Get all domains
  public List<Domains> findAll();

}
