package com.dropboxlite.website.controller;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import com.dropboxlite.website.client.model.DeleteFileOutput;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DecimalFormat;

@Controller
public class AdminController {
  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

  @Autowired
  private DropboxLiteAPIClient apiClient;

  @GetMapping("/admin")
  public String listAllFiles(Model model) throws IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getDetails();
    logger.info("Received Admin List files request. User {}", user.getUserId());

    if (!user.isAdminUser()) {
      return "redirect:/list";
    }

    ListFileOutput output = apiClient.listAllFiles();
    model.addAttribute("firstName", user.getFirstName());
    if (output.getFiles().size() > 0) {
      output.getFiles()
          .parallelStream()
          .forEach(e -> e.setReadableFileSize(getReadableFileSize(e.getFileSize())));

      model.addAttribute("file", output.getFiles());
    } else {
      model.addAttribute("message", "No files available.");
    }
    return "adminHome";

  }

  @PostMapping("/deleteAdminFile")
  public String deleteFile(@RequestParam("hiddenUserId") int userId,
                           @RequestParam("hiddenFileName") String fileName,
                           RedirectAttributes redirectAttributes) throws IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getDetails();
    logger.info("Received Admin Delete files request. User {}", user.getUserId());

    DeleteFileOutput output = apiClient.deleteFile(userId, fileName);
    redirectAttributes.
        addFlashAttribute("delete_message", "File Deleted Successfully.");
    redirectAttributes.addFlashAttribute("alertClass", "alert-success");

    return "redirect:/admin";

  }

  private String getReadableFileSize(long size) {
    final String[] units = new String[]{"B", "KB", "MB"};
    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
    return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " "
        + units[digitGroups];
  }
}
