package org.example.server;

import org.example.model.Product;
import org.example.DAO.ProductDAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ProductDAO productDAO;
    private Logger logger;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientHandler(Socket socket, ProductDAO dao, Logger logger) {
        this.clientSocket = socket;
        this.productDAO = dao;
        this.logger = logger;
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.severe("Error creating streams: " + e.getMessage());
        }
    }
    @Override
    public void run() {
        try {
            while (true) {
                String command = (String) input.readObject();
                switch (command) {
                    case "ADD":
                        Product product = (Product) input.readObject();
                        boolean added = productDAO.addProduct(product);
                        output.writeBoolean(added);
                        logger.info("Product added: " + product.getName());
                        break;
                    case "UPDATE":
                        Product updatedProduct = (Product) input.readObject();
                        boolean updated = productDAO.updateProduct(updatedProduct);
                        output.writeBoolean(updated);
                        logger.info("Product updated: " + updatedProduct.getName());
                        break;

                    case "DELETE":
                        int idToDelete = (Integer) input.readObject();
                        boolean deleted = productDAO.deleteProduct(idToDelete);
                        logger.info(String.valueOf(deleted));
                        logger.info("Product deleted with ID: " + idToDelete);
                        output.writeBoolean(deleted);
                        break;

                    case "GET":
                        int idToGet = (Integer) input.readObject();
                        Product foundProduct = productDAO.getProduct(idToGet);
                        output.writeObject(foundProduct);
                        logger.info("Product retrieved with ID: " + idToGet);
                        break;

                    case "GET_ALL":
                        List<Product> products = productDAO.getAllProducts();
                        output.writeObject(products);
                        logger.info("Sent all products to client");
                        break;

                    case "SEARCH":
                        String searchByName = (String) input.readObject();
                        List<Product> searchResults = productDAO.searchProducts(searchByName);
                        output.writeObject(searchResults);
                        logger.info("Search performed ");
                        break;
                    case "CHECK_NAME_UNIQUE":
                        String productNameToCheck = (String) input.readObject();
                        boolean isUniqueName = productDAO.isProductNameUnique(productNameToCheck);
                        output.writeBoolean(isUniqueName);
                        logger.info("Checked if product name is unique: " + productNameToCheck + " - Result: " + isUniqueName);
                        break;

                    default:
                        logger.warning("Unknown command received: " + command);
                        output.writeBoolean(false);
                        break;
                }
                output.flush();
            }
        } catch (Exception e) {
            logger.severe("Error handling client: " + e.getMessage());
        }
    }
}
