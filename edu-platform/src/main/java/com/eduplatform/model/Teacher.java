package com.eduplatform.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
public class Teacher extends User {
    private List<Course> teachingCourses = new ArrayList<>();
    private Course createCourse(Course course) {
        teachingCourses.add(course);
        return course;
    }

    public void gradeAssignment(Assignment assignment, int score, String feedback) {
        assignment.setScore(score);
        assignment.setFeedback(feedback);
        assignment.setStatus(SubmissionStatus.GRADED);
    }
}