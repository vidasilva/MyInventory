package app.controller;

import app.dao.ProductDAO;
import app.model.Product;
import app.service.ProductService;
import app.util.AlertUtil;
import app.util.PriceUtil;
import app.util.SceneManager;
import app.util.TextFieldUtil;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditProductController implements Initializable {

    @FXML
    private VBox editForm;
    @FXML
    private TextField nameField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField supplierField;
    @FXML
    private Button saveButton;
    @FXML
    private Button addAttributeButton;
    @FXML
    private HBox attributeContainer;
    @FXML
    private FlowPane addAttributeContainer;
    @FXML
    private Button cancelButton;

    private ProductService productService;
    private Runnable onProductUpdated;
    private Product product;

    public void onEditFormOpen(Product product, Runnable callback) {
        this.product = product;
        this.onProductUpdated = callback;

        // Preload text fields
        nameField.setText(product.getName());
        brandField.setText(product.getBrand());
        priceField.setText(PriceUtil.formatCents(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
        supplierField.setText(product.getSupplier());

        // Load dynamic attributes into the form
        Map<String, String> attributes = product.getAttributes();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            addPreFilledAttributeRow(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the database service layer
        productService = new ProductService(new ProductDAO());

        // Customize I/O utilities
        TextFieldUtil.setIntegerOnly(quantityField);
        TextFieldUtil.setCurrencyFormat(priceField);

        // Employ the buttons to their functions
        addAttributeButton.setOnAction(eh -> addAttributeRow());
        saveButton.setOnAction(eh -> saveChanges());
        cancelButton.setOnAction(eh -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

    }

//     Methods / Functions
//    
    // Fills up the current custom attributes of the Product
    private void addPreFilledAttributeRow(String key, String value) {
        TextField keyField = new TextField(key);
        TextField valueField = new TextField(value);
        Button removeBtn = new Button("X");

        HBox row = new HBox(5, new Label("•"), keyField, valueField, removeBtn);
        removeBtn.setOnAction(e -> attributeContainer.getChildren().remove(row));

        // Find the index of the button's container so we can insert above it
        int insertIndex = attributeContainer.getChildren().indexOf(addAttributeContainer);
        if (insertIndex == -1) {
            attributeContainer.getChildren().add(row);
        } else {
            attributeContainer.getChildren().add(insertIndex, row);
        }
    }

    private void addAttributeRow() {
        TextField keyField = new TextField();
        keyField.setPromptText("Attribute Name (i.e Color)");

        TextField valueField = new TextField();
        valueField.setPromptText("Value (i.e Black");

        Button closeButton = new Button("X");
        HBox row = new HBox(5, new Label("•"), keyField, valueField, closeButton);

        // Find the index of the button's container so we can insert above it
        int insertIndex = attributeContainer.getChildren().indexOf(addAttributeContainer);
        if (insertIndex == -1) {
            attributeContainer.getChildren().add(row);
        } else {
            attributeContainer.getChildren().add(insertIndex, row);
        }
    }

    // TODO: Split into specific field slq update statements later
    private void saveChanges() {
        try {
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            String supplier = supplierField.getText().trim();
            String priceText = priceField.getText().trim().replace("$", "").trim();
            int price = PriceUtil.parseToCents(priceText);
            int quantity = Integer.parseInt(quantityField.getText().trim());

            Map<String, String> attributes = new HashMap<>();

            for (javafx.scene.Node node : attributeContainer.getChildren()) {
                if (node instanceof HBox) {
                    HBox row = (HBox) node;

                    TextField keyField = null;
                    TextField valueField = null;
                    for (javafx.scene.Node child : row.getChildren()) {
                        if (child instanceof TextField) {
                            if (keyField == null) {
                                keyField = (TextField) child;
                            } else {
                                valueField = (TextField) child;
                            }
                        }
                    }

                    if (keyField != null && valueField != null) {
                        String key = keyField.getText().trim();
                        String value = valueField.getText().trim();
                        if (!key.isEmpty() && !value.isEmpty()) {
                            attributes.put(key, value);
                        }
                    }
                }
            }
            boolean updated = productService.updateProduct(product.getId(), name, brand, price, quantity, supplier, attributes);
            if (updated) {
                AlertUtil.showInfo("Your product's details have been successfully updated to your inventory.");

                if (onProductUpdated != null) {
                    onProductUpdated.run();
                }
                SceneManager.closeWindow((Stage) editForm.getScene().getWindow());
            }

        } catch (IOException | NumberFormatException ex) {
            AlertUtil.showError("Invalid input: " + ex.getMessage());
        }
    }

}
