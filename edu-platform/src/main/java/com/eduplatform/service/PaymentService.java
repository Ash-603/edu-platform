package com.eduplatform.service;

import com.eduplatform.model.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.*;

@Service
public class PaymentService {
    private Map<String, Order> orders = new HashMap<>();
    private final CourseService courseService;
    private final UserService userService;
    private final NotificationService notificationService;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PaymentService(CourseService courseService, UserService userService, NotificationService notificationService) {
        this.courseService = courseService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Order createPaymentOrder(String courseId, String studentId, double amount) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        User user = userService.getUserById(studentId);
        if (!(user instanceof Student)) {
            throw new RuntimeException("Only students can create orders");
        }

        Order order = new Order(studentId, courseId, amount);
        orders.put(order.getOrderId(), order);

        // 设置30分钟订单过期
        scheduler.schedule(() -> {
            Order savedOrder = orders.get(order.getOrderId());
            if (savedOrder != null && savedOrder.getStatus() == OrderStatus.PENDING) {
                savedOrder.setStatus(OrderStatus.CANCELED);
                System.out.println("Order timed out: " + order.getOrderId());
            }
        }, 30, TimeUnit.MINUTES);

        return order;
    }

    public String getPaymentPageLink(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        // 生成支付链接
        return "https://payment.example.com/pay?orderId=" + orderId;
    }

    public boolean processPayment(String orderId, String paymentMethod) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            return false;
        }

        Payment payment;
        if ("wechat".equals(paymentMethod)) {
            payment = new WechatPayment();
        } else if ("alipay".equals(paymentMethod)) {
            payment = new AlipayPayment();
        } else {
            throw new RuntimeException("Unsupported payment method");
        }

        payment.setOrderId(orderId);
        payment.setAmount(order.getAmount());

        boolean success = order.processPayment(payment);
        if (success) {
            // 支付成功，授予课程访问权限
            courseService.enrollStudent(order.getCourseId(), order.getStudentId());

            // 发送支付成功通知
            notificationService.sendPaymentSuccessNotification(order.getStudentId(), order.getCourseId());
        }

        return success;
    }

    public Order getOrderById(String orderId) {
        return orders.get(orderId);
    }

    public List<Order> getOrdersByStudent(String studentId) {
        List<Order> studentOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getStudentId().equals(studentId)) {
                studentOrders.add(order);
            }
        }
        return studentOrders;
    }

    public boolean requestRefund(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (order.getStatus() != OrderStatus.PAID) {
            return false;
        }

        // 检查退款条件
        long daysSincePurchase = TimeUnit.DAYS.convert(
                new Date().getTime() - order.getPayTime().getTime(), TimeUnit.MILLISECONDS
        );

        if (daysSincePurchase > 7) {
            throw new RuntimeException("Refund only available within 7 days of purchase");
        }

        // 退款需要教师审核
        // 这里简化处理，自动执行退款
        boolean success = order.refund();

        if (success) {
            // 收回课程访问权限
            // 这里简化，在实际实现中需要移除学生对课程的访问权限
            System.out.println("Course access revoked for student: " + order.getStudentId());
        }

        return success;
    }
}