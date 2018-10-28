package com.dropboxlite.website.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

public class SuccessHandler implements AuthenticationSuccessHandler {
  private static final Logger logger = LoggerFactory.getLogger(SuccessHandler.class);
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException {
    handle(request, response, authentication);
    clearAuthenticationAttributes(request);
  }

  private void handle(HttpServletRequest request,
                      HttpServletResponse response,
                      Authentication authentication) throws IOException {

    String targetUrl = determineTargetUrl(authentication, response);

    if (response.isCommitted()) {
      logger.debug("Response has already been committed. Unable to redirect to {}", targetUrl);
      return;
    }

    redirectStrategy.sendRedirect(request, response, targetUrl);
  }

  private String determineTargetUrl(Authentication authentication,
                                    HttpServletResponse response) {
    Collection<? extends GrantedAuthority> authorities
        = authentication.getAuthorities();
    for (GrantedAuthority grantedAuthority : authorities) {
      switch (grantedAuthority.getAuthority()) {
        case CustomAuthProvider.ROLE_ADMIN:
          return "/admin";

        case CustomAuthProvider.ROLE_USER:
          return "/list";

      }
    }

    throw new IllegalStateException("User Granted Role is neither admin nor user");
  }

  private void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }
}
