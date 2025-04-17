package com.eduplatform.controller;

import com.eduplatform.model.Order;
import com.eduplatform.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> request) {
        try {
            String courseId = (String) request.get("courseId");
            String studentId = (String) request.get("studentId");
            Double amount = Double.parseDouble(request.get("amount").toString());

            if (courseId == null || studentId == null || amount == null) {
                return ResponseEntity.badRequest().body("Course ID, student ID, and amount are required");
            }

            Order order = paymentService.createPaymentOrder(courseId, studentId, amount);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        Order order = paymentService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders/{studentId}")
    public ResponseEntity<?> getStudentOrders(@PathVariable String studentId) {
        List<Order> orders = paymentService.getOrdersByStudent(studentId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/pay/{orderId}")
    public ResponseEntity<?> getPaymentPage(@PathVariable String orderId) {
        try {
            String paymentLink = paymentService.getPaymentPageLink(orderId);
            return ResponseEntity.ok(Map.of("paymentLink", paymentLink));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/process/{orderId}")
    public ResponseEntity<?> processPayment(@PathVariable String orderId, @RequestBody Map<String, String> request) {
        try {
            String paymentMethod = request.get("paymentMethod");
            if (paymentMethod == null) {
                return ResponseEntity.badRequest().body("Payment method is required");
            }

            boolean success = paymentService.processPayment(orderId, paymentMethod);
            if (success) {
                return ResponseEntity.ok().body("Payment processed successfully");
            } else {
                return ResponseEntity.badRequest().body("Payment processing failed");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refund/{orderId}")
    public ResponseEntity<?> requestRefund(@PathVariable String orderId) {
        try {
            boolean success = paymentService.requestRefund(orderId);
            if (success) {
                return ResponseEntity.ok().body("Refund processed successfully");
            } else {
                return ResponseEntity.badRequest().body("Refund processing failed");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}