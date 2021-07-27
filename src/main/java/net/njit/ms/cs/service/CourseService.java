package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.CourseDto;
import net.njit.ms.cs.model.dto.response.CourseResponse;
import net.njit.ms.cs.model.dto.response.SectionInfo;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.model.entity.Course;
import net.njit.ms.cs.repository.DepartmentRepository;
import net.njit.ms.cs.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                !course.getDepartmentCode().equals(courseDto.getDepartmentCode())) {
            String message = String.format(
                    "Course number: %s or department cannot be changed in update", number);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedCourse(this.getNewCourse(courseDto));
    }

    public void deleteCourse(Integer number) {
        Course course = this.getCourseById(number);
        try {
            this.courseRepository.delete(course);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting course with number: %s to backend", number);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    public static CourseResponse getCourseResponse(Course course) {
        CourseResponse courseResponse = new CourseResponse();

        courseResponse.setNumber(course.getNumber());
        courseResponse.setDepartmentCode(course.getDepartmentCode());
        courseResponse.setName(course.getName());
        courseResponse.setCredits(course.getCredits());
        courseResponse.setTaHours(course.getTaHours());

        Set<SectionInfo> sections = new HashSet<>();
        course.getSections().forEach(section -> {
            SectionInfo sectionInfo = new SectionInfo();
            sectionInfo.setNumber(section.getNumber());
            sectionInfo.setCourseNumber(section.getCourseNumber());
            sections.add(sectionInfo);
        });
        courseResponse.setSections(sections);

        return courseResponse;
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
        course.setDepartmentCode(this.getDepartmentForCreateRequest(courseDto.getDepartmentCode()).getCode());
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