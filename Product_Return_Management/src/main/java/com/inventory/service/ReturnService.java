package com.inventory.service;

import com.inventory.util.DatabaseConnection;
import com.inventory.exception.InvalidReturnException;
import java.sql.*;
import java.util.Scanner;

public class ReturnService {
    private Connection conn;

    public ReturnService() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public void processNewReturn(String customerId, String productId, String orderId, String reason, String condition) {
        try {
            conn.setAutoCommit(false);

            // Check if customer exists, otherwise create a new customer
            if (!customerExists(customerId)) {
                System.out.print("Customer not found. Enter customer name: ");
                String name = new Scanner(System.in).nextLine();
                System.out.print("Enter customer email: ");
                String email = new Scanner(System.in).nextLine();
                System.out.print("Enter customer phone: ");
                String phone = new Scanner(System.in).nextLine();
                createCustomer(customerId, name, email, phone);
            }

            // Check if product exists
            if (!productExists(productId)) {
                System.out.println("Product not found. Please verify the Product ID.");
                conn.rollback();
                return;
            }

            // Record the return for the specific product and customer
            createReturnRecord(orderId, customerId, productId, reason, condition);

            // Commit transaction if everything is successful
            conn.commit();
            System.out.println("Return processed successfully!");

        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Error processing return. Transaction rolled back.");
            } catch (SQLException rollbackEx) {
                System.out.println("Error during transaction rollback: " + rollbackEx.getMessage());
            }
            System.out.println("Error processing return: " + e.getMessage());
        }
    }

    private boolean customerExists(String customerId) throws SQLException {
        String sql = "SELECT 1 FROM customers WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            return stmt.executeQuery().next();
        }
    }

    private void createCustomer(String customerId, String name, String email, String phone) throws SQLException {
        String sql = "INSERT INTO customers (customer_id, name, email, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.executeUpdate();
        }
    }

    private boolean productExists(String productId) throws SQLException {
        String sql = "SELECT 1 FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productId);
            return stmt.executeQuery().next();
        }
    }

    private void createReturnRecord(String orderId, String customerId, String productId, String reason, String condition) throws SQLException {
        String sql = "INSERT INTO returns (order_id, customer_id, product_id, reason, product_condition, status) VALUES (?, ?, ?, ?, ?, 'PENDING')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderId);       // Make sure you pass the order_id
            stmt.setString(2, customerId);    // Pass the customer_id
            stmt.setString(3, productId);     // Pass the product_id
            stmt.setString(4, reason);        // Pass the reason
            stmt.setString(5, condition);     // Pass the condition
            stmt.executeUpdate();
        }
    }

    public void viewReturnStatus(String returnId) {
        String sql = "SELECT return_id, customer_id, product_id, created_date, reason, `product_condition`, status FROM returns WHERE return_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, returnId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Return ID: " + rs.getInt("return_id"));
                System.out.println("Customer ID: " + rs.getString("customer_id"));
                System.out.println("Product ID: " + rs.getString("product_id"));
                System.out.println("Created Date: " + rs.getTimestamp("created_date"));
                System.out.println("Reason: " + rs.getString("reason"));
                System.out.println("Condition: " + rs.getString("product_condition"));
                System.out.println("Status: " + rs.getString("status"));
            } else {
                System.out.println("Return not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing return: " + e.getMessage());
        }
    }

    public void generateReport(int reportType) {
        String timeFrame = reportType == 1 ? "DAY" : reportType == 2 ? "WEEK" : "MONTH";
        String sql = "SELECT status, COUNT(*) as count FROM returns WHERE created_date >= DATE_SUB(NOW(), INTERVAL 1 " + timeFrame + ") GROUP BY status";

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\nReturn Report for the last " + timeFrame);
            System.out.println("------------------------");
            while (rs.next()) {
                System.out.printf("%s: %d returns\n", rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
}