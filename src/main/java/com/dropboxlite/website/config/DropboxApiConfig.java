package com.dropboxlite.website.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("dropboxlite")
@Getter
@Setter
@Component
@ToString
public class DropboxApiConfig {
  private String dns;
}
