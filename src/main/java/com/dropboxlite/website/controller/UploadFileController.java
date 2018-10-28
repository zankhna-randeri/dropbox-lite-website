package com.dropboxlite.website.controller;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import com.dropboxlite.website.client.model.UploadFileOutput;
import com.dropboxlite.website.client.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class UploadFileController {
  private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);

  @Autowired
  private DropboxLiteAPIClient client;

  private User getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    logger.debug("User Details: {}", authentication.getDetails());
    return (User) authentication.getDetails();
  }

  @PostMapping("/upload")
  public String uploadFile(@RequestParam("file") MultipartFile file,
                           @RequestParam("description") String description,
                           RedirectAttributes redirectAttributes) throws IOException {
    User user = getUserId();
    logger.info("Received Upload file request. User {}", user.getUserId());
    UploadFileOutput output = client.uploadFile(user.getUserId(), file, description);
    redirectAttributes
        .addFlashAttribute("message", String.format("File upload: %s", output.isResult()));
    return "redirect:/list";
  }
}
