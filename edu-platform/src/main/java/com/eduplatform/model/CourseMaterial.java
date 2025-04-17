package com.eduplatform.model;

import lombok.Data;
import java.util.List;

@Data
public class CourseMaterial {
    private String materialId;
    private String title;
    private String type; // VIDEO, PDF, DOC
    private String url;
    private List<Assignment> assignments;

    public void addMaterial(Assignment assignment) {
        assignments.add(assignment);
    }
}