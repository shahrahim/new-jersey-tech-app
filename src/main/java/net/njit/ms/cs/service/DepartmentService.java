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
import net.njit.ms.cs.model.entity.Staff;
import net.njit.ms.cs.repository.BuildingRepository;
import net.njit.ms.cs.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final BuildingRepository buildingRepository;
    private final StaffService staffService;
    private final StudentService studentService;

    private static final String FACULTY = "Faculty";

    public DepartmentService(DepartmentRepository departmentRepository,
                             BuildingRepository buildingRepository,
                             StaffService staffService,
                             StudentService studentService) {
        this.departmentRepository = departmentRepository;
        this.buildingRepository = buildingRepository;
        this.staffService = staffService;
        this.studentService = studentService;
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = this.departmentRepository.findAll();
        Set<Staff> nonFaculties = new HashSet<>();
        departments.forEach(department -> {
            department.getFaculties().forEach(faculty -> {
                if(!faculty.getType().equals(FACULTY)) {
//                    department.getFaculties().remove(faculty);
                    nonFaculties.add(faculty);
                }
            });
        });
        nonFaculties.forEach(nonFaculty -> departments.forEach(department -> department.getFaculties().remove(nonFaculty)));
        return departments;
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
        return this.getCreatedOrReplacedDepartment(this.getNewDepartmentForCreateRequest(departmentDto));
    }

    public Department getUpdatedDepartment(String code, DepartmentDto departmentDto) {
        Department department = this.getDepartmentById(code);

        if (!code.equals(departmentDto.getCode())) {
            String message = String.format("Department code: %s cannot be changed in update", code);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        validateDepartment(departmentDto);
        return this.getCreatedOrReplacedDepartment(this.getDepartmentForRequest(department, departmentDto));
    }

    public void deleteDepartment(String code) {
        Department department = this.getDepartmentById(code);
        handleFacultyForDepartmentDeletion(department);
        handleStudentsForDepartmentDeletion(department);
        try {
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
        departmentResponse.setBuildingNumber(department.getBuildingNumber());


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

    public Department getCreatedOrReplacedDepartment(Department department) {
        try {
            return this.departmentRepository.save(department);
        } catch (Exception e) {
            log.error("{}", e);
            String message = String.format(
                    "Something went wrong creating or replacing department with code: %s to backend",
                    department.getCode());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private void handleFacultyForDepartmentDeletion(Department department) {
        department.getFaculties().forEach(faculty -> {
            faculty.getDepartments().forEach(departmentToRemove -> {
                faculty.getDepartments().remove(departmentToRemove);
                this.staffService.getCreateOrReplacedStaff(faculty);
            });

        });
    }

    private void handleStudentsForDepartmentDeletion(Department department) {
        department.getStudents().forEach(student -> {
            student.setDepartmentCode(null);
            this.studentService.getCreateOrReplacedStudent(student);
        });
    }

    private void validateDepartment(DepartmentDto departmentDto) {
        String departmentCode = departmentDto.getCode();
        if (departmentCode == null || departmentCode.equals("")) {
            String message = String.format("Department code: %s is invalid", departmentCode);
            log.error(message);
            throw new BadRequestRequestException(message);
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

        if(isBuildingNumberDefined(buildingNumber)) {
            Building building = this.buildingRepository.findById(buildingNumber).orElseThrow(()  ->
                    new ResourceNotFoundException(String.format("Building number %s does not exist", buildingNumber)));
            department.setBuildingNumber(building.getNumber());
        }
        department.setChairSsn(this.getChairPersonForRequest(departmentDto.getChairSsn()));
        department.setName(departmentDto.getName());
        return department;
    }

    private String getChairPersonForRequest(String chairSsn) {
        if (chairSsn != null && !chairSsn.equals("")) {
            Staff staff = this.staffService.getStaffById(chairSsn);
            if (staff.getType() != FACULTY) {
                String message = String.format("Chair Ssn: %s id not of type %s", chairSsn, FACULTY);
                log.error(message);
                throw new BadRequestRequestException(message);
            }
            return staff.getSsn();
        }
        return null;
    }


}