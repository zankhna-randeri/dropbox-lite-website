package com.dropboxlite.website.client.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePresignedUrlOutput {
  private String preSignedUrl;
}
