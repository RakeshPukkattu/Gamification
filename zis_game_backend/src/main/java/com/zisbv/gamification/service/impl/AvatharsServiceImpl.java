package com.zisbv.gamification.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.zisbv.gamification.config.FileStorageProperties;
import com.zisbv.gamification.entities.Avathars;
import com.zisbv.gamification.exceptions.FileStorageException;
import com.zisbv.gamification.exceptions.MyFileNotFoundException;
import com.zisbv.gamification.repositories.AvatharsRepository;
import com.zisbv.gamification.service.AvatharsService;
import com.zisbv.gamification.util.AppConstants;




@Service(value = "avatharsService")
public class AvatharsServiceImpl implements AvatharsService {

  @Autowired
  AvatharsRepository avatharsRepository;
  
  private final Path fileStorageLocation1;
  
  @Autowired
  public AvatharsServiceImpl(FileStorageProperties fileStorageAvathar) {
        this.fileStorageLocation1 =
        Paths.get(fileStorageAvathar.getAvatharDir()).toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileStorageLocation1);
    } catch (Exception ex) {
      throw new FileStorageException(AppConstants.FILE_STORAGE_EXCEPTION_PATH_NOT_FOUND, ex);
    }
  }

  @Override
  public List<Avathars> findAll() {
    List<Avathars> list = new ArrayList<>();
    avatharsRepository.findAll().iterator().forEachRemaining(list::add);
    return list;
  }

  @Override
  public Avathars save(String key) {
    Avathars avathar = new Avathars();
    avathar.setAvatharKey(key);
    return avatharsRepository.save(avathar);
  }

  @Override
  public String storeAvatharThumbs(MultipartFile file) throws IOException {
    if (!(file.getOriginalFilename().endsWith(AppConstants.PNG_FILE_FORMAT)
        || file.getOriginalFilename().endsWith(AppConstants.JPEG_FILE_FORMAT)
        || file.getOriginalFilename().endsWith(AppConstants.JPG_FILE_FORMAT)))
      throw new FileStorageException(AppConstants.INVALID_FILE_FORMAT);

    File f = new File(AppConstants.TEMP_DIR + file.getOriginalFilename());

    f.createNewFile();
    FileOutputStream fout = new FileOutputStream(f);
    fout.write(file.getBytes());
    fout.close();

    if (f.exists()) f.delete();

    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    try {
      if (fileName.contains(AppConstants.INVALID_FILE_DELIMITER)) {
        throw new FileStorageException(AppConstants.INVALID_FILE_PATH_NAME + fileName);
      }
      String newFileName = System.currentTimeMillis() + AppConstants.FILE_SEPERATOR + fileName;
      Path targetLocation = this.fileStorageLocation1.resolve(newFileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return newFileName;
    } catch (IOException ex) {
      throw new FileStorageException(String.format(AppConstants.FILE_STORAGE_EXCEPTION, fileName),
          ex);
    }
  }

  @Override
  public Resource loadAvatharFileAsResource(String fileName) {
    try {
      Path filePath = this.fileStorageLocation1.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new MyFileNotFoundException(AppConstants.FILE_NOT_FOUND + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new MyFileNotFoundException(AppConstants.FILE_NOT_FOUND + fileName, ex);
    }
  }


}
