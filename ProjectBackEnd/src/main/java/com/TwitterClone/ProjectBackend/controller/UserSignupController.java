package com.TwitterClone.ProjectBackend.controller;

import com.TwitterClone.ProjectBackend.DTO.RegisteredRequest;
import com.TwitterClone.ProjectBackend.userManagement.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This class is on charge of managing all the petitions from the view to the model in the signUp process.
 */
@Controller
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupController {
    @Autowired
    private UserService service;

    /**
     * Change the current page to signup page
     *
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/signup")
    public String signup(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        return "signup";
    }

    /**
     * Register a new user to the website and sends an email to the new user
     *
     * @param username
     * @param password
     * @param email
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String email)
            throws MessagingException, IOException {
        RegisteredRequest registeredRequest = new RegisteredRequest(email, username, password);

        if (service.signup(registeredRequest)) {
            return "confirmation";
        }

        return "error";
    }

    /**
     * Verify the user account
     *
     * @param code
     * @return
     */
    @GetMapping("/verification")
    public String verifyUser(@Param("code") String code) {

        if (service.verify(code)) {
            return "verify";
        }

        return "error";
    }
}