package com.eduplatform.model;

import lombok.Data;
import java.util.Date;

@Data
public class Order {
    private String orderId;
    private String studentId;
    private String courseId;
    private double amount;
    private Date createTime;
    private Date payTime;
    private OrderStatus status;
    private Payment payment;

    public Order(String studentId, String courseId, double amount) {
        this.orderId = generateOrderId();
        this.studentId = studentId;
        this.courseId = courseId;
        this.amount = amount;
        this.createTime = new Date();
        this.status = OrderStatus.PENDING;
    }

    private String generateOrderId() {
        return "OD" + System.currentTimeMillis();
    }

    public boolean processPayment(Payment payment) {
        this.payment = payment;
        boolean result = payment.processPayment(studentId);
        if (result) {
            this.status = OrderStatus.PAID;
            this.payTime = new Date();
        }
        return result;
    }

    public boolean refund() {
        if (this.status != OrderStatus.PAID) {
            return false;
        }

        boolean result = payment.refunding();
        if (result) {
            this.status = OrderStatus.REFUNDED;
        }
        return result;
    }
}

enum OrderStatus {
    PENDING, PAID, CANCELED, REFUNDED
}