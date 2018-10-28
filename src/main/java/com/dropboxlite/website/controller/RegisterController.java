package com.dropboxlite.website.controller;

import com.dropboxlite.website.client.DropboxLiteAPIClient;
import com.dropboxlite.website.client.model.RegisterOutput;
import com.dropboxlite.website.client.model.User;
import com.dropboxlite.website.constant.CookieConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RegisterController {

  private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

  @Autowired
  private DropboxLiteAPIClient client;

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String register() {
    return "register";
  }

  @PostMapping(value = "/registerSubmit")
  public String registerUser(@RequestParam("inputFirstName") String firstName,
                             @RequestParam("inputLastName") String lastName,
                             @RequestParam("inputEmail") String email,
                             @RequestParam("inputPassword") String password,
                             @RequestParam("inputConfirmPassword") String confirmPassword,
                             RedirectAttributes redirectAttributes,
                             HttpServletResponse servletResponse) throws IOException {
    logger.info("Received register user request");
    User user = User.builder().firstName(firstName).lastName(lastName)
        .userEmail(email).password(password).build();
    RegisterOutput output = client.registerUser(user);
    logger.info("User registration response. {}", output);
    if (output.isUserCreated()) {
      servletResponse.addCookie(new Cookie(CookieConst.USER_ID, String.valueOf(output.getUserId())));
      servletResponse.addCookie(new Cookie(CookieConst.FIRST_NAME, firstName));
      redirectAttributes
          .addFlashAttribute("message", "Account created!");
    } else {
      redirectAttributes
          .addFlashAttribute("message", "Account already Exists!");
    }
    return "redirect:/";
  }
}
