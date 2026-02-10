package org.wahid.foody.data.remote.user_auth.firebase.facebook_auth_service;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.wahid.foody.data.remote.user_auth.UserCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.FirebaseClient;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;
public class FirebaseAuthWithFacebook implements AuthenticationWithFacebook {

    @Override
    public void login(FacebookCredentials credentials, OnAuthenticatedCallBack callBack) {
        FirebaseAuth firebaseAuth = FirebaseClient.getInstance();
        AuthCredential credential = FacebookAuthProvider.getCredential(credentials.getToken());
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

    @Override
    public void register(FacebookCredentials credentials, OnAuthenticatedCallBack callBack) {
        // For Facebook, register and login are essentially the same
        login(credentials, callBack);
    }
}
