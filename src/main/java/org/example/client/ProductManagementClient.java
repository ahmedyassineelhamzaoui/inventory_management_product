package org.example.client;

import org.example.filter.NumberDocFilter;
import org.example.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class ProductManagementClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private boolean isConnected = false;
    private DefaultTableModel tableModel;


    public ProductManagementClient() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createAndShowGUI() {
        ProductManagementClient client = new ProductManagementClient();

        // Crée une fenêtre avec un titre
        JFrame frame = new JFrame("Gestion des Produits");
        // Définir ce qui ce passe quand l'utilisateur clique sur le bouton fermer (X)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Définir la taille initial de la fenetre
        frame.setSize(1000, 600);
        // Empeché l'utilisateur de redimensioner la fenetre 
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Main panel to hold all sections
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ============ SECTION 1: Add Product Form ============
        JPanel addProductPanel = new JPanel(new BorderLayout());
        addProductPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un Produit"));

        // Fields panel
        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Field for Nom
        JTextField nameField = new JTextField(10);

        // Field for Prix
        JTextField priceField = new JTextField(10);
        ((AbstractDocument) priceField.getDocument()).setDocumentFilter(new NumberDocFilter(true)); // Allow decimal point

        // Field for Catégorie
        JTextField categoryField = new JTextField(10);

        // Field for stock
        JTextField stockField = new JTextField(15);
        ((AbstractDocument) stockField.getDocument()).setDocumentFilter(new NumberDocFilter(false)); // No decimal point






        fieldsPanel.add(new JLabel("Nom :"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Prix:"));
        fieldsPanel.add(priceField);
        fieldsPanel.add(new JLabel("Catégorie :"));
        fieldsPanel.add(categoryField);
        fieldsPanel.add(new JLabel("Stock :"));
        fieldsPanel.add(stockField);

        // Add button panel (right-aligned)
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Ajouter");
        addButtonPanel.add(addButton);

        addProductPanel.add(fieldsPanel, BorderLayout.CENTER);
        addProductPanel.add(addButtonPanel, BorderLayout.EAST);

        // ============ SECTION 2: Search Panel ============
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Rechercher"));

        searchPanel.add(new JLabel("Chercher un produit :"));
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        // ============ SECTION 3: Action Buttons ============
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Actualiser"); // New refresh button

        actionPanel.add(refreshButton);  // Add refresh button
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        // ============ Table Panel ============
        // Create a custom table model that makes all cells non-editable
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Prix", "Catégorie", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        JTable productTable = new JTable(tableModel);

        // Additional table settings
        productTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
        productTable.getTableHeader().setResizingAllowed(false);   // Prevent column resizing


        client.tableModel = tableModel;

        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ============ Add padding and spacing ============
        addProductPanel.setBorder(BorderFactory.createCompoundBorder(
                addProductPanel.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                searchPanel.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        actionPanel.setBorder(BorderFactory.createCompoundBorder(
                actionPanel.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // ============ Assembly ============
        mainPanel.add(addProductPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(actionPanel);

        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // ============ Styling ============
        Color sectionBackground = new Color(245, 245, 245);
        addProductPanel.setBackground(sectionBackground);
        searchPanel.setBackground(sectionBackground);
        actionPanel.setBackground(sectionBackground);

        // Button styling
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(60, 179, 113));
        searchButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(255, 165, 0));
        updateButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(100, 149, 237));  // Cornflower blue
        refreshButton.setForeground(Color.WHITE);


        frame.setVisible(true);


        // add button action
        addButton.addActionListener(e -> {
            if (!client.isConnected) {
                JOptionPane.showMessageDialog(frame,
                        "Impossible de se connecter au serveur. Veuillez vérifier que le serveur est démarré.",
                        "Erreur de connexion",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String name = nameField.getText().trim();
                String category = categoryField.getText().trim();
                String priceStr = priceField.getText().trim();
                String stockStr = stockField.getText().trim();

                if (!client.isNameUnique(frame,name)) {
                    // Show a message if the name is already taken
                    JOptionPane.showMessageDialog(frame,
                            "Le nom du produit doit être unique. Ce nom est déjà utilisé.",
                            "Erreur de duplication",
                            JOptionPane.ERROR_MESSAGE);
                    return;  // Exit the method if the name is not unique
                }
                double price ;
                int stock ;
                if (name.isEmpty() || priceStr.isEmpty() || category.isEmpty() || stockStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method if validation fails
                }
                try{
                    price = Double.parseDouble(priceStr);
                    stock = Integer.parseInt(stockStr);
                }catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(frame,"Price & Stock must be a valid numbers .","Input Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Product product = new Product(name, price, category, stock);

                client.output.writeObject("ADD");
                client.output.writeObject(product);
                boolean success = client.input.readBoolean();

                if (success) {
                    JOptionPane.showMessageDialog(frame, "Product added successfully!");
                    nameField.setText("");
                    categoryField.setText("");
                    priceField.setText("");
                    stockField.setText("");
                    // Refresh table
                    client.refreshTableData(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Error adding product!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // refresh button action
        refreshButton.addActionListener(e -> {
            client.refreshTableData(frame);
        });

        // delete button action
        deleteButton.addActionListener(e -> {
            // Vérifier si un produit est sélectionné
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame,
                        "Veuillez sélectionner un produit à supprimer",
                        "Aucune sélection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obtenir l'ID du produit sélectionné
            int productId = (int) tableModel.getValueAt(selectedRow, 0); // 0 est l'index de la colonne ID

            // Demander confirmation
            int confirmation = JOptionPane.showConfirmDialog(frame,
                    "Êtes-vous sûr de vouloir supprimer ce produit ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    // Envoyer la commande de suppression au serveur
                    client.output.writeObject("DELETE");
                    client.output.writeObject(productId);

                    // Lire la réponse du serveur
                    boolean deleted = client.input.readBoolean();

                    if (deleted) {
                        // Supprimer la ligne du tableau
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(frame,
                                "Produit supprimé avec succès",
                                "Succès",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Actualiser le tableau
                        client.refreshTableData(frame);
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Erreur lors de la suppression du produit",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Erreur de communication avec le serveur: " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // update button action
        updateButton.addActionListener(e -> {
            // Vérifier si un produit est sélectionné
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame,
                        "Veuillez sélectionner un produit à modifier",
                        "Aucune sélection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }


            // Récupérer les données du produit sélectionné
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String currentName = (String) tableModel.getValueAt(selectedRow, 1);
            double currentPrice = (double) tableModel.getValueAt(selectedRow, 2);
            String currentCategory = (String) tableModel.getValueAt(selectedRow, 3);
            int currentStock = (int) tableModel.getValueAt(selectedRow, 4);

            // Créer un JDialog pour la modification
            JDialog updateDialog = new JDialog(frame, "Modifier le produit", true);
            updateDialog.setLayout(new BorderLayout());
            updateDialog.setSize(400, 300);
            updateDialog.setLocationRelativeTo(frame);

            // Panel pour les champs
            JPanel fieldsPanelToEdit = new JPanel(new GridLayout(5, 2, 10, 10));
            fieldsPanelToEdit.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Créer les champs avec les valeurs actuelles
            JTextField nameEditField = new JTextField(currentName);
            JTextField priceEditField = new JTextField(String.valueOf(currentPrice));
            JTextField categoryEditField = new JTextField(currentCategory);
            JTextField stockEditField = new JTextField(String.valueOf(currentStock));

            // Ajouter les champs au panel
            fieldsPanelToEdit.add(new JLabel("Nom:"));
            fieldsPanelToEdit.add(nameEditField);
            fieldsPanelToEdit.add(new JLabel("Prix:"));
            fieldsPanelToEdit.add(priceEditField);
            fieldsPanelToEdit.add(new JLabel("Catégorie:"));
            fieldsPanelToEdit.add(categoryEditField);
            fieldsPanelToEdit.add(new JLabel("Stock:"));
            fieldsPanelToEdit.add(stockEditField);

            // Panel pour les boutons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Enregistrer");
            JButton cancelButton = new JButton("Annuler");


            // Action du bouton Enregistrer
            saveButton.addActionListener(saveEvent -> {
                if(!nameEditField.getText().trim().equals(currentName)){
                    if (!client.isNameUnique(frame,nameEditField.getText())) {
                        // Show a message if the name is already taken
                        JOptionPane.showMessageDialog(updateDialog,
                                "Le nom du produit doit être unique. Ce nom est déjà utilisé.",
                                "Erreur de duplication",
                                JOptionPane.ERROR_MESSAGE);
                        return;  // Exit the method if the name is not unique
                    }
                }

                try {
                    // Créer un nouveau produit avec les valeurs modifiées
                    Product updatedProduct = new Product(
                            id,
                            nameEditField.getText(),
                            Double.parseDouble(priceEditField.getText()),
                            categoryEditField.getText(),
                            Integer.parseInt(stockEditField.getText())
                    );

                    // Envoyer la mise à jour au serveur
                    client.output.writeObject("UPDATE");
                    client.output.writeObject(updatedProduct);

                    // Lire la réponse du serveur
                    boolean success = client.input.readBoolean();

                    if (success) {
                        // Mettre à jour le tableau
                        client.refreshTableData(frame);

                        JOptionPane.showMessageDialog(updateDialog,
                                "Produit mis à jour avec succès",
                                "Succès",
                                JOptionPane.INFORMATION_MESSAGE);
                        updateDialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(updateDialog,
                                "Erreur lors de la mise à jour du produit",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(updateDialog,
                            "Veuillez entrer des valeurs numériques valides pour le prix et le stock",
                            "Erreur de saisie",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(updateDialog,
                            "Erreur de communication avec le serveur: " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            // Action du bouton Annuler
            cancelButton.addActionListener(cancelEvent -> updateDialog.dispose());

            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            // Assembler le dialog
            updateDialog.add(fieldsPanelToEdit, BorderLayout.CENTER);
            updateDialog.add(buttonPanel, BorderLayout.SOUTH);

            // Afficher le dialog
            updateDialog.setVisible(true);

        });

        // search product
        searchButton.addActionListener(e->{


            try {
                String searchByName =  searchField.getText();

                client.output.writeObject("SEARCH");
                client.output.writeObject(searchByName);

                client.output.flush();

                @SuppressWarnings("unchecked")
                java.util.List<Product> products = (java.util.List<Product>) client.input.readObject();
                tableModel.setRowCount(0);
                // Add products to table
                if (products.isEmpty()) {
                    // If no products found, display a row with a message
                    tableModel.addRow(new Object[]{
                            "",
                            "",
                            "Aucun produit trouvé",
                            "",
                            ""
                    });
                } else {
                    for (Product product : products) {
                        tableModel.addRow(new Object[]{
                                product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getCategory(),
                                product.getStock()
                        });
                    }
                }



            }catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Erreur lors de la recherche des produits: " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        client.refreshTableData(frame);
    }
    private void refreshTableData(JFrame frame) {

        try {
            // Send request for all products
            output.writeObject("GET_ALL");
            output.flush();

            // Read the respons
            @SuppressWarnings("unchecked")
            java.util.List<Product> products = (java.util.List<Product>) input.readObject();
            // Clear existing table data
            tableModel.setRowCount(0);
            // Add products to table
            if (products.isEmpty()) {
                // If no products found, display a row with a message
                tableModel.addRow(new Object[]{
                        "",
                        "",
                        "Aucun produit trouvé",
                        "",
                        ""
                });
            }else{
                for (Product product : products) {
                    tableModel.addRow(new Object[]{
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getCategory(),
                            product.getStock()
                    });
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(frame,
                    "Erreur lors de la récupération des produits: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            isConnected = false;
        }
    }
    private boolean isNameUnique(JFrame frame,String name) {
        try {
            // Send request to check if name is unique
            output.writeObject("CHECK_NAME_UNIQUE");  // Command to check name uniqueness
            output.writeObject(name);  // Send the name to check
            output.flush();

            // Read the response from the server
            boolean isUnique = input.readBoolean();
            return isUnique;  // Return the result

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                    "Erreur lors de la vérification du nom: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


}
