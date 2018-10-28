package com.dropboxlite.website.config;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
  @Bean
  public DropboxLiteAPIClient client() {
    return new DropboxLiteAPIClient("dropbox-api-1610903489.us-east-1.elb.amazonaws.com:7777");
  }
}
