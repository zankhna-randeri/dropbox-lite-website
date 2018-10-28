package com.dropboxlite.website.client;

import com.dropboxlite.website.client.exception.InternalServerException;
import com.dropboxlite.website.client.exception.InvalidRequestException;
import com.dropboxlite.website.client.model.*;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

public class DropboxLiteAPIClient {

  private static final Logger logger = LoggerFactory.getLogger(DropboxLiteAPIClient.class);

  private static final String HEADER_USER_ID = "userid";
  private static final String HEADER_USER_EMAIL = "email";
  private static final String HEADER_USER_PASSWORD = "password";

  private static final Gson GSON = new Gson();
  private final String dnsName;

  public DropboxLiteAPIClient(String dnsName) {
    this.dnsName = String.format("http://%s", dnsName);
  }

  public ListFileOutput listFiles(int userId) throws IOException {
    Response response = Request.Get(dnsName + "/listfile")
        .setHeader(HEADER_USER_ID, String.valueOf(userId))
        .execute();
    return response.handleResponse(new ResponseHandler<ListFileOutput>() {
      @Override
      public ListFileOutput handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
          // parse response & convert to ListFileOutput
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(
              httpResponse.getEntity().getContent()
          ))) {
            StringBuilder builder = new StringBuilder();
            String content;
            while ((content = reader.readLine()) != null) {
              builder.append(content);
            }

            logger.info("Response from API: Status Code: {}, Msg: {}", statusCode, builder.toString());
            return GSON.fromJson(builder.toString(), ListFileOutput.class);
          }
        } else if (statusCode >= 400 && statusCode < 500) {
          // raise bad request exception
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          throw new InvalidRequestException(msg);
        } else {
          // raise internal server error
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          throw new InternalServerException();
        }
      }
    });
  }

  public ListFileOutput listAllFiles() throws IOException {

    Response response = Request.Get(dnsName + "/listfileAdmin")
        .execute();
    return response.handleResponse(new ResponseHandler<ListFileOutput>() {
      @Override
      public ListFileOutput handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
          // parse response & convert to ListFileOutput
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(
              httpResponse.getEntity().getContent()
          ))) {
            StringBuilder builder = new StringBuilder();
            String content;
            while ((content = reader.readLine()) != null) {
              builder.append(content);
            }
            logger.info("Response from API: Status Code: {}, Msg: {}", statusCode, builder.toString());
            return GSON.fromJson(builder.toString(), ListFileOutput.class);
          }
        } else if (statusCode >= 400 && statusCode < 500) {
          // raise bad request exception
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          throw new InvalidRequestException(msg);
        } else {
          // raise internal server error
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          throw new InternalServerException();
        }
      }
    });
  }

  public RegisterOutput registerUser(User user) throws IOException {
    StringEntity entity = new StringEntity(GSON.toJson(user, User.class));
    Response response =
        Request.Post(dnsName + "/user")
            .setHeader("Content-Type", "application/json")
            .body(entity).execute();

    return response.handleResponse(new ResponseHandler<RegisterOutput>() {
      @Override
      public RegisterOutput handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
          // parse response & convert to ListFileOutput
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(
              httpResponse.getEntity().getContent()
          ))) {
            StringBuilder builder = new StringBuilder();
            String content;
            while ((content = reader.readLine()) != null) {
              builder.append(content);
            }

            logger.info("Response from API: Status Code: {}, Msg: {}", statusCode, builder.toString());
            return GSON.fromJson(builder.toString(), RegisterOutput.class);
          }
        } else if (statusCode >= 400 && statusCode < 500) {
          // raise bad request exception
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          throw new InvalidRequestException(msg);
        } else {
          // raise internal server error
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          throw new InternalServerException();
        }
      }
    });
  }

  public User loginUser(String email, String password) throws IOException {
    logger.info("Logging User API Call. email: {}, password: {}", email, password);
    Response response = Request.Get(dnsName + "/login")
        .setHeader(HEADER_USER_EMAIL, email)
        .setHeader(HEADER_USER_PASSWORD, password)
        .execute();
    return response.handleResponse(new ResponseHandler<User>() {
      @Override
      public User handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
          // parse response & convert to ListFileOutput
          try (BufferedReader reader =
                   new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))) {
            StringBuilder builder = new StringBuilder();
            String content;
            while ((content = reader.readLine()) != null) {
              builder.append(content);
            }

            logger.info("Response from API: Status Code: {}, Msg: {}", statusCode, builder.toString());
            return GSON.fromJson(builder.toString(), User.class);
          }
        } else if (statusCode >= 400 && statusCode < 500) {
          // raise bad request exception
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          throw new InvalidRequestException(msg);
        } else {
          // raise internal server error
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          throw new InternalServerException();
        }
      }
    });
  }

  public UploadFileOutput uploadFile(int userId,
                                     MultipartFile inputFile,
                                     String description) throws IOException {

    HttpEntity entity = MultipartEntityBuilder.create()
        .addBinaryBody("file",
            inputFile.getInputStream(),
            ContentType.MULTIPART_FORM_DATA,
            inputFile.getOriginalFilename())
        .addTextBody("description", description)
        .build();

    Response response = Request.Post(dnsName + "/upload")
        .setHeader("userid", String.valueOf(userId))
        .body(entity)
        .execute();

    return response.handleResponse(new ResponseHandler<UploadFileOutput>() {
      @Override
      public UploadFileOutput handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
          // parse response & convert to ListFileOutput
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(
              httpResponse.getEntity().getContent()
          ))) {
            StringBuilder builder = new StringBuilder();
            String content;
            while ((content = reader.readLine()) != null) {
              builder.append(content);
            }

            logger.info("Response from API: Status Code: {}, Msg: {}", statusCode, builder.toString());
            return GSON.fromJson(builder.toString(), UploadFileOutput.class);
          }
        } else if (statusCode >= 400 && statusCode < 500) {
          // raise bad request exception
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          //TODO: add user friendly messages
          throw new InvalidRequestException(msg);
        } else {
          // raise internal server error
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          //TODO: add user friendly messages
          throw new InternalServerException();
        }
      }
    });
  }

  public DeleteFileOutput deleteFile(int userId, String fileName) throws IOException {

    fileName = URLEncoder.encode(fileName, "UTF-8");
    Response response = Request.Delete(dnsName + "/delete/" + userId + "/" + fileName)
        .execute();
    return response.handleResponse(new ResponseHandler<DeleteFileOutput>() {
      @Override
      public DeleteFileOutput handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
          // parse response & convert to ListFileOutput
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(
              httpResponse.getEntity().getContent()
          ))) {
            StringBuilder builder = new StringBuilder();
            String content;
            while ((content = reader.readLine()) != null) {
              builder.append(content);
            }

            logger.info("Response from API: Status Code: {}, Msg: {}", statusCode, builder.toString());
            return GSON.fromJson(builder.toString(), DeleteFileOutput.class);
          }
        } else if (statusCode >= 400 && statusCode < 500) {
          // raise bad request exception
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          //TODO: add user friendly messages
          throw new InvalidRequestException(msg);
        } else {
          // raise internal server error
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          //TODO: add user friendly messages
          throw new InternalServerException();
        }
      }
    });
  }

  public CreatePresignedUrlOutput downloadFile(int userId, String fileName) throws IOException {

    fileName = URLEncoder.encode(fileName, "UTF-8");
    Response response = Request.Get(dnsName + "/createPresignedURL/" + fileName)
        .setHeader("userId", String.valueOf(userId))
        .execute();
    return response.handleResponse(new ResponseHandler<CreatePresignedUrlOutput>() {
      @Override
      public CreatePresignedUrlOutput handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
          // parse response & convert to ListFileOutput
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(
              httpResponse.getEntity().getContent()
          ))) {
            StringBuilder builder = new StringBuilder();
            String content;
            while ((content = reader.readLine()) != null) {
              builder.append(content);
            }

            logger.info("Response from API: Status Code: {}, Msg: {}", statusCode, builder.toString());
            return GSON.fromJson(builder.toString(), CreatePresignedUrlOutput.class);
          }
        } else if (statusCode >= 400 && statusCode < 500) {
          // raise bad request exception
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          //TODO: add user friendly messages
          throw new InvalidRequestException(msg);
        } else {
          // raise internal server error
          String msg = httpResponse.getStatusLine().getReasonPhrase();
          logger.error("Response from API: Status Code: {}, Reason: {}", statusCode, msg);
          throw new InternalServerException();
        }
      }
    });

  }
}
