package org.wahid.foody.data.remote.user_auth.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.wahid.foody.data.remote.user_auth.UserCredentials;

import java.util.Objects;

public abstract class FirebaseUserAuthenticator implements OnAuthenticatedCallBack {
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static void loginWithEmailPassword(String email, String password, OnAuthenticatedCallBack callBack){

              firebaseAuth
              .signInWithEmailAndPassword(email,password)
              .addOnCompleteListener(task -> {
                  if (task.isSuccessful()) {
                      FirebaseUser user = firebaseAuth.getCurrentUser();
                      callBack.onSuccess(extractCredentials(user));
                  }
              }).addOnFailureListener(callBack::onFail);
    }

    public static void registerANewUser(String username, String password, OnAuthenticatedCallBack callBack){
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                callBack.onSuccess(extractCredentials(user));
            }
        }).addOnFailureListener(callBack::onFail);


    }

    private static UserCredentials extractCredentials(FirebaseUser user){
        return new UserCredentials(
                user != null ? user.getDisplayName() : "UnKnown",
                user != null ? user.getEmail() : "UnKnown",
                Objects.requireNonNull(user).getPhotoUrl(),
                user.getUid());
    }


}
