package com.inventory;

import java.sql.SQLException;
import java.util.Scanner;

import com.inventory.service.ReturnService;

public class ReturnManagementApp {
    private static ReturnService returnService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        returnService = new ReturnService();
        
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            processChoice(choice);
        }
    }

    private static void displayMenu() {
        System.out.println("\n1. Process New Return");
        System.out.println("2. View Return Status");
        System.out.println("3. Generate Return Report");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void processChoice(int choice) {
        switch (choice) {
            case 1:
                processNewReturn();
                break;
            case 2:
                viewReturnStatus();
                break;
            case 3:
                generateReturnReport();
                break;
            case 4:
                System.out.println("Thank you for using Returns Management System!");
                System.exit(0);
            default:
                System.out.println("Invalid choice! Please select 1-4");
        }
    }

    private static void processNewReturn() {
        scanner.nextLine(); 
        System.out.print("Enter Order ID: ");
        String orderId = scanner.nextLine();
        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine();
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine();
        System.out.print("Enter Return Reason: ");
        String reason = scanner.nextLine();
        System.out.print("Enter Product Condition (Good/Damaged): ");
        String condition = scanner.nextLine();

        returnService.processNewReturn(orderId,  customerId, productId, reason, condition);
    }

    private static void viewReturnStatus() {
        scanner.nextLine();
        System.out.print("Enter Return ID to check status: ");
        String returnId = scanner.nextLine();
        
        returnService.viewReturnStatus(returnId);
    }

    private static void generateReturnReport() {
        System.out.println("1. Daily Report");
        System.out.println("2. Weekly Report");
        System.out.println("3. Monthly Report");
        System.out.print("Choose report type: ");
        int choice = scanner.nextInt();
        
        returnService.generateReport(choice);
    }
}