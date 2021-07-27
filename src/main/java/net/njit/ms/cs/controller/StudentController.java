package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.StudentDto;
import net.njit.ms.cs.model.dto.response.StudentResponse;
import net.njit.ms.cs.model.entity.Student;
import net.njit.ms.cs.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<StudentResponse>> getAllStudent() {
        List<Student> studentList = this.studentService.getAllStudent();
        List<StudentResponse> studentResponses = new ArrayList<>();
        studentList.forEach(student -> studentResponses.add(StudentService.getStudentResponse(student)));
        return ResponseEntity.ok().body(studentResponses);
    }

    @GetMapping("/{sid}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable String sid) {
        Student student = this.studentService.getStudentById(sid);
        return ResponseEntity.ok().body(StudentService.getStudentResponse(student));
    }

    @PostMapping
    public ResponseEntity<StudentResponse> getCreatedStudent(@RequestBody StudentDto studentDto) {
        Student student = this.studentService.getCreatedStudent(studentDto);
        return ResponseEntity.created(null).body(StudentService.getStudentResponse(student));
    }

    @PutMapping("/{sid}")
    public ResponseEntity<StudentResponse> getUpdatedStudent(@PathVariable String sid, @RequestBody StudentDto studentDto) {
        Student student = this.studentService.getUpdatedStudent(sid, studentDto);
        return ResponseEntity.ok().body(StudentService.getStudentResponse(student));
    }

    @DeleteMapping("/{sid}")
    public ResponseEntity<String> deleteStudent(@PathVariable String sid) {
        this.studentService.deleteStudent(sid);
        String message = String.format("Student with sid %s has been deleted.", sid);
        return ResponseEntity.ok().body(message);
    }


}
