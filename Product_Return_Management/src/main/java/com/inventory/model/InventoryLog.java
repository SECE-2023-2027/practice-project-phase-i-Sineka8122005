package com.inventory.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class InventoryLog {
    private int logId;
    private int productId;
    private int quantityChange;
    private String actionType;
    private LocalDateTime actionDate;
    
    private static final List<String> VALID_ACTIONS = Arrays.asList("RETURN", "RESTOCK");

    public InventoryLog(int productId, int quantityChange, String actionType) {
        validateProductId(productId);
        validateQuantityChange(quantityChange);
        validateActionType(actionType);
        
        this.productId = productId;
        this.quantityChange = quantityChange;
        this.actionType = actionType.toUpperCase();
        this.actionDate = LocalDateTime.now();
    }

    public int getLogId() { 
        return logId; 
    }

    public void setLogId(int logId) { 
        if (logId <= 0) {
            throw new IllegalArgumentException("Log ID must be positive");
        }
        this.logId = logId; 
    }

    public int getProductId() { 
        return productId; 
    }

    public void setProductId(int productId) { 
        validateProductId(productId);
        this.productId = productId; 
    }

    public int getQuantityChange() { 
        return quantityChange; 
    }

    public void setQuantityChange(int quantityChange) { 
        validateQuantityChange(quantityChange);
        this.quantityChange = quantityChange; 
    }

    public String getActionType() { 
        return actionType; 
    }

    public void setActionType(String actionType) { 
        validateActionType(actionType);
        this.actionType = actionType.toUpperCase(); 
    }

    public LocalDateTime getActionDate() { 
        return actionDate; 
    }

    public void setActionDate(LocalDateTime actionDate) { 
        if (actionDate == null) {
            throw new IllegalArgumentException("Action date cannot be null");
        }
        this.actionDate = actionDate; 
    }

    // Validation methods
    private void validateProductId(int productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
    }

    private void validateQuantityChange(int quantityChange) {
        if (quantityChange == 0) {
            throw new IllegalArgumentException("Quantity change cannot be zero");
        }
    }

    private void validateActionType(String actionType) {
        if (actionType == null || !VALID_ACTIONS.contains(actionType.toUpperCase())) {
            throw new IllegalArgumentException("Invalid action type. Must be either RETURN or RESTOCK");
        }
    }

    @Override
    public String toString() {
        return "InventoryLog{" +
               "logId=" + logId +
               ", productId=" + productId +
               ", quantityChange=" + quantityChange +
               ", actionType='" + actionType + '\'' +
               ", actionDate=" + actionDate +
               '}';
    }
}