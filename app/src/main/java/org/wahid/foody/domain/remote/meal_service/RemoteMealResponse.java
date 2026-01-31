package org.wahid.foody.domain.remote.meal_service;

public interface RemoteMealResponse<T,E extends Throwable> {
    void onSuccess(T response);
    void onFail(E exception);
}