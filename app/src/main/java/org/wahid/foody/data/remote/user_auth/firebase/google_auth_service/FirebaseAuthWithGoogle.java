package org.wahid.foody.data.remote.user_auth.firebase.google_auth_service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import org.wahid.foody.data.remote.user_auth.UserCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.FirebaseClient;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;


public class FirebaseAuthWithGoogle implements AuthenticationWithGoogle {

    private static final String TAG = "GoogleFragment";

    @Override
    public void login(GoogleCredentials credentials, OnAuthenticatedCallBack callBack) {

        firebaseAuthWithGoogle(credentials.getToken(),callBack);
    }

    @Override
    public void register(GoogleCredentials credentials, OnAuthenticatedCallBack callBack) {
        // For Google, register and login are essentially the same
        login(credentials, callBack);
    }

    private void firebaseAuthWithGoogle(String idToken, OnAuthenticatedCallBack callBack) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth firebaseAuth = FirebaseClient.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseClient.getInstance().getCurrentUser();
                            callBack.onSuccess(new UserCredentials<FirebaseUser>(user));
                        }
                    }
                }).addOnFailureListener(callBack::onFail);
    }
}