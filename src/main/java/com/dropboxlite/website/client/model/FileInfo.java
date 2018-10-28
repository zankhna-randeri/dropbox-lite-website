package com.dropboxlite.website.client.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileInfo {
  private String fileName;
  private int userId;
  private long fileCreationTimestamp;
  private long fileUpdateTimestamp;
  private String s3Key;
  private String description;
  private long fileSize;
  private String userFirstName;
  private String readableFileSize;
}