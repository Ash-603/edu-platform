package com.eduplatform.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlipayPayment extends Payment implements PaymentStrategy {
    @Override
    public boolean processPayment(String userId) {
        // 实现支付宝支付逻辑
        System.out.println("Processing Alipay payment for user: " + userId);
        return true;
    }

    @Override
    public boolean processPayment(User user, double amount) {
        // 支付宝支付具体实现
        System.out.println("Processing Alipay payment of " + amount + " for " + user.getName());
        return true;
    }

    @Override
    public boolean refunding() {
        // 实现支付宝退款逻辑
        System.out.println("Refunding via Alipay for order: " + getOrderId());
        return true;
    }
}