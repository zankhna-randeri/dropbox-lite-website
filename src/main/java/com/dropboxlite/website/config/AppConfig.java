package com.dropboxlite.website.config;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class AppConfig {

  private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

  @Autowired
  private DropboxApiConfig config;

  @Bean
  public DropboxLiteAPIClient client() {
    logger.info("API Config: {}", config);
    return new DropboxLiteAPIClient(config.getDns());
  }

  @Autowired
  private GoogleSSOClientProperties googleClient;

  @Autowired
  private GoogleSSOResourceProperties googleResource;

  @Bean
  public AuthorizationCodeResourceDetails google() {
    AuthorizationCodeResourceDetails details = googleClient;
    logger.info("Google Auth Client: {}", details);
    return details;
  }

  @Bean
  @ConfigurationProperties
  public ResourceServerProperties googleResource() {
    ResourceServerProperties properties = googleResource;
    logger.info("Google Auth Resource Properties: {}", properties);
    return properties;
  }
}
