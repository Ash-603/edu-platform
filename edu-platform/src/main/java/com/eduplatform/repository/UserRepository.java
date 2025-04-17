package com.eduplatform.repository;

import com.eduplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE TYPE(u) = Student")
    List<User> findAllStudents();

    @Query("SELECT u FROM User u WHERE TYPE(u) = Teacher")
    List<User> findAllTeachers();

    @Query("SELECT COUNT(u) FROM User u WHERE TYPE(u) = Student")
    long countStudents();

    @Query("SELECT COUNT(u) FROM User u WHERE TYPE(u) = Teacher")
    long countTeachers();
}