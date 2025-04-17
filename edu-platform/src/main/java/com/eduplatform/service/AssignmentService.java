package com.eduplatform.service;

import com.eduplatform.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AssignmentService {
    private Map<String, Assignment> assignments = new HashMap<>();
    private final UserService userService;
    private final NotificationService notificationService;

    public AssignmentService(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Assignment createAssignment(String courseId, String title, String description, Date deadline) {
        Assignment assignment = new Assignment();
        assignment.setAssignmentId(generateAssignmentId());
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDeadline(deadline);
        assignment.setStatus(SubmissionStatus.PENDING);

        assignments.put(assignment.getAssignmentId(), assignment);
        return assignment;
    }

    public Assignment getAssignmentById(String assignmentId) {
        return assignments.get(assignmentId);
    }

    public boolean submitAssignment(String assignmentId, String studentId, String content) {
        Assignment assignment = assignments.get(assignmentId);
        if (assignment == null) {
            throw new RuntimeException("Assignment not found");
        }

        User user = userService.getUserById(studentId);
        if (!(user instanceof Student)) {
            throw new RuntimeException("Only students can submit assignments");
        }

        // 检查截止日期
        if (assignment.checkSubmissionStatus() == SubmissionStatus.OVERDUE) {
            // 允许提交但标记为迟交
            System.out.println("Late submission detected");
        }

        // 创建或更新提交
        Submission submission = new Submission();
        submission.setAssignmentId(assignmentId);
        submission.setStudentId(studentId);
        submission.setContent(content);
        submission.setSubmitTime(new Date());

        assignment.getSubmissions().add(submission);
        assignment.setStatus(SubmissionStatus.SUBMITTED);

        return true;
    }

    public boolean gradeAssignment(String assignmentId, String studentId, int score, String feedback) {
        Assignment assignment = assignments.get(assignmentId);
        if (assignment == null) {
            throw new RuntimeException("Assignment not found");
        }

        // 找到对应学生的提交
        for (Submission submission : assignment.getSubmissions()) {
            if (submission.getStudentId().equals(studentId)) {
                submission.setScore(score);
                submission.setFeedback(feedback);

                // 发送评分通知
                notificationService.sendGradingNotification(studentId, assignmentId, score);
                return true;
            }
        }

        return false;
    }

    public List<Assignment> getPendingAssignments(String teacherId) {
        List<Assignment> pendingAssignments = new ArrayList<>();
        for (Assignment assignment : assignments.values()) {
            if (assignment.getStatus() == SubmissionStatus.SUBMITTED) {
                pendingAssignments.add(assignment);
            }
        }
        return pendingAssignments;
    }

    private String generateAssignmentId() {
        return "assignment_" + System.currentTimeMillis();
    }
}

// 补充Submission类
class Submission {
    private String studentId;
    private String assignmentId;
    private String content;
    private Date submitTime;
    private Integer score;
    private String feedback;

    // Getters and setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getAssignmentId() { return assignmentId; }
    public void setAssignmentId(String assignmentId) { this.assignmentId = assignmentId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}