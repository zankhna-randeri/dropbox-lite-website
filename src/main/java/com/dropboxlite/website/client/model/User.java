package com.dropboxlite.website.client.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class User {
  private int userId;
  private String firstName;
  private String lastName;
  private String userEmail;
  private String password;
  @Builder.Default
  private boolean adminUser = false;
}
