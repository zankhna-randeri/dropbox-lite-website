package com.dropboxlite.website.client.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class RegisterOutput {
  private boolean userCreated;
  private int userId;
}