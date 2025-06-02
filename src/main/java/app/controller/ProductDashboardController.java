package app.controller;

import app.dao.ProductDAO;
import app.model.Product;
import app.service.ProductService;
import app.util.SceneManager;
import app.util.Session;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductDashboardController implements Initializable {

    @FXML
    private VBox mainVBox;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button addProductButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button logoutButton;

    private ProductService productService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productService = new ProductService(new ProductDAO());
        addProductButton.setOnAction(eh -> openNewProductForm());
        if (Session.get() != null) {
            String username = Session.get().getUsername();
            welcomeLabel.setText("👋 Welcome, " + username);
        }
        
        logoutButton.setOnAction(e -> {
            Session.clear(); // 🔒 clear current session
            try {
                SceneManager.switchTo("login.fxml", 900, 600);
            } catch (IOException ex) {
                System.getLogger(ProductDashboardController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        
        searchField.setOnAction(eh -> searchProduct(searchField));
        searchButton.setOnAction(eh -> searchProduct(searchField));

        refreshProductListUI();

    }

    public void refreshProductListUI() {
        mainVBox.getChildren().clear();
        List<Product> products = productService.getAllProducts();

        for (Product product : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/product-card.fxml"));
                HBox card = loader.load();

                ProductCardController controller = loader.getController();
                controller.setProduct(product, this::refreshProductListUI);
                mainVBox.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openNewProductForm() {
        Consumer<AddProductController> controllerSetup = controller -> {
            controller.setOnProductAdded(this::refreshProductListUI);
        };

        try {
            SceneManager.openWindowAndGetController(
                    "product-register-form.fxml",
                    "Add Product",
                    false,
                    true,
                    controllerSetup
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchProduct(TextField t) {
        String keyword = t.getText().trim();
        List<Product> products;

        if (keyword.isEmpty() || keyword.isBlank()) {
            products = productService.getAllProducts();
        } else {
            products = productService.searchProducts(keyword);
        }

        mainVBox.getChildren().clear();

        for (Product p : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/product-card.fxml"));
                HBox card = loader.load();

                ProductCardController controller = loader.getController();
                controller.setProduct(p, this::refreshProductListUI);
                mainVBox.getChildren().add(card);
            } catch (IOException ex) {
                ex.printStackTrace(); // TODO
            }
        }
    }

}
