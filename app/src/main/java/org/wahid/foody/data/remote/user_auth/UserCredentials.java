package org.wahid.foody.data.remote.user_auth;

public record UserCredentials(
        String username,
        String userEmail,
        android.net.Uri photo_url,
        String userId
) { }