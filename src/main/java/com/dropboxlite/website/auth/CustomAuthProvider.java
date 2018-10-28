package com.dropboxlite.website.auth;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import com.dropboxlite.website.client.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAuthProvider implements AuthenticationProvider {
  public static final String ROLE_ADMIN = "ADMIN";
  public static final String ROLE_USER = "USER";
  private static final Logger logger = LoggerFactory.getLogger(CustomAuthProvider.class);

  private DropboxLiteAPIClient apiClient;

  public CustomAuthProvider(DropboxLiteAPIClient apiClient) {
    this.apiClient = apiClient;
  }

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {

    String name = authentication.getName();
    Object credentials = authentication.getCredentials();
    if (!(credentials instanceof String)) {
      return null;
    }
    String password = credentials.toString();
    logger.info("username: {}, password: {}", name, password);
    logger.info("Authentication request: {}", authentication);
    User user = null;
    try {
      user = apiClient.loginUser(name, password);
    } catch (IOException e) {
      logger.error("Error retrieving auth info.", e);
    }
    if (user == null) {
      throw new BadCredentialsException("Authentication failed for " + name);
    }

    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority(user.isAdminUser() ? ROLE_ADMIN : ROLE_USER));
    Authentication auth = new UsernamePasswordAuthenticationToken(user.getUserId(),
        password,
        grantedAuthorities);
    ((UsernamePasswordAuthenticationToken) auth).setDetails(user);
    return auth;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
