package app.controller;

import app.dao.ProductDAO;
import app.model.Product;
import app.service.ProductService;
import app.util.AlertUtil;
import app.util.PriceUtil;
import app.util.SceneManager;
import app.util.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ProductCardController {

    @FXML
    private ImageView imgViewSection;

    @FXML
    private TextField brandField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField skuField;
    @FXML
    private TextField unitsField;
    @FXML
    private TextField supplierField;
    @FXML
    private TextField attributesField;
    @FXML
    private Button editProductButton;
    @FXML
    private Hyperlink deleteProductButton;

    private Product product;
    private Map<String, String> attr = new HashMap<>();

    private ProductService productService;
    private Runnable onRun;

    public void setProduct(Product product, Runnable callback) {
        this.product = product;
        this.onRun = callback;
        attr = product.getAttributes();

        productService = new ProductService(new ProductDAO());

        brandField.setText(product.getBrand());
        nameField.setText(product.getName());
        priceField.setText(PriceUtil.formatCents(product.getPrice()));
        attributesField.setText(attr.toString().trim().replace("{", "").replace("}", "").replace("=", ": ").trim());
        unitsField.setText(Integer.toString(product.getQuantity()) + " units");
        skuField.setText(product.getSku());
        supplierField.setText(product.getSupplier());

        editProductButton.setOnAction(eh -> openEditProductForm(product));
        deleteProductButton.setOnAction(eh -> deleteHandler(product, productService));

        if (!Session.get().getRole().equals("admin")) {
            deleteProductButton.setVisible(false);
            editProductButton.setVisible(false);
        }
    }

    private void openEditProductForm(Product product) {
        try {
            SceneManager.openWindowAndGetController("edit-product.fxml", "Edit Product", false, false, controller -> {
                if (controller instanceof EditProductController) {
                    ((EditProductController) controller).onEditFormOpen(product, onRun);
                }
            });
        } catch (IOException e) {
            AlertUtil.showError("Could not open editor: " + e.getMessage());
        }
    }

    private void deleteHandler(Product product, ProductService ps) {
        if (AlertUtil.showConfirmationDialog("Delete Product", "Are you sure you want to delte this?")) {

            int id = product.getId();
            boolean deleted = productService.deleteProduct(id);
            if (deleted) {
                AlertUtil.showInfo("Product deleted from your inventory.");
                onRun.run();
            } else {
                AlertUtil.showError("Could not delete product: Something went wrong.");
            }
        }
    }

}
