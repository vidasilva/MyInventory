package app.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String GLOBAL_DB = "auth.db";
    private static String userDbPath = null;

    public static Connection getGlobalConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + GLOBAL_DB);
    }

    public static void setUser(String username) {
        // Create users folder if not exists
        File usersDir = new File("users");
        if (!usersDir.exists()) {
            usersDir.mkdir();
        }

        userDbPath = "users/" + username + "_inventory.db";
    }

    public static Connection getUserConnection() throws SQLException {
        if (userDbPath == null) {
            throw new IllegalStateException("User database not selected. Call setUser(username) first.");
        }
        return DriverManager.getConnection("jdbc:sqlite:" + userDbPath);
    }

    public static String getCurrentUserDb() {
        return userDbPath;
    }
}
