package org.wahid.foody.data.remote.user_auth.firebase;

import com.google.firebase.auth.FirebaseAuth;

public abstract class FirebaseClient {

    public static FirebaseAuth getInstance(){
        return FirebaseAuth.getInstance();
    }
}
