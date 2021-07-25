package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.CourseDto;
import net.njit.ms.cs.model.entity.Course;
import net.njit.ms.cs.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Course>> getAllCourse() {
        List<Course> courseList = this.courseService.getAllCourse();
        return ResponseEntity.ok().body(courseList);
    }

    @GetMapping("/{number}")
    public ResponseEntity<Course> getCourseById(@PathVariable Integer number) {
        Course course = this.courseService.getCourseById(number);
        return ResponseEntity.ok().body(course);
    }

    @PostMapping
    public ResponseEntity<Course> getCreatedCourse(@RequestBody CourseDto courseDto) {
        return ResponseEntity.created(null).body(this.courseService.getCreatedCourse(courseDto));
    }

    @PutMapping("/{number}")
    public ResponseEntity<Course> getUpdatedCourse(@PathVariable Integer number, @RequestBody CourseDto courseDto) {
        Course course = this.courseService.getUpdatedCourse(number, courseDto);
        return ResponseEntity.ok().body(course);
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<String> deleteCourse(@PathVariable Integer number) {
        this.courseService.deleteCourse(number);
        String message = String.format("Course with number %s has been deleted.", number);
        return ResponseEntity.ok().body(message);
    }


}
