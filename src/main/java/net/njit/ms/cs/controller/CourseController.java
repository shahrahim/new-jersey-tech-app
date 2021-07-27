package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.CourseDto;
import net.njit.ms.cs.model.dto.response.CourseResponse;
import net.njit.ms.cs.model.entity.Course;
import net.njit.ms.cs.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<CourseResponse>> getAllCourse() {
        List<Course> courseList = this.courseService.getAllCourse();
        List<CourseResponse> courseResponses = new ArrayList<>();

        courseList.forEach(course -> courseResponses.add(CourseService.getCourseResponse(course)));
        return ResponseEntity.ok().body(courseResponses);
    }

    @GetMapping("/{number}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Integer number) {
        Course course = this.courseService.getCourseById(number);
        return ResponseEntity.ok().body(CourseService.getCourseResponse(course));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> getCreatedCourse(@RequestBody CourseDto courseDto) {
        Course course = this.courseService.getCreatedCourse(courseDto);
        return ResponseEntity.created(null).body(CourseService.getCourseResponse(course));
    }

    @PutMapping("/{number}")
    public ResponseEntity<CourseResponse> getUpdatedCourse(@PathVariable Integer number, @RequestBody CourseDto courseDto) {
        Course course = this.courseService.getUpdatedCourse(number, courseDto);
        return ResponseEntity.ok().body(CourseService.getCourseResponse(course));
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<String> deleteCourse(@PathVariable Integer number) {
        this.courseService.deleteCourse(number);
        String message = String.format("Course with number %s has been deleted.", number);
        return ResponseEntity.ok().body(message);
    }


}
