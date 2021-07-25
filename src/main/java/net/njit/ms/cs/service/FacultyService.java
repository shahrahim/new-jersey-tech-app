package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.FacultyDto;
import net.njit.ms.cs.model.dto.request.FacultyUpdateDto;
import net.njit.ms.cs.model.dto.response.*;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.model.entity.Faculty;
import net.njit.ms.cs.repository.DepartmentRepository;
import net.njit.ms.cs.repository.FacultyRepository;
import net.njit.ms.cs.repository.StaffRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FacultyService {

    private final FacultyRepository facultyRepository;

    private final StaffRepository staffRepository;

    private final DepartmentRepository departmentRepository;

    private final DepartmentService departmentService;


    public FacultyService(FacultyRepository facultyRepository,
                          StaffRepository staffRepository,
                          DepartmentRepository departmentRepository,
                          @Lazy DepartmentService departmentService) {
        this.facultyRepository = facultyRepository;
        this.staffRepository = staffRepository;
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
    }

    public List<Faculty> getAllFaculty() {
        return this.facultyRepository.findAll();
    }

    public Faculty getFacultyById(String ssn) {
        return this.facultyRepository.findById(ssn).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Faculty with ssn: %s not found", ssn)));
    }

    public Faculty getCreatedFaculty(FacultyDto facultyDto) {
        String ssn = facultyDto.getSsn();

        if (this.facultyRepository.existsById(ssn)) {
            String message = String.format("Faculty with ssn: %s already exists.", ssn);
            log.error(message);
            throw new BadRequestRequestException(message);
        } else if (!this.staffRepository.existsById(ssn)) {
            String message = String.format("Staff with ssn: %s must exist before creating Faculty.", ssn);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedFaculty(this.getNewFacultyForCreateRequest(facultyDto));
    }

    public Faculty getUpdatedFaculty(String ssn, FacultyUpdateDto facultyDto) {
        Faculty faculty = this.getFacultyById(ssn);
        if (!ssn.equals(facultyDto.getSsn())) {
            String message = String.format("Faculty ssn: %s cannot be changed in update", ssn);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        handleFacultyUpdate(faculty, facultyDto);
        faculty.setRank(facultyDto.getRank());
        faculty.setCourseLoad(facultyDto.getCourseLoad());
        return this.getCreateOrReplacedFaculty(faculty);
    }

    public void deleteFaculty(String ssn) {
        Faculty faculty = this.getFacultyById(ssn);
        try {
            handleDeleteDepartment(faculty);
            this.facultyRepository.delete(this.getFacultyById(ssn));
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting faculty with ssn: %s to backend", ssn);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    public static FacultyResponse getFacultyResponse(Faculty faculty) {
        FacultyResponse facultyResponse = new FacultyResponse();
        facultyResponse.setSsn(faculty.getSsn());
        facultyResponse.setRank(faculty.getRank());
        facultyResponse.setCourseLoad(faculty.getCourseLoad());


        Set<NumberDto> sections = new HashSet<>();
        faculty.getSections().forEach(section -> {
            NumberDto numberDto = new NumberDto();
            numberDto.setNumber(section.getNumber());
            sections.add(numberDto);
        });
        facultyResponse.setSections(sections);

        Set<CodeDto> departments = new HashSet<>();
        faculty.getDepartments().forEach(department -> {
            CodeDto codeDto = new CodeDto();
            codeDto.setCode(department.getCode());
            departments.add(codeDto);
        });
        facultyResponse.setDepartments(departments);

        return facultyResponse;
    }

    private void handleDeleteDepartment(Faculty faculty) {
        faculty.getDepartments().forEach(department -> {
            for(Faculty departmentFaculty: department.getFaculties()) {
                if(departmentFaculty.getSsn().equals(faculty.getSsn())){
                    department.getFaculties().remove(faculty);
                    break;
                }
            }
            this.departmentService.getCreateOrReplacedDepartment(department);
        });
    }

    public Faculty getCreateOrReplacedFaculty(Faculty faculty) {
        try {
            return this.facultyRepository.save(faculty);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing faculty with ssn: %s to backend", faculty.getSsn());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private void handleFacultyUpdate(Faculty faculty, FacultyUpdateDto facultyDto) {
        handleFacultyRemoval(faculty, facultyDto);
        handleFacultyAdd(faculty, facultyDto);
    }

    private void handleFacultyRemoval(Faculty faculty, FacultyUpdateDto facultyDto) {
        List<Department> departmentsToRemove = new ArrayList<>();
        faculty.getDepartments().forEach(department -> {
            if(facultyDto.getDepartmentsToRemove().contains(department.getCode())) {
                departmentsToRemove.add(department);
            }
        });
        departmentsToRemove.forEach(department -> faculty.getDepartments().remove(department));
        departmentsToRemove.forEach(department -> {
            Department departmentToUpdate = this.departmentService.getDepartmentById(department.getCode());
            departmentToUpdate.getFaculties().removeIf(departmentFaculty ->
                    departmentFaculty.getSsn().equals(faculty.getSsn()));
        });
    }

    private void handleFacultyAdd(Faculty faculty, FacultyUpdateDto facultyDto) {
        Set<String> currentDepartmentSet = new HashSet<>();
        Set<String> duplicateDepartments = new HashSet<>();

        faculty.getDepartments().forEach(department -> currentDepartmentSet.add(department.getCode()));
        facultyDto.getDepartmentsToAdd().forEach(departmentToAdd -> {
            if(currentDepartmentSet.contains(departmentToAdd)) {
                duplicateDepartments.add(departmentToAdd);
            }
        });
        duplicateDepartments.forEach(duplicate -> facultyDto.getDepartmentsToAdd().remove(duplicate));
        facultyDto.getDepartmentsToAdd().forEach(departmentCode -> {
            faculty.getDepartments().add(this.departmentService.getDepartmentById(departmentCode));
        });
    }

    private Faculty getNewFacultyForCreateRequest(FacultyDto facultyDto) {
        Faculty faculty = new Faculty();
        faculty.setSsn(facultyDto.getSsn());
        faculty.setRank(facultyDto.getRank());
        faculty.setCourseLoad(facultyDto.getCourseLoad());
        faculty.setDepartments(getDepartmentSet(facultyDto.getDepartments()));
        return faculty;
    }

    private Set<Department> getDepartmentSet(Set<String> departments) {
        Set<Department> departmentSet = new HashSet<>();
        if(departments != null) {
            departments.forEach(departmentCode -> {
                departmentSet.add(this.departmentService.getDepartmentById(departmentCode));
            });
        }
        return departmentSet;
    }
}