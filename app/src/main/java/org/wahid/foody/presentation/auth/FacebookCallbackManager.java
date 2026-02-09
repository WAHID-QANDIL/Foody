package org.wahid.foody.presentation.auth;

import com.facebook.CallbackManager;

public abstract class FacebookCallbackManager {
    private static CallbackManager callbackManager;

    public static CallbackManager manager(){
        if (callbackManager == null){
            callbackManager = CallbackManager.Factory.create();
        }
        return callbackManager;

    }
}