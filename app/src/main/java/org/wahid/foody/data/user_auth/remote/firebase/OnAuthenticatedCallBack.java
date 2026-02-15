package org.wahid.foody.data.user_auth.remote.firebase;

import org.wahid.foody.data.user_auth.core.UserCredentials;

public interface OnAuthenticatedCallBack {
    void onSuccess(UserCredentials credentials);
    void onFail(Throwable throwable);
}