package com.eduplatform.model;

public interface PaymentStrategy {
    boolean processPayment(User user, double amount);
    boolean refunding();
}