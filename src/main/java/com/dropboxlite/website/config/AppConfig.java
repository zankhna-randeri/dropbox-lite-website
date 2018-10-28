package com.dropboxlite.website.config;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
