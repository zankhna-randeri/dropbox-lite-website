package com.dropboxlite.website.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;
import com.dropboxlite.website.auth.CustomAuthProvider;
import com.dropboxlite.website.auth.SuccessHandler;
import com.dropboxlite.website.client.DropboxLiteAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private DropboxLiteAPIClient client;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .antMatcher("/**")
          .authorizeRequests()
        .antMatchers("/", "/css/*", "/img/*", "/ping", "/register*", "/login",
            "/registerSubmit", "/loginGoogle", "/js/*").permitAll()
          .anyRequest().authenticated()
        .and()
          .addFilterBefore(singleSignOnFilters(), BasicAuthenticationFilter.class)
          .authenticationProvider(new CustomAuthProvider(client))
        .formLogin()
           .loginPage("/login")
              .usernameParameter("inputEmail")
              .passwordParameter("inputPassword")
              .successForwardUrl("/list")
              .successHandler(new SuccessHandler())
           .permitAll()
         .and()
            .logout()
              .logoutSuccessUrl("/")
            .permitAll();
  }

  @Override
  public void configure(AuthenticationManagerBuilder builder)
      throws Exception {
    builder.authenticationProvider(new CustomAuthProvider(client));
  }

  @Autowired
  private GoogleSSOAuhtroizationCodeResourceProperties googleClient;

  @Autowired
  private GoogleSSOResourceServerProperties googleResource;

  @Autowired
  private OAuth2ClientContext oauth2ClientContext;

  private Filter singleSignOnFilters() {
    CompositeFilter filter = new CompositeFilter();
    List<Filter> filters = new ArrayList<>();
    filters.add(ssoFilter(googleClient, googleResource, oauth2ClientContext, "/loginGoogle"));
    filter.setFilters(filters);
    return filter;
  }

  private Filter ssoFilter(AuthorizationCodeResourceDetails client,
                           ResourceServerProperties resourceServer,
                           OAuth2ClientContext oauth2ClientContext,
                           String path) {
    OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
    OAuth2RestTemplate template = new OAuth2RestTemplate(client, oauth2ClientContext);
    filter.setRestTemplate(template);
    UserInfoTokenServices tokenServices = new UserInfoTokenServices(
        resourceServer.getUserInfoUri(), client.getClientId());
    tokenServices.setRestTemplate(template);
    filter.setTokenServices(tokenServices);
    return filter;
  }
}
