package com.inventory.model;

import com.inventory.interfaces.Returnable;
import java.time.LocalDateTime;

public abstract class Return implements Returnable {
    protected int returnId;
    protected int customerId;
    protected int productId;
    protected String reason;
    protected ReturnStatus status;
    protected LocalDateTime returnDate;

    public Return(int customerId, int productId, String reason) {
        validateReturnData(customerId, productId, reason);
        this.customerId = customerId;
        this.productId = productId;
        this.reason = reason;
        this.status = ReturnStatus.PENDING;
        this.returnDate = LocalDateTime.now();
    }

    public abstract boolean processReturn();
    public abstract void updateStock();

    @Override
    public String generateReturnReport() {
        return String.format("Return ID: %d\nCustomer ID: %d\nProduct ID: %d\nStatus: %s\nReturn Date: %s\nReason: %s",
                returnId, customerId, productId, status, returnDate, reason);
    }

    public int getReturnId() { 
        return returnId; 
    }

    public void setReturnId(int returnId) { 
        validateId(returnId, "Return ID");
        this.returnId = returnId; 
    }

    public int getCustomerId() { 
        return customerId; 
    }

    public void setCustomerId(int customerId) { 
        validateId(customerId, "Customer ID");
        this.customerId = customerId; 
    }

    public int getProductId() { 
        return productId; 
    }

    public void setProductId(int productId) { 
        validateId(productId, "Product ID");
        this.productId = productId; 
    }

    public String getReason() { 
        return reason; 
    }

    public void setReason(String reason) { 
        validateReason(reason);
        this.reason = reason; 
    }

    public ReturnStatus getStatus() { 
        return status; 
    }

    public void setStatus(ReturnStatus status) { 
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status; 
    }

    public LocalDateTime getReturnDate() { 
        return returnDate; 
    }

    public void setReturnDate(LocalDateTime returnDate) { 
        if (returnDate == null) {
            throw new IllegalArgumentException("Return date cannot be null");
        }
        this.returnDate = returnDate; 
    }

    protected void validateReturnData(int customerId, int productId, String reason) {
        validateId(customerId, "Customer ID");
        validateId(productId, "Product ID");
        validateReason(reason);
    }

    protected void validateId(int id, String fieldName) {
        if (id <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
    }

    protected void validateReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Return reason is required");
        }
    }

    @Override
    public String toString() {
        return "Return{" +
               "returnId=" + returnId +
               ", customerId=" + customerId +
               ", productId=" + productId +
               ", reason='" + reason + '\'' +
               ", status=" + status +
               ", returnDate=" + returnDate +
               '}';
    }
}