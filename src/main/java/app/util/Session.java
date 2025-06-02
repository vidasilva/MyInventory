package app.util;

import app.model.User;

public class Session {

    private static User currentUser;

    public static void set(User user) {
        currentUser = user;
    }

    public static User get() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
