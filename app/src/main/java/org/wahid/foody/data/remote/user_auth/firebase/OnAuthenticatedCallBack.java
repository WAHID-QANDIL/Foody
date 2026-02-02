package org.wahid.foody.data.remote.user_auth.firebase;

import org.wahid.foody.data.remote.user_auth.UserCredentials;

public interface OnAuthenticatedCallBack {
    void onSuccess(UserCredentials credentials);
    void onFail(Throwable throwable);
}
