package com.zisbv.gamification.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.zisbv.gamification.entities.Avathars;

public interface AvatharsService {

  // Get all avathar images
  public List<Avathars> findAll();

  // Save new AvatharKey.
  Avathars save(String key);
  
  public String storeAvatharThumbs(MultipartFile file) throws IOException;
  
  public Resource loadAvatharFileAsResource(String filename);
}
