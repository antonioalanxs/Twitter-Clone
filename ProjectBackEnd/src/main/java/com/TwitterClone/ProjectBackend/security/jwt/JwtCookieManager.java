package com.TwitterClone.ProjectBackend.security.jwt;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieManager {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "AuthToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";

    public HttpCookie createAccessTokenCookie(String token, Long duration) {
        String encryptedToken = SecurityCipher.encrypt(token);
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, encryptedToken).maxAge(-1).httpOnly(true).path("/").build();
    }

    public HttpCookie createRefreshTokenCookie(String token, Long duration) {
        String encryptedToken = SecurityCipher.encrypt(token);
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, encryptedToken).maxAge(-1).httpOnly(true).path("/").build();
    }

    public HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "").maxAge(0).httpOnly(true).path("/").build();
    }

}
