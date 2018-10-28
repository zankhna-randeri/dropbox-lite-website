package com.dropboxlite.website.controller;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import com.dropboxlite.website.client.model.DeleteFileOutput;
import com.dropboxlite.website.client.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class DeleteFileController {

  private static final Logger logger = LoggerFactory.getLogger(DeleteFileController.class);

  @Autowired
  private DropboxLiteAPIClient client;

  @PostMapping("/deleteFile")
  public String deleteFile(@RequestParam("hiddenFileName") String fileName,
                           RedirectAttributes redirectAttributes) throws IOException {
    User user = getUserId();
    logger.info("Received Delete file request. User {}", user.getUserId());
    DeleteFileOutput output = client.deleteFile(user.getUserId(), fileName);
    redirectAttributes
        .addFlashAttribute("message", String.format("File Deleted: %s", output.getStatus()));
    return "redirect:/list";
  }

  private User getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    logger.debug("User Details: {}", authentication.getDetails());
    return (User) authentication.getDetails();
  }

}
