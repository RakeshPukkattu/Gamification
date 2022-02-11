package com.zisbv.gamification.util;

public class AppConstants {

  //Image upload
  public static final String FILE_PROPERTIES_PREFIX = "file";
  public static final String INDEX_PAGE_URI = "/index";
  public static final String INDEX_PAGE = "index";
  public static final String FILE_STORAGE_EXCEPTION_PATH_NOT_FOUND = "Could not create the directory where the uploaded files will be stored";
  public static final String INVALID_FILE_DIMENSIONS = "Invalid file dimensions. File dimension should note be more than 300 X 300";
  public static final String INVALID_FILE_FORMAT = "Only PNG, JPEG and JPG images are allowed";
  public static final String PNG_FILE_FORMAT = ".png";
  public static final String JPEG_FILE_FORMAT = ".jpeg";
  public static final String JPG_FILE_FORMAT = ".jpg";
  public static final String TEMP_DIR = "avatharUploads/uploads";
  public static final String INVALID_FILE_PATH_NAME = "Sorry! Filename contains invalid path sequence";
  public static final String FILE_NOT_FOUND = "File not found ";
  public static final String FILE_STORAGE_EXCEPTION = "Could not store file %s !! Please try again!";
  public static final CharSequence INVALID_FILE_DELIMITER = "..";
  public static final String FILE_SEPERATOR = "_";
  
  //Controllers
  public static final String USER_SERVICE_URI = "/userManagement";
  public static final String USER_UPLOAD_URI = "/addNewUser0";
  public static final String USER_MODIFY_URI ="/modifyUser/{id}";
  public static final String USER_JSON_PARAM = "userJson";
  public static final String USER_FILE_PARAM = "file";
  public static final String DOWNLOAD_PATH = "userManagement/downloadFile/";
  public static final String THEME_DOWNLOAD_PATH = "adminUIThemeManagement/downloadFile/";
  public static final String COMPONENT_DOWNLOAD_PATH = "adminComponentsManagement/downloadFile/";
  public static final String GAME_DOWNLOAD_PATH = "adminGamesManagement/downloadFile/";
  public static final String SUCCESS_CODE = "200-OK";
  public static final String SUCCESS_MSG = "User created successfully";
  public static final String DOWNLOAD_URI = "/downloadFile/{fileName:.+}";
  public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
  public static final String FILE_DOWNLOAD_HTTP_HEADER = "attachment; filename=\"%s\"";
  public static final String USER_CRED_URI = "/getCredentials/{email}";
  public static final String USER_UPDATE_PASSWORD_URI = "/updatePassword/{email}";
  public static final String USER_UPDATE_STATUS_URI ="/updateStatus/{email}";

}
