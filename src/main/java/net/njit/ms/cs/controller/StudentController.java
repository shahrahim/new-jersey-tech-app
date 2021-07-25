package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.StudentDto;
import net.njit.ms.cs.model.entity.Student;
import net.njit.ms.cs.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Student>> getAllStudent() {
        List<Student> studentList = this.studentService.getAllStudent();
        return ResponseEntity.ok().body(studentList);
    }

    @GetMapping("/{sid}")
    public ResponseEntity<Student> getStudentById(@PathVariable String sid) {
        Student student = this.studentService.getStudentById(sid);
        return ResponseEntity.ok().body(student);
    }

    @PostMapping
    public ResponseEntity<Student> getCreatedStudent(@RequestBody StudentDto studentDto) {
        return ResponseEntity.created(null).body(this.studentService.getCreatedStudent(studentDto));
    }

    @PutMapping("/{sid}")
    public ResponseEntity<Student> getUpdatedStudent(@PathVariable String sid, @RequestBody StudentDto studentDto) {
        Student student = this.studentService.getUpdatedStudent(sid, studentDto);
        return ResponseEntity.ok().body(student);
    }

    @DeleteMapping("/{sid}")
    public ResponseEntity<String> deleteStudent(@PathVariable String sid) {
        this.studentService.deleteStudent(sid);
        String message = String.format("Student with sid %s has been deleted.", sid);
        return ResponseEntity.ok().body(message);
    }


}
