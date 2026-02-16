package org.wahid.foody.data.user_auth.remote.guest;

import org.wahid.foody.data.user_auth.core.AuthenticationService;
import org.wahid.foody.data.user_auth.core.UserCredentials;
import org.wahid.foody.data.user_auth.remote.firebase.FirebaseClient;
import org.wahid.foody.data.user_auth.local.session.GuestSessionManager;
import io.reactivex.rxjava3.core.Single;

public class GuestAuthService implements AuthenticationService<GuestCredentials> {

    private static final String GUEST_USER_ID = "guest_user";
    private static final String GUEST_USERNAME = "Guest";
    private static final String GUEST_EMAIL = "guest@foody.app";

    @Override
    public Single<UserCredentials<?>> login(GuestCredentials credentials) {
        return Single.create(emitter -> {
            try {
                FirebaseClient.getInstance().signOut();

                GuestSessionManager.getInstance().setGuestMode(true);

                GuestUser guestUser = new GuestUser(GUEST_USER_ID, GUEST_USERNAME, GUEST_EMAIL);
                emitter.onSuccess(new UserCredentials<>(guestUser));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<UserCredentials<?>> register(GuestCredentials credentials) {
        return login(credentials);
    }
    public static class GuestUser {
        private final String id;
        private final String name;
        private final String email;

        public GuestUser(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}