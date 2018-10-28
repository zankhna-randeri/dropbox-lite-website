package com.dropboxlite.website.config;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;

@Configuration
public class AppConfig {

  private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

  @Autowired
  private DropboxApiConfig config;

  @Bean
  public DropboxLiteAPIClient client() {
    logger.info("API Config: {}", config);
    return new DropboxLiteAPIClient(config.getDns());
  }

  @Bean
  public FilterRegistrationBean oauth2ClientFilterRegistration(
      OAuth2ClientContextFilter filter) {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(filter);
    registration.setOrder(-100);
    return registration;
  }
}
