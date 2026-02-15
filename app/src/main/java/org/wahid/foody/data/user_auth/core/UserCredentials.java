package org.wahid.foody.data.user_auth.core;

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