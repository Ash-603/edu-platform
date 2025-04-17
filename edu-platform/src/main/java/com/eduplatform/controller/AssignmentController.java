package com.eduplatform.controller;

import com.eduplatform.model.Assignment;
import com.eduplatform.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<?> getAssignment(@PathVariable String assignmentId) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assignment);
    }

    @PostMapping("/submit/{assignmentId}")
    public ResponseEntity<?> submitAssignment(@PathVariable String assignmentId,
                                              @RequestParam("studentId") String studentId,
                                              @RequestParam(value = "file", required = false) MultipartFile file,
                                              @RequestParam(value = "content", required = false) String content) {
        try {
            String submissionContent;
            if (file != null) {
                // 处理文件上传，简化示例
                submissionContent = "File: " + file.getOriginalFilename();
            } else {
                submissionContent = content;
            }

            boolean success = assignmentService.submitAssignment(assignmentId, studentId, submissionContent);
            if (success) {
                return ResponseEntity.ok().body("Assignment submitted successfully");
            } else {
                return ResponseEntity.badRequest().body("Assignment submission failed");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/grade/{assignmentId}")
    public ResponseEntity<?> gradeAssignment(@PathVariable String assignmentId, @RequestBody Map<String, Object> request) {
        try {
            String studentId = (String) request.get("studentId");
            Integer score = (Integer) request.get("score");
            String feedback = (String) request.get("feedback");

            if (studentId == null || score == null) {
                return ResponseEntity.badRequest().body("Student ID and score are required");
            }

            boolean success = assignmentService.gradeAssignment(assignmentId, studentId, score, feedback);
            if (success) {
                return ResponseEntity.ok().body("Assignment graded successfully");
            } else {
                return ResponseEntity.badRequest().body("Assignment grading failed");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pending/{teacherId}")
    public ResponseEntity<?> getPendingAssignments(@PathVariable String teacherId) {
        List<Assignment> pendingAssignments = assignmentService.getPendingAssignments(teacherId);
        return ResponseEntity.ok(pendingAssignments);
    }
}