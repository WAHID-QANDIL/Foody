package org.wahid.foody.data.remote.user_auth;

import org.wahid.foody.data.remote.user_auth.firebase.facebook_auth_service.FirebaseAuthWithFacebook;
import org.wahid.foody.data.remote.user_auth.firebase.google_auth_service.FirebaseAuthWithGoogle;
import org.wahid.foody.data.remote.user_auth.firebase.email_password_auth_service.FirebaseEmailPasswordAuth;

public class AuthenticationServiceFactory{
    public AuthenticationService getAuthenticationInstance(AuthenticationServiceType type){
        return switch (type){
            case EMAIL_PASSWORD -> new FirebaseEmailPasswordAuth();
            case GOOGLE         -> new FirebaseAuthWithGoogle();
            case FACEBOOK       -> new FirebaseAuthWithFacebook();
        };
    }
}