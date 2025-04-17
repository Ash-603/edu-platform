package com.eduplatform.model;

import lombok.Data;
import java.util.Date;

@Data
public class Submission {
    private String submissionId;
    private String assignmentId;
    private String studentId;
    private String content;
    private Date submitTime;
    private Integer score;
    private String feedback;
    private SubmissionStatus status;
    private boolean lateSubmission;

    public Submission() {
        this.submissionId = "sub_" + System.currentTimeMillis();
        this.submitTime = new Date();
        this.status = SubmissionStatus.SUBMITTED;
    }

    public void grade(int score, String feedback) {
        this.score = score;
        this.feedback = feedback;
        this.status = SubmissionStatus.GRADED;
    }
}