package com.TwitterClone.ProjectBackend.security.jwt;

public class AuthResponse {

    private Status status;
    private String message;
    private String error;

    public enum Status {
        SUCCESS, FAILURE
    }

    public AuthResponse() {
    }

    public AuthResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public AuthResponse(Status status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "LoginResponse [status=" + status + ", message=" + message + ", error=" + error + "]";
    }

}
