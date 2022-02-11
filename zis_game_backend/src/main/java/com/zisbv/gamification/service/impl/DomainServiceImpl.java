package com.zisbv.gamification.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.entities.Domains;
import com.zisbv.gamification.repositories.DomainsRepository;
import com.zisbv.gamification.service.DomainService;

@Service("domainService")
public class DomainServiceImpl implements DomainService {

  @Autowired
  private DomainsRepository domainsDao;

  @Override
  public List<Domains> findAll() {
    List<Domains> list = new ArrayList<>();
    domainsDao.findAll().iterator().forEachRemaining(list::add);
    return list;
  }
}
