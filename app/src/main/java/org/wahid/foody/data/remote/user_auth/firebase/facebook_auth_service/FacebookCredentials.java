package org.wahid.foody.data.remote.user_auth.firebase.facebook_auth_service;

import org.wahid.foody.presentation.auth.AuthCredentials;

public class FacebookCredentials implements AuthCredentials {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public FacebookCredentials(String token) {
        this.token = token;
    }
}
