package com.eduplatform.model;

import lombok.Data;
import java.util.Date;

@Data
public abstract class Payment {
    private String orderId;
    private String paymentMethod;
    private Double amount;
    private boolean refundingStatus;
    private boolean statusTimedOut;

    public abstract boolean processPayment(String userId);
    public abstract boolean refunding();
}