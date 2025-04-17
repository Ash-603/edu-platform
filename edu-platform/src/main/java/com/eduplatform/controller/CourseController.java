package com.eduplatform.controller;

import com.eduplatform.model.Course;
import com.eduplatform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Map<String, Object> request) {
        try {
            String teacherId = (String) request.get("teacherId");
            if (teacherId == null) {
                return ResponseEntity.badRequest().body("Teacher ID is required");
            }

            Course course = new Course();
            course.setTitle((String) request.get("title"));
            course.setDescription((String) request.get("description"));
            course.setPrice(Double.parseDouble(request.get("price").toString()));

            Course createdCourse = courseService.createCourse(teacherId, course);
            return ResponseEntity.ok(createdCourse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourse(@PathVariable String courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        List<Course> courses = courseService.getPublishedCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getTeacherCourses(@PathVariable String teacherId) {
        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{courseId}/publish")
    public ResponseEntity<?> publishCourse(@PathVariable String courseId) {
        try {
            courseService.publishCourse(courseId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<?> enrollStudent(@PathVariable String courseId, @RequestBody Map<String, String> request) {
        try {
            String studentId = request.get("studentId");
            if (studentId == null) {
                return ResponseEntity.badRequest().body("Student ID is required");
            }

            boolean success = courseService.enrollStudent(courseId, studentId);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Enrollment failed");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{courseId}/assignments")
    public ResponseEntity<?> addAssignment(@PathVariable String courseId, @RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String deadlineStr = (String) request.get("deadline");

            // 简化日期处理，实际应该用proper date parsing
            Date deadline = new Date(deadlineStr);

            courseService.addAssignment(courseId, title, deadline);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{courseId}/materials")
    public ResponseEntity<?> uploadMaterial(@PathVariable String courseId,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestParam("title") String title,
                                            @RequestParam("type") String type) {
        try {
            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }

            // 简化处理，实际实现应该上传到文件存储系统
            CourseMaterial material = new CourseMaterial();
            material.setMaterialId("material_" + System.currentTimeMillis());
            material.setTitle(title);
            material.setType(type);
            material.setUrl("/uploads/" + file.getOriginalFilename());

            course.addCourseMaterial(material);
            courseService.updateCourse(course);

            return ResponseEntity.ok(material);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

// CourseMaterial类补充
class CourseMaterial {
    private String materialId;
    private String title;
    private String type;
    private String url;

    // Getters and setters
    public String getMaterialId() { return materialId; }
    public void setMaterialId(String materialId) { this.materialId = materialId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}