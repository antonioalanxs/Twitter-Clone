package com.TwitterClone.ProjectBackend.controller;

import com.TwitterClone.ProjectBackend.service.MailService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import com.TwitterClone.ProjectBackend.userManagement.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Manages all the petitions referent to reset the password of user
 */
@Controller
public class ResetPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    /**
     * Change the current page to forgot password page
     *
     * @param model
     * @return
     */
    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "forgot-password";
    }

    /**
     * Obtain the email of the user to prepare a token to be used to change the password
     *
     * @param request
     * @param model
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/forgot-password")
    public String processForgotPassword(HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        String passwordToken = RandomString.make(30);

        userService.updateResetPasswordToken(passwordToken, email);
        String resetPasswordLink = "https://localhost:8443/reset-password?passwordToken=" + passwordToken;
        mailService.sendResetPasswordMail(email, resetPasswordLink);

        return "forgot-password-confirmation";
    }

    /**
     * Change the current page to forgot password confirmation page
     *
     * @return
     */
    @GetMapping("/forgot-password-confirmation")
    public String forgotPasswordConfirmation() {
        return "forgot-password-confirmation";
    }

    /**
     * Change the current page to reset password page
     *
     * @param passwordToken
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/reset-password")
    public String resetPasswordPage(@Param("passwordToken") String passwordToken, Model model, HttpServletRequest request) {
        CsrfToken token_csrf = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("passwordToken", passwordToken);
        model.addAttribute("_csrf", token_csrf.getToken());
        return "reset-password";
    }

    /**
     * Change the current password to the new one
     *
     * @param passwordToken
     * @param password
     * @return
     */
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String passwordToken,
                                       @RequestParam String password) {

        User user = userService.getByResetPasswordToken(passwordToken);

        if (user != null) {
            userService.updatePassword(user, password);
            return "redirect:/password-reset-confirmation";
        }

        return "error";
    }

    /**
     * Change the current page to password reset confirmation
     *
     * @return
     */
    @GetMapping("/password-reset-confirmation")
    public String passwordResetConfirmation() {
        return "password-reset-confirmation";
    }
}
