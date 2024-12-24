package org.example.server;

import org.example.DAO.ProductDAO;
import org.example.DAO.ProductDAOImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class InventoryServer {
    private static final int PORT = 5000;
    private ServerSocket serverSocket;
    private ProductDAO productDAO;
    private static final Logger logger = Logger.getLogger(InventoryServer.class.getName());

    public InventoryServer() {
        this.productDAO = new ProductDAOImpl();
        setupLogger();
    }

    private void setupLogger() {
        try {
            FileHandler fh = new FileHandler("inventory_logs.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, productDAO, logger);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            logger.severe("Server error: " + e.getMessage());
        }
    }
}
