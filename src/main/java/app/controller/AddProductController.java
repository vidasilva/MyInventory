package app.controller;

import app.dao.ProductDAO;
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

public class AddProductController implements Initializable {

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
    private Button registerProductButton;
    @FXML
    private Button addAttributeButton;
    @FXML
    private Button cancelButton;
    @FXML
    private VBox attributeContainer;
    @FXML
    private FlowPane addAttributeContainer;

    private ProductService productService;
    private Runnable onProductAdded;

    public void setOnProductAdded(Runnable callback) {
        this.onProductAdded = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productService = new ProductService(new ProductDAO());

        TextFieldUtil.setIntegerOnly(quantityField);
        TextFieldUtil.setCurrencyFormat(priceField);

        addAttributeButton.setOnAction(eh -> addAttributeRow());
        registerProductButton.setOnAction(eh -> handleRegisterProduct());
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

    }

    private void addAttributeRow() {
        TextField keyField = new TextField();
        keyField.setPromptText("Attribute Name");

        TextField valueField = new TextField();
        valueField.setPromptText("Value");

        Button closeButton = new Button("X");
        HBox row = new HBox(5, new Label("•"), keyField, valueField, closeButton);

        // Find the index of the button so we can insert above it
        int insertIndex = attributeContainer.getChildren().indexOf(addAttributeContainer);
        if (insertIndex == -1) {
            attributeContainer.getChildren().add(row);
        } else {
            attributeContainer.getChildren().add(insertIndex, row);
        }
    }

    private void handleRegisterProduct() {
        try {
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            String priceText = priceField.getText().trim().replace("$", "").trim();
            int price = PriceUtil.parseToCents(priceText);
            int quantity = Integer.parseInt(quantityField.getText().trim());
            String supplier = supplierField.getText().trim();

            // Build attributes map from VBox
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

            boolean created = productService.addNewProduct(name, brand, price, attributes, quantity, supplier);
            if (created) {
                AlertUtil.showInfo("Your product was successfully added to your inventory.");

                if (onProductAdded != null) {
                    onProductAdded.run();
                }

                Stage stage = (Stage) nameField.getScene().getWindow();
                SceneManager.closeWindow(stage);
            }

        } catch (IOException | NumberFormatException e) {
            AlertUtil.showError("Invalid input: " + e.getMessage());
        }
    }

}
