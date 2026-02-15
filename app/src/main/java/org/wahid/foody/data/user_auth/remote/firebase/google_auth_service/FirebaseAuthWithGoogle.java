package org.wahid.foody.data.user_auth.remote.firebase.google_auth_service;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.wahid.foody.data.user_auth.core.UserCredentials;
import org.wahid.foody.data.user_auth.remote.firebase.FirebaseClient;

import io.reactivex.rxjava3.core.Single;

public class FirebaseAuthWithGoogle implements AuthenticationWithGoogle {

    private static final String TAG = "GoogleFragment";

    @Override
    public Single<UserCredentials<?>> login(GoogleCredentials credentials) {
        return firebaseAuthWithGoogle(credentials.getToken());
    }

    @Override
    public Single<UserCredentials<?>> register(GoogleCredentials credentials) {
        // For Google, register and login are essentially the same
        return login(credentials);
    }

    private Single<UserCredentials<?>> firebaseAuthWithGoogle(String idToken) {
        return Single.create(emitter -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            FirebaseAuth firebaseAuth = FirebaseClient.getInstance();
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseClient.getInstance().getCurrentUser();
                            emitter.onSuccess(new UserCredentials<>(user));
                        } else if (task.getException() != null) {
                            emitter.onError(task.getException());
                        } else {
                            emitter.onError(new Exception("Google authentication failed"));
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}