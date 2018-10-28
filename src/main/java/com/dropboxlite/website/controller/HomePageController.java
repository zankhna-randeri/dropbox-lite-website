package com.dropboxlite.website.controller;

import com.dropboxlite.website.auth.CustomAuthProvider;
import com.dropboxlite.website.client.DropboxLiteAPIClient;
import com.dropboxlite.website.client.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class HomePageController {

  private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);

  @Autowired
  private DropboxLiteAPIClient apiClient;

  @RequestMapping("/")
  public String homePage() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      return "login";
    }

    logger.info("User authenticated? {}", auth.isAuthenticated());
    logger.info("Auth Details {}", auth);

    if (auth.getAuthorities().contains(new SimpleGrantedAuthority(CustomAuthProvider.ROLE_ADMIN))
        || auth.getAuthorities().contains(new SimpleGrantedAuthority(CustomAuthProvider.ROLE_USER))) {

      if (auth.isAuthenticated()) {
        User user = (User) auth.getDetails();
        logger.info("is admin user? {}", user.isAdminUser());
        return String.format("redirect:/%s", user.isAdminUser() ? "admin" : "list");
      }
    }
    return "login";
  }

  @GetMapping("/loginGoogle")
  public String loginWithGoogle(Principal principal) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    logger.info("Principle {}", principal);
    logger.info("Auth {}", auth);
    throw new RuntimeException("test");
  }

  @GetMapping("/loginFacebook")
  public String loginWithFacebook(Principal principal) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    logger.info("Principle {}", principal);
    logger.info("Auth {}", auth);
    throw new RuntimeException("test");
  }
}
