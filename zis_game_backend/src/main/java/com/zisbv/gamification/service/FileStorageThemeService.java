package com.zisbv.gamification.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageThemeService {

	  public String storeFile(MultipartFile file) throws IOException;
	  
	  String storeZipFile(MultipartFile file) throws IOException;
	  
	  public Resource loadFileAsResource(String fileName);
}
