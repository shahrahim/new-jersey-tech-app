package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.StudentDto;
import net.njit.ms.cs.model.dto.response.SectionInfo;
import net.njit.ms.cs.model.dto.response.StudentResponse;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.model.entity.Student;
import net.njit.ms.cs.repository.DepartmentRepository;
import net.njit.ms.cs.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public StudentService(StudentRepository studentRepository,
                          DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Student> getAllStudent() {
        return this.studentRepository.findAll();
    }

    public Student getStudentById(String sid) {
        return this.studentRepository.findById(sid).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Student with sid: %s not found", sid)));
    }

    public Student getCreatedStudent(StudentDto studentDto) {
        if (this.studentRepository.existsById(studentDto.getSid())) {
            String message = String.format("Student with sid: %s already exists.", studentDto.getSid());
            log.error(message);
            throw new BadRequestRequestException(message);
        } else if (!this.studentRepository.findAllBySsn(studentDto.getSsn()).isEmpty()) {
            String message = String.format("Student with ssn: %s already exists.", studentDto.getSsn());
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedStudent(this.getNewStudent(studentDto));
    }

    public Student getUpdatedStudent(String sid, StudentDto studentDto) {
        Student student = this.getStudentById(sid);
        if (!sid.equals(studentDto.getSid()) || !student.getSsn().equals(studentDto.getSsn())) {
            String message = String.format(
                    "Student sid: %s or ssn: %s cannot be changed in update", sid, student.getSsn());
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedStudent(this.getNewStudent(studentDto));
    }

    public void deleteStudent(String ssn) {
        Student student = this.getStudentById(ssn);
        try {
            this.studentRepository.delete(student);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting student with ssn: %s to backend", ssn);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    public static StudentResponse getStudentResponse(Student student) {
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setSid(student.getSid());
        studentResponse.setSsn(student.getSsn());

        studentResponse.setDepartmentCode(student.getDepartmentCode());
        studentResponse.setName(student.getName());
        studentResponse.setAddress(student.getAddress());
        studentResponse.setYear(student.getYear());
        studentResponse.setHighSchool(student.getHighSchool());

        Set<SectionInfo> sections = new HashSet<>();
        student.getSections().forEach(section -> {
            SectionInfo sectionInfo = new SectionInfo();
            sectionInfo.setNumber(section.getNumber());
            sectionInfo.setCourseNumber(section.getCourseNumber());
            sections.add(sectionInfo);
        });
        studentResponse.setSections(sections);

        return studentResponse;
    }

    public Student getCreateOrReplacedStudent(Student student) {
        try {
            return this.studentRepository.save(student);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing student with ssn: %s to backend", student.getSsn());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private Student getNewStudent(StudentDto studentDto) {
        Student student = new Student();
        student.setSid(studentDto.getSid());
        student.setDepartmentCode(this.getDepartmentForCreateRequest(studentDto.getDepartmentCode()).getCode());
        student.setSsn(studentDto.getSsn());
        student.setName(studentDto.getName());
        student.setAddress(studentDto.getAddress());
        student.setHighSchool(studentDto.getHighSchool());
        student.setYear(studentDto.getYear());
        return student;
    }

    private Department getDepartmentForCreateRequest(String departmentCode) {
        return this.departmentRepository.findById(departmentCode).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Department with code %s does not exist", departmentCode)));
    }
}