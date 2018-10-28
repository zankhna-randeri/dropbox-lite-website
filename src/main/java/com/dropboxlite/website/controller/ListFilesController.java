package com.dropboxlite.website.controller;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import com.dropboxlite.website.client.model.ListFileOutput;
import com.dropboxlite.website.client.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ListFilesController {
  private static final Logger logger = LoggerFactory.getLogger(ListFilesController.class);

  @Autowired
  private DropboxLiteAPIClient client;

  @GetMapping("/list")
  public String list(Model model) throws IOException {
    User user = getUserId();
    logger.info("Received List files request. User {}", user.getUserId());
    ListFileOutput output = client.listFiles(user.getUserId());
    model.addAttribute("firstName", user.getFirstName());
    if (output.getFiles().size() > 0) {
      model.addAttribute("file", output.getFiles());
      model.addAttribute("message", "Your Files");
    } else {
      model.addAttribute("message", "No files available.");
    }
    return "listFile";
  }


  private User getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    logger.debug("User Details: {}", authentication.getDetails());
    return (User) authentication.getDetails();
  }
}
