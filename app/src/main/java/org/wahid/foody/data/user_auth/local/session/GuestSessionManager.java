package org.wahid.foody.data.user_auth.local.session;

public class GuestSessionManager {

    private static GuestSessionManager instance;
    private boolean isGuestMode = false;

    private GuestSessionManager() {
    }

    public static synchronized GuestSessionManager getInstance() {
        if (instance == null) {
            instance = new GuestSessionManager();
        }
        return instance;
    }


    public void setGuestMode(boolean isGuest) {
        this.isGuestMode = isGuest;
    }


    public boolean isGuestMode() {
        return isGuestMode;
    }

    public void clearSession() {
        this.isGuestMode = false;
    }


    public boolean isFeatureAvailable(Feature feature) {
        if (!isGuestMode) {
            return true;
        }

        return switch (feature) {
            case FAVORITES, MEAL_PLAN, SYNC_DATA -> false;
            case BROWSE_MEALS, SEARCH, VIEW_DETAILS -> true;
        };
    }

    public enum Feature {
        FAVORITES,
        MEAL_PLAN,
        SYNC_DATA,
        BROWSE_MEALS,
        SEARCH,
        VIEW_DETAILS
    }
}

