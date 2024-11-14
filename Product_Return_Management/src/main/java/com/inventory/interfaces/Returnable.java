package com.inventory.interfaces;

public interface Returnable {
    boolean processReturn();
    void updateStock();
    String generateReturnReport();
}