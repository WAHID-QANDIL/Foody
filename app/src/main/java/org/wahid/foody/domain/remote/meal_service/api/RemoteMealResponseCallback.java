package org.wahid.foody.domain.remote.meal_service.api;

public interface RemoteMealResponseCallback<T,E extends Throwable> {
    void onSuccess(T response);
    void onFail(E exception);
}