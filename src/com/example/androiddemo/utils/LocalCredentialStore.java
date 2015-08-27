package com.example.androiddemo.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalCredentialStore {

    private static final String OAUTH_TOKEN = "oauth_token";
    private static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";

    private SharedPreferences prefs;

    public LocalCredentialStore(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public AuthToken getToken() {
        AuthToken authToken = new AuthToken(prefs.getString(OAUTH_TOKEN, ""),prefs.getString(OAUTH_TOKEN_SECRET, ""));
        return authToken;
    }

    public void store(AuthToken authToken) {
        Editor editor = prefs.edit();
        editor.putString(OAUTH_TOKEN, authToken.getAuthToken());
        editor.putString(OAUTH_TOKEN_SECRET, authToken.getAuthTokenSecret());
        editor.commit();
    }

    public void clear() {
        Editor editor = prefs.edit();
        editor.remove(OAUTH_TOKEN);
        editor.remove(OAUTH_TOKEN_SECRET);
        editor.commit();
    }
}
