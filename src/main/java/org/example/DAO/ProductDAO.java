package org.example.DAO;


import org.example.model.Product;

import java.util.List;

public interface ProductDAO {
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(int id);
    boolean isProductNameUnique(String productName);
    Product getProduct(int id);
    List<Product> getAllProducts();
    List<Product> searchProducts(String name);
}
