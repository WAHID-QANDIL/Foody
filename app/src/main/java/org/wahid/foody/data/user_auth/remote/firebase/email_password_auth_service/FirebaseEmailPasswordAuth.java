package org.wahid.foody.data.user_auth.remote.firebase.email_password_auth_service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.wahid.foody.data.user_auth.core.AuthenticationService;
import org.wahid.foody.data.user_auth.core.UserCredentials;
import org.wahid.foody.data.user_auth.remote.firebase.FirebaseClient;

import io.reactivex.rxjava3.core.Single;

public class FirebaseEmailPasswordAuth implements AuthenticationService<EmailPasswordCredentials> {

    private static final FirebaseAuth firebaseAuth = FirebaseClient.getInstance();

    private Single<UserCredentials<?>> loginWithEmailPassword(String email, String password) {
        return Single.create(emitter -> {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            emitter.onSuccess(new UserCredentials<>(user));
                        } else if (task.getException() != null) {
                            emitter.onError(task.getException());
                        } else {
                            emitter.onError(new Exception("Login failed"));
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    private Single<UserCredentials<?>> registerANewUser(String username, String password) {
        return Single.create(emitter -> {
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            emitter.onSuccess(new UserCredentials<>(user));
                        } else if (task.getException() != null) {
                            emitter.onError(task.getException());
                        } else {
                            emitter.onError(new Exception("Registration failed"));
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }


    @Override
    public Single<UserCredentials<?>> login(EmailPasswordCredentials credentials) {
        return loginWithEmailPassword(credentials.getEmail(), credentials.getPassword());
    }

    @Override
    public Single<UserCredentials<?>> register(EmailPasswordCredentials credentials) {
        return registerANewUser(credentials.getEmail(), credentials.getPassword());
    }
}