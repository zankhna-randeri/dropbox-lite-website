package com.dropboxlite.website.config;

import com.dropboxlite.website.auth.CustomAuthProvider;
import com.dropboxlite.website.auth.SuccessHandler;
import com.dropboxlite.website.client.DropboxLiteAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private DropboxLiteAPIClient client;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    /*http.authorizeRequests()
        .antMatchers("/", "/ping","/login*", "/register", "/registerSubmit", "/css/*", "/img/***")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginProcessingUrl("/login")
        .passwordParameter("inputPassword")
        .usernameParameter("inputEmail")
        .successForwardUrl("/list")
        .successHandler(new SuccessHandler())
        .permitAll()
        .and()
        .logout()
        .permitAll();*/

    http
        .authorizeRequests()
          .antMatchers("/", "/css/*", "/img/*", "/ping", "/register*", "/login").permitAll()
          .anyRequest().authenticated()
          .and()
        .formLogin()
          .loginPage("/login")
          .passwordParameter("inputPassword")
          .usernameParameter("inputEmail")
          .successForwardUrl("/list")
          //FIXME: handle failure case on login page and show messages
          .failureForwardUrl("/")
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
}
