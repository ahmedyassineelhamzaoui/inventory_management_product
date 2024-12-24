package org.example;

import org.example.client.ProductManagementClient;
import org.example.server.InventoryServer;

import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        // Start the server in a new thread
        new Thread(() -> {
            InventoryServer server = new InventoryServer();
            server.start();
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start the client in the main thread
        SwingUtilities.invokeLater(() -> {
            try {
                ProductManagementClient.createAndShowGUI();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Impossible de se connecter au serveur",
                        "Erreur de connexion",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

