package org.wahid.foody.data.user_auth.core;

import org.wahid.foody.data.user_auth.remote.firebase.facebook_auth_service.FirebaseAuthWithFacebook;
import org.wahid.foody.data.user_auth.remote.firebase.google_auth_service.FirebaseAuthWithGoogle;
import org.wahid.foody.data.user_auth.remote.firebase.email_password_auth_service.FirebaseEmailPasswordAuth;
import org.wahid.foody.data.user_auth.remote.guest.GuestAuthService;

public class AuthenticationServiceFactory{
    public AuthenticationService getAuthenticationInstance(AuthenticationServiceType type){
        return switch (type){
            case EMAIL_PASSWORD -> new FirebaseEmailPasswordAuth();
            case GOOGLE         -> new FirebaseAuthWithGoogle();
            case FACEBOOK       -> new FirebaseAuthWithFacebook();
            case GUEST          -> new GuestAuthService();
        };
    }
}