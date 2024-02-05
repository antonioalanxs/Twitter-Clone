package com.TwitterClone.ProjectBackend.controller.restController;

import com.TwitterClone.ProjectBackend.security.jwt.AuthResponse;
import com.TwitterClone.ProjectBackend.security.jwt.LoginRequest;
import com.TwitterClone.ProjectBackend.security.jwt.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class LoginRestController {

    @Autowired
    private UserLoginService userService;

    @Operation(summary = "Check the credentials of a user and let them in if they are correct")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log In successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "400", description = "invalid credentials", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest, accessToken, refreshToken);
    }

    @Operation(summary = "Refresh the content of a page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page has been successfully refreshed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "404", description = "URL not found", content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        return userService.refresh(refreshToken);
    }

    @Operation(summary = "Log a user out of the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log Out successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "400", description = "invalid request", content = @Content)
    })
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userService.logout(request, response)));
    }
}
