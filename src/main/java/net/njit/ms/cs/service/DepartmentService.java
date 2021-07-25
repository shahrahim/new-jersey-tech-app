package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.DepartmentDto;
import net.njit.ms.cs.model.dto.response.DepartmentResponse;
import net.njit.ms.cs.model.dto.response.NumberDto;
import net.njit.ms.cs.model.dto.response.SidDto;
import net.njit.ms.cs.model.dto.response.SsnDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.repository.BuildingRepository;
import net.njit.ms.cs.repository.DepartmentRepository;
import net.njit.ms.cs.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final BuildingRepository buildingRepository;
    private final FacultyService facultyService;

    public DepartmentService(DepartmentRepository departmentRepository,
                             FacultyRepository facultyRepository,
                             BuildingRepository buildingRepository,
                             FacultyService facultyService) {
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
        this.buildingRepository = buildingRepository;
        this.facultyService = facultyService;
    }

    public List<Department> getAllDepartments() {
        return this.departmentRepository.findAll();
    }

    public Department getDepartmentById(String code) {
        return this.departmentRepository.findById(code).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Department with code: %s not found", code)));
    }

    public Department getCreatedDepartment(DepartmentDto departmentDto) {
        if (this.departmentRepository.existsById(departmentDto.getCode())) {
            String message = String.format("Department with code: %s already exists.", departmentDto.getCode());
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        validateDepartment(departmentDto);
        return this.getCreateOrReplacedDepartment(this.getNewDepartmentForCreateRequest(departmentDto));
    }

    public Department getUpdatedDepartment(String code, DepartmentDto departmentDto) {
        Department department = this.getDepartmentById(code);

        if (!code.equals(departmentDto.getCode())) {
            String message = String.format("Department code: %s cannot be changed in update", code);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        validateDepartment(departmentDto);
        return this.getCreateOrReplacedDepartment(this.getDepartmentForRequest(department, departmentDto));
    }

    public void deleteDepartment(String code) {
        Department department = this.getDepartmentById(code);
        try {
            handleDeleteFaculty(department);
            this.departmentRepository.delete(this.getDepartmentById(code));
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting staff with code: %s to backend", code);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    public static DepartmentResponse getDepartmentResponse(Department department) {
        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setCode(department.getCode());
        departmentResponse.setChairSsn(department.getChairSsn());
        departmentResponse.setName(department.getName());

        NumberDto building = new NumberDto();
        building.setNumber(department.getBuilding().getNumber());
        departmentResponse.setBuilding(building);

        Set<SidDto> students = new HashSet<>();
        department.getStudents().forEach(student -> {
            SidDto sidDto = new SidDto();
            sidDto.setSid(student.getSid());
            students.add(sidDto);
        });
        departmentResponse.setStudents(students);

        Set<NumberDto> courses = new HashSet<>();
        department.getCourses().forEach(course -> {
            NumberDto numberDto = new NumberDto();
            numberDto.setNumber(course.getNumber());
            courses.add(numberDto);
        });
        departmentResponse.setCourses(courses);

        Set<SsnDto> faculties = new HashSet<>();
        department.getFaculties().forEach(faculty -> {
            SsnDto ssnDto = new SsnDto();
            ssnDto.setSsn(faculty.getSsn());
            faculties.add(ssnDto);
        });
        departmentResponse.setFaculties(faculties);

        return departmentResponse;

    }

    public Department getCreateOrReplacedDepartment(Department department) {
        try {
            return this.departmentRepository.save(department);
        } catch (Exception e) {
            log.error("{}",e);
            String message = String.format(
                    "Something went wrong creating or replacing department with code: %s to backend",
                    department.getCode());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private void handleDeleteFaculty(Department department) {
        department.getFaculties().forEach(faculty -> {
            Department departmentToRemove = null;
            for(Department facultyDepartment: faculty.getDepartments()) {
                if(facultyDepartment.getCode().equals(department.getCode())){
                    departmentToRemove = department;
                    break;
                }
            }
            faculty.getDepartments().remove(departmentToRemove);
            this.facultyService.getCreateOrReplacedFaculty(faculty);
        });
    }

    private void validateDepartment(DepartmentDto departmentDto) {
        String departmentCode = departmentDto.getCode();
        if (departmentCode == null || departmentCode.equals("")) {
            String message = String.format("Department code: %s is invalid", departmentCode);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        validateDepartmentChair(departmentDto.getChairSsn());
    }

    private void validateDepartmentChair(String chairSsn) {
        System.out.println("Chair" + chairSsn);
        if (chairSsn != null && !chairSsn.equals("")) {
            if (!this.facultyRepository.existsById(chairSsn)) {
                String message = String.format("Chair Ssn: %s does not exist", chairSsn);
                log.error(message);
                throw new BadRequestRequestException(message);
            }
        }
    }

    private boolean isBuildingNumberDefined(Integer buildingNumber) {
        return buildingNumber != null;
    }

    private Department getNewDepartmentForCreateRequest(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setCode(departmentDto.getCode());
        return getDepartmentForRequest(department, departmentDto);
    }

    private Department getDepartmentForRequest(Department department, DepartmentDto departmentDto) {
        Integer buildingNumber = departmentDto.getBuildingNumber();

        if(!isBuildingNumberDefined(buildingNumber)) {
            String message = String.format("Building Must be defined: %s does not exist", buildingNumber);
            log.error(message);
            throw new BadRequestRequestException(message);
        } else {
            Building building = this.buildingRepository.findById(buildingNumber).orElseThrow(()  ->
                    new ResourceNotFoundException(String.format("Building number %s does not exist", buildingNumber)));
            department.setBuilding(building);
        }
        department.setChairSsn(departmentDto.getChairSsn());
        department.setName(departmentDto.getName());
        return department;
    }


}