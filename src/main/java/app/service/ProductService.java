package app.service;

import app.dao.ProductDAO;
import app.model.Product;
import app.util.SkuGenerator;
import java.util.List;
import java.util.Map;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public boolean addNewProduct(String name, String brand, int price, Map<String, String> attributes, int quantity, String supplier) {
        String sku = SkuGenerator.generateUnique(productDAO, name, attributes.get("type"), attributes.get("variant"));

        Product product = new Product(0, sku, name, brand, price, quantity, supplier);
        product.setAttributes(attributes);

        return productDAO.insert(product);
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Product findBySKU(String sku) {
        return productDAO.findBySku(sku);
    }

    public Product findById(int id) {
        return productDAO.findById(id);
    }

    public boolean deleteProduct(int id) {
        return productDAO.deleteById(id);
    }

    // TODO: Split into specific field slq update statements
    public boolean updateProduct(int id, String name, String brand, int price, int quantity, String supplier, Map<String, String> attributes) {
        Product existingProduct = productDAO.findById(id);
        System.out.println("🔍 Searching for ID: " + existingProduct.getSku());

        if (existingProduct == null) {
            return false; // product doesn't exist
        }

        System.out.println("🔁 Updating product: " + existingProduct.getId());
        System.out.println("🧩 Attributes: " + attributes);

        existingProduct.setName(name);
        existingProduct.setBrand(brand);
        existingProduct.setPrice(price);
        existingProduct.setQuantity(quantity);
        existingProduct.setSupplier(supplier);
        existingProduct.setAttributes(attributes);

        return productDAO.update(existingProduct);
    }

    public List<Product> searchProducts(String keyword) {
        return productDAO.searchByKeyword(keyword);
    }
}
