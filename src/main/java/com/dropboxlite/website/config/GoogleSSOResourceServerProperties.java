package com.dropboxlite.website.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("security.oauth2.google.resource")
@Getter
@Setter
@ToString
@Component
public class GoogleSSOResourceServerProperties extends ResourceServerProperties {
}
