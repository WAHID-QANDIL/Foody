package org.wahid.foody.data.remote.user_auth;

public class UserCredentials<T>{
    private T credentials;

    public UserCredentials(T credentials) {
        this.credentials = credentials;
    }

    public T getCredentials() {
        return credentials;
    }

    public void setCredentials(T credentials) {
        this.credentials = credentials;
    }
}