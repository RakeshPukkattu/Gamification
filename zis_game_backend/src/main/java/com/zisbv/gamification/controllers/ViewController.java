package com.zisbv.gamification.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.zisbv.gamification.util.AppConstants;

@Controller
public class ViewController {

  @CrossOrigin(origins = "*")
  @GetMapping(value = AppConstants.INDEX_PAGE_URI)
  public String indexPage() {
      return AppConstants.INDEX_PAGE;
  }
}
