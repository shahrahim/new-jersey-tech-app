package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.CourseDto;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.model.entity.Course;
import net.njit.ms.cs.repository.DepartmentRepository;
import net.njit.ms.cs.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    public CourseService(CourseRepository courseRepository,
                         DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Course> getAllCourse() {
        return this.courseRepository.findAll();
    }

    public Course getCourseById(Integer number) {
        return this.courseRepository.findById(number).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Course with number: %s not found", number)));
    }

    public Course getCreatedCourse(CourseDto courseDto) {
        if (this.courseRepository.existsById(courseDto.getNumber())) {
            String message = String.format("Course with number: %s already exists.", courseDto.getNumber());
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedCourse(this.getNewCourse(courseDto));
    }

    public Course getUpdatedCourse(Integer number, CourseDto courseDto) {
        Course course = this.getCourseById(number);
        if (!number.equals(courseDto.getNumber()) ||
                !course.getDepartment().getCode().equals(courseDto.getDepartmentCode())) {
            String message = String.format(
                    "Course number: %s or department cannot be changed in update", number);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedCourse(this.getNewCourse(courseDto));
    }

    public void deleteCourse(Integer number) {
        try {
            this.courseRepository.delete(this.getCourseById(number));
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting course with number: %s to backend", number);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    private Course getCreateOrReplacedCourse(Course course) {
        try {
            return this.courseRepository.save(course);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing course with number: %s to backend", course.getNumber());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private Course getNewCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setNumber(courseDto.getNumber());
        course.setDepartment(this.getDepartmentForCreateRequest(courseDto.getDepartmentCode()));
        course.setName(courseDto.getName());
        course.setCredits(courseDto.getCredits());
        course.setTaHours(courseDto.getTaHours());
        return course;
    }

    private Department getDepartmentForCreateRequest(String departmentCode) {
        return this.departmentRepository.findById(departmentCode).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Department with code %s does not exist", departmentCode)));
    }
}