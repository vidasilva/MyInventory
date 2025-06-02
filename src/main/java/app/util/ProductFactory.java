package app.util;


import app.model.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ProductFactory {

    public static Product fromResultSet(ResultSet rs, Map<String, String> attributes) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("sku"),
                rs.getString("name"),
                rs.getString("brand"),
                rs.getInt("price"),
                rs.getInt("quantity"),
                rs.getString("supplier"),
                attributes
        );
    }
}
