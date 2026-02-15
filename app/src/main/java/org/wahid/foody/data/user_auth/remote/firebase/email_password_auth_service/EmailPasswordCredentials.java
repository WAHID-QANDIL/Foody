package org.wahid.foody.data.user_auth.remote.firebase.email_password_auth_service;

import org.wahid.foody.presentation.auth.AuthCredentials;

public class EmailPasswordCredentials implements AuthCredentials {

    private String email;
    private String password;

    public EmailPasswordCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
