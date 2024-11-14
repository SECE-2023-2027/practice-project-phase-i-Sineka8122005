package com.inventory.model;

public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int currentStock;
    private static final double MIN_PRICE = 0.01;

    public Product(int productId, String name, String description, double price, int currentStock) {
        validateProductData(productId, name, price, currentStock);
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currentStock = currentStock;
    }

    public int getProductId() { 
        return productId; 
    }

    public void setProductId(int productId) { 
        validateProductId(productId);
        this.productId = productId; 
    }
    
    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        validateName(name);
        this.name = name; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public double getPrice() { 
        return price; 
    }

    public void setPrice(double price) { 
        validatePrice(price);
        this.price = price; 
    }

    public int getCurrentStock() { 
        return currentStock; 
    }

    public void setCurrentStock(int currentStock) { 
        validateStock(currentStock);
        this.currentStock = currentStock; 
    }

    private void validateProductData(int productId, String name, double price, int currentStock) {
        validateProductId(productId);
        validateName(name);
        validatePrice(price);
        validateStock(currentStock);
    }

    private void validateProductId(int productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
    }

    private void validatePrice(double price) {
        if (price < MIN_PRICE) {
            throw new IllegalArgumentException("Price must be greater than " + MIN_PRICE);
        }
    }

    private void validateStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }

    // Business methods
    public void incrementStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Increment quantity must be positive");
        }
        this.currentStock += quantity;
    }

    public void decrementStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Decrement quantity must be positive");
        }
        if (quantity > this.currentStock) {
            throw new IllegalStateException("Insufficient stock available");
        }
        this.currentStock -= quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
               "productId=" + productId +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", price=" + price +
               ", currentStock=" + currentStock +
               '}';
    }
}