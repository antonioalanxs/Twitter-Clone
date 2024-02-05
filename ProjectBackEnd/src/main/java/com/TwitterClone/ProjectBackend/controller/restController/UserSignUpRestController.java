package com.TwitterClone.ProjectBackend.controller.restController;

import com.TwitterClone.ProjectBackend.DTO.RegisteredRequest;
import com.TwitterClone.ProjectBackend.userManagement.User;
import com.TwitterClone.ProjectBackend.userManagement.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserSignUpRestController {
    @Autowired
    private UserService service;

    @Operation(summary = "Redirect the user to the signUp view")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody RegisteredRequest request)
            throws MessagingException, IOException {
        boolean hasBeenRegistered = service.signup(request);

        if (hasBeenRegistered) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();

    }

    @Operation(summary = "Check the verification code to create an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account verified", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "400", description = "invalid Token", content = @Content)
    })
    @GetMapping("/verification")
    public ResponseEntity<Object> verifyUser(@Param("code") String code) {

        if (service.verify(code)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

}
