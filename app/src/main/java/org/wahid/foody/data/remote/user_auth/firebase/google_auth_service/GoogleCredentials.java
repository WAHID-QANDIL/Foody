package org.wahid.foody.data.remote.user_auth.firebase.google_auth_service;

import org.wahid.foody.presentation.auth.AuthCredentials;

public class GoogleCredentials implements AuthCredentials {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public GoogleCredentials(String token) {
        this.token = token;
    }
}