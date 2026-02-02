package org.wahid.foody.data.remote.meal_service.api;

public interface RemoteMealResponseCallback<T,E extends Throwable> {
    void onSuccess(T response);
    void onFail(E exception);
}