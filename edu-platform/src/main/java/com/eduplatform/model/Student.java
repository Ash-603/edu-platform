package com.eduplatform.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    private List<Course> enrolledCourses = new ArrayList<>();
    private boolean enrolled = false;

    public void enrollCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            enrolled = true;
        }
    }

    public boolean submitAssignment(Assignment assignment, String content) {
        return assignment.checkSubmissionStatus() != SubmissionStatus.OVERDUE;
    }
}