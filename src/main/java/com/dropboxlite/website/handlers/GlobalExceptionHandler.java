package com.dropboxlite.website.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = MaxUploadSizeExceededException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public String sizeExceeded() {
    return "Max file size can not exceeds 10 MB.";
  }
}