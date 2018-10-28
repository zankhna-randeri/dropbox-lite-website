package com.dropboxlite.website.controller;

import com.dropboxlite.website.auth.CustomAuthProvider;
import com.dropboxlite.website.client.DropboxLiteAPIClient;
import com.dropboxlite.website.client.model.RegisterOutput;
import com.dropboxlite.website.client.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    if (auth.isAuthenticated()) {
      Set<String> whitelistedAuthorities = new HashSet<>();
      whitelistedAuthorities.add(CustomAuthProvider.ROLE_ADMIN);
      whitelistedAuthorities.add(CustomAuthProvider.ROLE_USER);
      whitelistedAuthorities.add("ROLE_USER");

      if (whitelistedAuthorities.contains(auth.getAuthorities().iterator().next().getAuthority())) {
        // user is authenticated now check for authorization in DB
        if (auth.getDetails() instanceof User) {
          return handleDropboxUser((User) auth.getDetails());
        } else {
          // FIXME: we can also check with OAuth2Authentication class
          // User is sign in using Google or FB
          return handleSSOUser((OAuth2Authentication) auth);
        }
      }
    }
    return "login";
  }

  private String handleSSOUser(OAuth2Authentication auth) {
    // Accquire user info

    Map<String, String> details = (Map<String, String>) auth.getUserAuthentication().getDetails();
    String email = details.get("email");
    String firstname = details.get("given_name");
    String lastname = details.get("family_name");
    String password = details.get("id");
    User user = User.builder()
        .userEmail(email)
        .firstName(firstname)
        .lastName(lastname)
        .password(password)
        .adminUser(false)
        .build();
    try {
     RegisterOutput output = apiClient.registerUser(user);
     user.setUserId(output.getUserId());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    auth.setDetails(user);
    return "redirect:/list";
  }

  private String handleDropboxUser(User user) {
    logger.info("is admin user? {}", user.isAdminUser());
    return String.format("redirect:/%s", user.isAdminUser() ? "admin" : "list");
  }
}
