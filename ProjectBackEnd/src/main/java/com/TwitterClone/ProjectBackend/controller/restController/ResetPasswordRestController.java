package com.TwitterClone.ProjectBackend.controller.restController;

import com.TwitterClone.ProjectBackend.DTO.ForgotPasswordRequest;
import com.TwitterClone.ProjectBackend.DTO.ResetPasswordRequest;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.TweetInformation;
import com.TwitterClone.ProjectBackend.service.MailService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import com.TwitterClone.ProjectBackend.userManagement.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Operation(summary = "Send a mail to reset a user's password in case it was forgotten")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link successfully send", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "404", description = "Mail not found", content = @Content)
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Object> processForgotPassword(@RequestBody ForgotPasswordRequest request) throws MessagingException, UnsupportedEncodingException {
        String passwordToken = RandomString.make(30);

        if (this.userService.findByMail(request.getEmail())== null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userService.updateResetPasswordToken(passwordToken, request.getEmail());
        String resetPasswordLink = "https://localhost:8443/reset-password?passwordToken=" + passwordToken;
        mailService.sendResetPasswordMail(request.getEmail(), resetPasswordLink);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Change the user's password for a new one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password has been reset", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "404", description = "Token not found", content = @Content)
    })
    @PutMapping("/reset-password")
    public ResponseEntity<Object> processResetPassword(@RequestParam String passwordToken,
                                                       @RequestBody ResetPasswordRequest request) {

        User user = userService.getByResetPasswordToken(passwordToken);

        if (user != null) {
            userService.updatePassword(user, request.getPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }
}
