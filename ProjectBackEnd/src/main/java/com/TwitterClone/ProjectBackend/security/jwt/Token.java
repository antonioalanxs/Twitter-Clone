package com.TwitterClone.ProjectBackend.security.jwt;

import java.time.LocalDateTime;

public class Token {

    private TokenType tokenType;
    private String tokenValue;
    private Long duration;
    private LocalDateTime expiryDate;

    public enum TokenType {
        ACCESS, REFRESH
    }

    public Token(TokenType tokenType, String tokenValue, Long duration, LocalDateTime expiryDate) {
        super();
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
        this.duration = duration;
        this.expiryDate = expiryDate;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "Token [tokenType=" + tokenType + ", tokenValue=" + tokenValue + ", duration=" + duration
                + ", expiryDate=" + expiryDate + "]";
    }
}
