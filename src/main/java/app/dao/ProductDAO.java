package app.dao;

import app.db.DBUtil;
import app.model.Product;
import app.util.ProductFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    private Map<String, String> loadAttributesForProduct(int productId) {
        Map<String, String> attributes = new HashMap<>();

        String sql = "SELECT key, value FROM product_attributes WHERE product_id = ?";

        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                attributes.put(rs.getString("key"), rs.getString("value"));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // or use AlertUtil / logger
        }

        return attributes;
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM product";

        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                products.add(ProductFactory.fromResultSet(rs, loadAttributesForProduct(id)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public Product findBySku(String sku) {
        String sql = "SELECT * FROM product WHERE sku = ?";

        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sku);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("sku"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getInt("price"),
                        rs.getInt("quantity"),
                        rs.getString("supplier")
                );

                p.setAttributes(loadAttributesForProduct(p.getId()));

                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace(); // TODO
        }

        return null;
    }

    public Product findById(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("sku"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getInt("price"),
                        rs.getInt("quantity"),
                        rs.getString("supplier")
                );
                // Assuming you have a method to load attributes
                Map<String, String> attributes = loadAttributesForProduct(id);
                product.setAttributes(attributes);
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // TODO
        }
        return null;
    }

    public boolean insert(Product product) {
        String sql = "INSERT INTO product (sku, name, brand, price, quantity, supplier) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getBrand());
            stmt.setInt(4, product.getPrice());
            stmt.setInt(5, product.getQuantity());
            stmt.setString(6, product.getSupplier());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int productId = generatedKeys.getInt(1);

                    String attrSQL = "INSERT INTO product_attributes (product_id, key, value) VALUES (?, ?, ?)";
                    try (PreparedStatement attrStmt = conn.prepareStatement(attrSQL)) {
                        for (Map.Entry<String, String> entry : product.getAttributes().entrySet()) {
                            attrStmt.setInt(1, productId);
                            attrStmt.setString(2, entry.getKey());
                            attrStmt.setString(3, entry.getValue());
                            attrStmt.addBatch();
                        }
                        attrStmt.executeBatch();
                    }
                } else {
                    throw new SQLException("Inserting product failed, no ID obtained.");
                }
            }

            return true;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("❌ SKU already exists: " + product.getSku());
            } else {
                e.printStackTrace(); // TODO
            }
            return false;
        }
    }

    // TODO: Split into field specific slq update statements
    public boolean update(Product product) {
        String sql = "UPDATE product SET name = ?, brand = ?, price = ?, quantity = ?, supplier = ? WHERE id = ?";
        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getBrand());
            stmt.setInt(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setString(5, product.getSupplier());
            stmt.setInt(6, product.getId());

            int rowsAffected = stmt.executeUpdate();
            updateAttributes(product.getId(), product.getAttributes());
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // TODO
        }
        return false;
    }

    private void updateAttributes(int productId, Map<String, String> attributes) {
        String deleteSQL = "DELETE FROM product_attributes WHERE product_id = ?";
        String insertSQL = "INSERT INTO product_attributes (product_id, key, value) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
            deleteStmt.setInt(1, productId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO
        }

        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                insertStmt.setInt(1, productId);
                insertStmt.setString(2, entry.getKey());
                insertStmt.setString(3, entry.getValue());
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // TODO
            return false;
        }
    }

    public List<Product> searchByKeyword(String keyword) {
        List<Product> results = new ArrayList<>();

        String sql = "SELECT * FROM product WHERE LOWER(name) LIKE ? OR LOWER(brand) LIKE ? OR LOWER(sku) LIKE ? OR LOWER(supplier) LIKE ?";
        try (Connection conn = DBUtil.getUserConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String likeQuery = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, likeQuery);
            stmt.setString(2, likeQuery);
            stmt.setString(3, likeQuery);
            stmt.setString(4, likeQuery);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Product product = ProductFactory.fromResultSet(rs, loadAttributesForProduct(id));
                results.add(product);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(); // TODO
        }
        return results;
    }
}
