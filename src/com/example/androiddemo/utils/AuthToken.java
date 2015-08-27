package com.example.androiddemo.utils;

/**
 * Created by kapiljain on 11/07/15.
 */
public class AuthToken {
    private String authToken;
    private String authTokenSecret;

    public AuthToken(String authToken, String authTokenSecret) {
        this.authToken = authToken;
        this.authTokenSecret = authTokenSecret;
    }

    public String getAuthToken() {

        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthTokenSecret() {
        return authTokenSecret;
    }

    public void setAuthTokenSecret(String authTokenSecret) {
        this.authTokenSecret = authTokenSecret;
    }
}
