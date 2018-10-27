package com.dropboxlite.website.config;

import com.dropboxlite.website.auth.CustomAuthProvider;
import com.dropboxlite.website.auth.SuccessHandler;
import com.dropboxlite.website.client.DropboxLiteAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private DropboxLiteAPIClient client;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/", "/ping","/login", "/register", "/registerSubmit", "/css/*", "/img/***").permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginPage("/")
        .loginProcessingUrl("/login")
        .passwordParameter("inputPassword")
        .usernameParameter("inputEmail")
        .successForwardUrl("/list")
        .successHandler(new SuccessHandler())
        .permitAll()
        .and()
        .logout()
        .permitAll();
  }

  @Override
  public void configure(AuthenticationManagerBuilder builder)
      throws Exception {
    builder.authenticationProvider(new CustomAuthProvider(client));
  }
}
