package com.zisbv.gamification.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserData user = userRepository.getUserWithEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException("User Not Found with -> username or email : " + username);
    }

    return UserPrincipal.build(user);
  }
}
