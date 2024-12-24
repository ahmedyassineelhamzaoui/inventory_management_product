package org.example.DAO;

import org.example.model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAOImpl implements ProductDAO {
    private Connection conn;
    private static final String URL = "jdbc:mysql://localhost:3306/inventory";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public ProductDAOImpl() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public boolean addProduct(Product product) {
        String query = "INSERT INTO products (name, price, category, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getCategory());
            pstmt.setInt(4, product.getStock());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET name=?, price=?, category=?, stock=? WHERE id=?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getCategory());
            pstmt.setInt(4, product.getStock());
            pstmt.setInt(5, product.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int id) {
        String query = "DELETE FROM products WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public Product getProduct(int id) {
        String query = "SELECT * FROM products WHERE id=?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getString("category"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        String query = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getInt("stock")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return products;

    }

    @Override
    public List<Product> searchProducts(String searchName) {
        String query = "SELECT * FROM products WHERE name LIKE ? OR category LIKE ?";

        List<Product> products = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + searchName + "%");
            pstmt.setString(2, "%" + searchName + "%");

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getString("category"),
                            rs.getInt("stock")
                    );
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        return products;
    }
    public boolean isProductNameUnique(String productName) {
        String query = "SELECT COUNT(*) FROM products WHERE name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, productName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        return false;
    }


}
