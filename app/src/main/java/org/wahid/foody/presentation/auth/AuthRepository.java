package org.wahid.foody.presentation.auth;

import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;

public interface AuthRepository {
    void login(String username, String password, OnAuthenticatedCallBack callBack);
    void register(String username, String password, OnAuthenticatedCallBack callBack);
}