package org.wahid.foody.data.remote.user_auth.firebase.email_password_auth_service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.wahid.foody.data.remote.user_auth.AuthenticationService;
import org.wahid.foody.data.remote.user_auth.UserCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.FirebaseClient;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;

import java.util.Objects;

public class FirebaseEmailPasswordAuth implements AuthenticationService<EmailPasswordCredentials> {

    private static final FirebaseAuth firebaseAuth = FirebaseClient.getInstance();

    private void loginWithEmailPassword(String email, String password, OnAuthenticatedCallBack callBack) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        callBack.onSuccess(new UserCredentials<FirebaseUser>(user));
                    }
                }).addOnFailureListener(callBack::onFail);
    }

    private void registerANewUser(String username, String password, OnAuthenticatedCallBack callBack) {
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                callBack.onSuccess(new UserCredentials<FirebaseUser>(user));
            }
        }).addOnFailureListener(callBack::onFail);
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