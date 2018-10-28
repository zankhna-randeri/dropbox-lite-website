package com.dropboxlite.website.client.exception;

public class InvalidRequestException extends RuntimeException {
  private String reason;

  public InvalidRequestException(String reason) {
    this.reason = reason;
  }

  public String reason() {
    return this.reason;
  }
}
