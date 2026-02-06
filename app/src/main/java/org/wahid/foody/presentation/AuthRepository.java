package org.wahid.foody.presentation;

import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;

public interface AuthRepository<C>{

    void login(C credentials, OnAuthenticatedCallBack callBack);
    void register(C credentials, OnAuthenticatedCallBack callBack);
}