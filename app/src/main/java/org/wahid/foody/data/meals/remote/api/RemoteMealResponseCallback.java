package org.wahid.foody.data.meals.remote.api;

public interface RemoteMealResponseCallback<T,E extends Throwable> {
    void onSuccess(T response);
    void onFail(E exception);
}