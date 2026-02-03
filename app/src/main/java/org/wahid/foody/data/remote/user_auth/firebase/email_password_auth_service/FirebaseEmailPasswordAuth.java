package org.wahid.foody.data.remote.user_auth.firebase.email_password_auth_service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.wahid.foody.data.remote.user_auth.AuthenticationService;
import org.wahid.foody.data.remote.user_auth.UserCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;

import java.util.Objects;

public class FirebaseEmailPasswordAuth implements AuthenticationService<EmailPasswordCredentials> {

    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private void loginWithEmailPassword(String email, String password, OnAuthenticatedCallBack callBack) {
        firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        callBack.onSuccess(extractCredentials(user));
                    }
                }).addOnFailureListener(callBack::onFail);
    }

    private void registerANewUser(String username, String password, OnAuthenticatedCallBack callBack) {
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                callBack.onSuccess(extractCredentials(user));
            }
        }).addOnFailureListener(callBack::onFail);
    }

    private UserCredentials extractCredentials(FirebaseUser user) {
        return new UserCredentials(
                user != null ? user.getDisplayName() : "UnKnown",
                user != null ? user.getEmail() : "UnKnown",
                Objects.requireNonNull(user).getPhotoUrl(),
                user.getUid());
    }

    @Override
    public void login(EmailPasswordCredentials credentials, OnAuthenticatedCallBack callBack) {
        loginWithEmailPassword(credentials.getEmail(), credentials.getPassword(), callBack);
    }

    @Override
    public void register(EmailPasswordCredentials credentials, OnAuthenticatedCallBack callBack) {
        registerANewUser(credentials.getEmail(), credentials.getPassword(), callBack);
    }
}
