package com.eduplatform.model;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Data
public class Course {
    private String courseId;
    private String title;
    private String description;
    private double price;
    private CourseStatus status;
    private List<Assignment> assignments = new ArrayList<>();
    private List<CourseMaterial> materials = new ArrayList<>();
    private Teacher teacher;
    private List<Student> enrolledStudents = new ArrayList<>();

    public void addAssignment(String title, Date deadline) {
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDeadline(deadline);
        assignment.setStatus(SubmissionStatus.PENDING);
        assignments.add(assignment);
    }

    public void addCourseMaterial(CourseMaterial material) {
        materials.add(material);
    }

    public void publish() {
        this.status = CourseStatus.PUBLISHED;
    }
}

enum CourseStatus {
    DRAFT, PUBLISHED, ARCHIVED
}