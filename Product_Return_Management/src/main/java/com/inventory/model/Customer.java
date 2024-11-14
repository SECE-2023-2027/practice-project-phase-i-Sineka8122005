package com.inventory.model;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;

    public Customer(int customerId, String name, String email, String phone) {
        validateCustomerData(customerId, name, email);
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getCustomerId() { 
        return customerId; 
    }

    public void setCustomerId(int customerId) { 
        validateCustomerId(customerId);
        this.customerId = customerId; 
    }
    
    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        validateName(name);
        this.name = name; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        validateEmail(email);
        this.email = email; 
    }

    public String getPhone() { 
        return phone; 
    }

    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    private void validateCustomerData(int customerId, String name, String email) {
        validateCustomerId(customerId);
        validateName(name);
        validateEmail(email);
    }

    private void validateCustomerId(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
               "customerId=" + customerId +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               '}';
    }
}
