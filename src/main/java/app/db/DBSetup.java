package app.db;

import java.sql.Connection;
import java.sql.Statement;

public class DBSetup {

    public static void initGlobalDatabase() {
        try (Connection conn = DBUtil.getGlobalConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS user (\n"
                    + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "    username TEXT UNIQUE NOT NULL,\n"
                    + "    password_hash TEXT NOT NULL,\n"
                    + "    created_at TEXT DEFAULT CURRENT_TIMESTAMP,\n"
                    + "    role TEXT DEFAULT 'user'\n"
                    + ");");
            System.out.println("✅ Global user database initialized.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // !!Only for logged in user!!
    public static void initUserDatabase() {
        try (Connection conn = DBUtil.getUserConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS product (\n"
                    + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "    sku TEXT UNIQUE,\n"
                    + "    name TEXT NOT NULL,\n"
                    + "    brand TEXT,\n"
                    + "    price INTEGER,\n"
                    + "    quantity INTEGER,\n"
                    + "    supplier TEXT\n"
                    + ");"
            );

            // Attribute key/value pairs (1:N with product)
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS product_attributes (\n"
                    + "    product_id INTEGER NOT NULL,\n"
                    + "    key TEXT NOT NULL,\n"
                    + "    value TEXT,\n"
                    + "    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE\n"
                    + ");"
            );

            stmt.execute("    CREATE TABLE IF NOT EXISTS customer (\n"
                    + "        id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "        name TEXT,\n"
                    + "        email TEXT,\n"
                    + "        phone TEXT\n"
                    + "    );\n");

            stmt.execute("    CREATE TABLE IF NOT EXISTS sale (\n"
                    + "        id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "        date TEXT,\n"
                    + "        customer_id INTEGER,\n"
                    + "        total INTEGER,\n"
                    + "        FOREIGN KEY (customer_id) REFERENCES customer(id)\n"
                    + "    );\n");

            stmt.execute("    CREATE TABLE IF NOT EXISTS sale_item (\n"
                    + "        id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "        sale_id INTEGER,\n"
                    + "        product_id INTEGER,\n"
                    + "        quantity INTEGER,\n"
                    + "        price INTEGER,\n"
                    + "        FOREIGN KEY (sale_id) REFERENCES sale(id),\n"
                    + "        FOREIGN KEY (product_id) REFERENCES product(id)\n"
                    + "    );\n");

            System.out.println("✅ User database initialized: " + DBUtil.getCurrentUserDb());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
