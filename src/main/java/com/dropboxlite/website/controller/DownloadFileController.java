package com.dropboxlite.website.controller;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import com.dropboxlite.website.client.model.CreatePresignedUrlOutput;
import com.dropboxlite.website.client.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URL;

@Controller
public class DownloadFileController {
  private static final Logger logger = LoggerFactory.getLogger(DownloadFileController.class);

  @Autowired
  private DropboxLiteAPIClient client;

  private User getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    logger.debug("User Details: {}", authentication.getDetails());
    return (User) authentication.getDetails();
  }


  @PostMapping("/download")
  public ResponseEntity<Resource> serveFile(@RequestParam("hiddenFileName") String fileName) throws IOException {
    User user = getUserId();
    logger.info("Received Download file request. User {}", user.getUserId());
    CreatePresignedUrlOutput output = client.downloadFile(user.getUserId(), fileName);
    URL fileUrl = new URL(output.getPreSignedUrl());
    Resource resource = new UrlResource(fileUrl);
    if (resource.exists() || resource.isReadable()) {
      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + resource.getFilename()).body(resource);
    } else {
      throw new RuntimeException("Could not read file: " + fileName);
    }
  }
}
