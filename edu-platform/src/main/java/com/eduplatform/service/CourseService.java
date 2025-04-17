package com.eduplatform.service;

import com.eduplatform.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CourseService {
    private Map<String, Course> courses = new HashMap<>();
    private final UserService userService;

    public CourseService(UserService userService) {
        this.userService = userService;
    }

    public Course createCourse(String teacherId, Course course) {
        User user = userService.getUserById(teacherId);
        if (!(user instanceof Teacher)) {
            throw new RuntimeException("Only teachers can create courses");
        }

        String courseId = generateCourseId();
        course.setCourseId(courseId);
        course.setTeacher((Teacher) user);
        course.setStatus(CourseStatus.DRAFT);

        courses.put(courseId, course);
        return course;
    }

    public Course getCourseById(String courseId) {
        return courses.get(courseId);
    }

    public List<Course> getCoursesByTeacher(String teacherId) {
        List<Course> teacherCourses = new ArrayList<>();
        for (Course course : courses.values()) {
            if (course.getTeacher().getUserId().equals(teacherId)) {
                teacherCourses.add(course);
            }
        }
        return teacherCourses;
    }

    public List<Course> getPublishedCourses() {
        List<Course> publishedCourses = new ArrayList<>();
        for (Course course : courses.values()) {
            if (course.getStatus() == CourseStatus.PUBLISHED) {
                publishedCourses.add(course);
            }
        }
        return publishedCourses;
    }

    public Course updateCourse(Course course) {
        if (!courses.containsKey(course.getCourseId())) {
            throw new RuntimeException("Course not found");
        }
        courses.put(course.getCourseId(), course);
        return course;
    }

    public boolean enrollStudent(String courseId, String studentId) {
        Course course = courses.get(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        User user = userService.getUserById(studentId);
        if (!(user instanceof Student)) {
            throw new RuntimeException("Only students can enroll in courses");
        }

        Student student = (Student) user;
        course.getEnrolledStudents().add(student);
        student.getEnrolledCourses().add(course);

        return true;
    }

    public void publishCourse(String courseId) {
        Course course = courses.get(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        course.setStatus(CourseStatus.PUBLISHED);
        courses.put(courseId, course);
    }

    public void addAssignment(String courseId, String title, Date deadline) {
        Course course = courses.get(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        course.addAssignment(title, deadline);
    }

    private String generateCourseId() {
        return "course_" + System.currentTimeMillis();
    }
}