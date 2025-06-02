package app.model;

import java.util.HashMap;
import java.util.Map;

public class Product {

    private int id;
    private String sku;
    private String name;
    private String brand;
    private int price;        // stored in cents
    private int quantity;     // stored in units
    private String supplier;
    private Map<String, String> attributes = new HashMap<>();

    // Full constructor (used when loading from DB)
    public Product(int id, String sku, String name, String brand, int price, int quantity, String supplier) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
        this.supplier = supplier;
    }

    // ProductFactory constructor
    public Product(int id, String sku, String name, String brand, int price, int quantity, String supplier, Map<String, String> attributes) {
        this(id, sku, name, brand, price, quantity, supplier);
        this.attributes = attributes;
    }

    // Constructor for new product (before insert, ID will be auto-generated)
    public Product(String sku, String name, String brand, int price, int quantity, String supplier) {
        this(0, sku, name, brand, price, quantity, supplier);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    // Optional helper
    public String getAttribute(String key) {
        return attributes.getOrDefault(key, "");
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }
}
