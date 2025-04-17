package com.eduplatform.model;

import lombok.Data;
import java.util.Date;

@Data
public class Assignment {
    private String assignmentId;
    private String title;
    private String description;
    private Date deadline;
    private SubmissionStatus status;
    private List<Submission> submissions = new ArrayList<>();
    private int score;
    private String feedback;

    public SubmissionStatus checkSubmissionStatus() {
        Date now = new Date();
        if (now.after(deadline)) {
            return SubmissionStatus.OVERDUE;
        }
        return status;
    }
}

enum SubmissionStatus {
    PENDING, SUBMITTED, GRADED, OVERDUE
}