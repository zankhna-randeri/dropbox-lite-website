package com.dropboxlite.website.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.stereotype.Component;

@ConfigurationProperties("security.oauth2.google.client")
@Getter
@Setter
@ToString
@Component
public class GoogleSSOAuhtroizationCodeResourceProperties extends AuthorizationCodeResourceDetails {
}
