package org.wahid.foody.data.user_auth.remote.firebase.facebook_auth_service;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.wahid.foody.data.user_auth.core.UserCredentials;
import org.wahid.foody.data.user_auth.remote.firebase.FirebaseClient;

import io.reactivex.rxjava3.core.Single;

public class FirebaseAuthWithFacebook implements AuthenticationWithFacebook {

    @Override
    public Single<UserCredentials<?>> login(FacebookCredentials credentials) {
        return Single.create(emitter -> {
            FirebaseAuth firebaseAuth = FirebaseClient.getInstance();
            AuthCredential credential = FacebookAuthProvider.getCredential(credentials.getToken());
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseClient.getInstance().getCurrentUser();
                            emitter.onSuccess(new UserCredentials<>(user));
                        } else if (task.getException() != null) {
                            emitter.onError(task.getException());
                        } else {
                            emitter.onError(new Exception("Facebook authentication failed"));
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Single<UserCredentials<?>> register(FacebookCredentials credentials) {
        // For Facebook, register and login are essentially the same
        return login(credentials);
    }
}
