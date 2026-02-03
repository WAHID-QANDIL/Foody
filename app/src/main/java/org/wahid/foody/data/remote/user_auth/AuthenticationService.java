package org.wahid.foody.data.remote.user_auth;

import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;


// Base for all authentication services, this could be recognized as a marker interface
public interface AuthenticationService<C>{
    void login(C credentials, OnAuthenticatedCallBack callBack);
    void register(C credentials, OnAuthenticatedCallBack callBack);
}