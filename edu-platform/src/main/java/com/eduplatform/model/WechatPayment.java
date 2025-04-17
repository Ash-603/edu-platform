package com.eduplatform.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WechatPayment extends Payment implements PaymentStrategy {
    @Override
    public boolean processPayment(String userId) {
        // 实现微信支付逻辑
        System.out.println("Processing Wechat payment for user: " + userId);
        return true;
    }

    @Override
    public boolean processPayment(User user, double amount) {
        // 微信支付具体实现
        System.out.println("Processing Wechat payment of " + amount + " for " + user.getName());
        return true;
    }

    @Override
    public boolean refunding() {
        // 实现微信退款逻辑
        System.out.println("Refunding via Wechat for order: " + getOrderId());
        return true;
    }
}