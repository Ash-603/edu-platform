package com.eduplatform.repository;

import com.eduplatform.model.Order;
import com.eduplatform.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByStudentId(String studentId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCreateTimeBetween(Date start, Date end);

    @Query("SELECT o FROM Order o WHERE o.status = ?1 AND o.createTime < ?2")
    List<Order> findExpiredOrders(OrderStatus status, Date expiryTime);

    @Query("SELECT SUM(o.amount) FROM Order o WHERE o.status = 'PAID' AND o.payTime BETWEEN ?1 AND ?2")
    Double calculateRevenueInPeriod(Date start, Date end);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PAID' AND o.courseId = ?1")
    long countPaidOrdersByCourse(String courseId);
}