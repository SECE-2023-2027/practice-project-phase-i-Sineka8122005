package com.inventory.model;

import com.inventory.exception.InvalidReturnException;
import com.inventory.exception.StockUpdateFailureException;
import com.inventory.util.DatabaseConnection;
import java.sql.*;

public class CustomerReturn extends Return {
    private boolean isInspected;
    private String condition;
    private ReturnStatus status;
    private Connection conn;

    public CustomerReturn(int customerId, int productId, String reason, String condition) throws SQLException {
        super(customerId, productId, reason);
        this.condition = condition;
        this.isInspected = false;
        this.status = ReturnStatus.PENDING;
        this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public boolean processReturn() {
        try {
            conn.setAutoCommit(false);
            
            validateReturn();
            inspectReturn();
            createReturnRecord();
            updateStock();
            updateReturnStatus();
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            rollbackTransaction();
            throw new InvalidReturnException("Return processing failed: " + e.getMessage());
        }
    }

    private void createReturnRecord() throws SQLException {
        String sql = "INSERT INTO returns (customer_id, product_id, reason, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, getCustomerId());
            stmt.setInt(2, getProductId());
            stmt.setString(3, getReason());
            stmt.setString(4, status.toString());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateStock() {
        if (!isInspected || status != ReturnStatus.APPROVED) {
            return;
        }

        String sql = "UPDATE products SET current_stock = current_stock + 1 WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, getProductId());
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new StockUpdateFailureException("Product not found in inventory");
            }
            
            logInventoryChange();
        } catch (SQLException e) {
            throw new StockUpdateFailureException("Failed to update inventory: " + e.getMessage());
        }
    }

    private void validateReturn() {
        if (getCustomerId() <= 0) {
            throw new InvalidReturnException("Invalid customer ID");
        }
        if (getProductId() <= 0) {
            throw new InvalidReturnException("Invalid product ID");
        }
        if (getReason() == null || getReason().trim().isEmpty()) {
            throw new InvalidReturnException("Return reason is required");
        }
        if (!isValidCondition()) {
            throw new InvalidReturnException("Invalid product condition");
        }
        validateCustomerExists();
        validateProductExists();
    }

    private boolean isValidCondition() {
        return condition != null && condition.matches("(?i)(good|damaged)");
    }

    private void validateCustomerExists() {
        String sql = "SELECT 1 FROM customers WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, getCustomerId());
            if (!stmt.executeQuery().next()) {
                throw new InvalidReturnException("Customer not found");
            }
        } catch (SQLException e) {
            throw new InvalidReturnException("Error validating customer: " + e.getMessage());
        }
    }

    private void validateProductExists() {
        String sql = "SELECT 1 FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, getProductId());
            if (!stmt.executeQuery().next()) {
                throw new InvalidReturnException("Product not found");
            }
        } catch (SQLException e) {
            throw new InvalidReturnException("Error validating product: " + e.getMessage());
        }
    }

    private void inspectReturn() {
        this.isInspected = true;
        this.status = condition.equalsIgnoreCase("good") ? 
            ReturnStatus.APPROVED : ReturnStatus.REJECTED;
    }

    private void updateReturnStatus() throws SQLException {
        String sql = "UPDATE returns SET status = ? WHERE customer_id = ? AND product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.toString());
            stmt.setInt(2, getCustomerId());
            stmt.setInt(3, getProductId());
            stmt.executeUpdate();
        }
    }

    private void logInventoryChange() throws SQLException {
        String sql = "INSERT INTO inventory_log (product_id, quantity_change, action_type) VALUES (?, 1, 'RETURN')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, getProductId());
            stmt.executeUpdate();
        }
    }

    private void rollbackTransaction() {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            throw new StockUpdateFailureException("Transaction rollback failed");
        }
    }

    public boolean isInspected() {
        return isInspected;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        if (!condition.matches("(?i)(good|damaged)")) {
            throw new InvalidReturnException("Invalid product condition");
        }
        this.condition = condition;
    }

    public ReturnStatus getStatus() {
        return status;
    }
}