package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.DepartmentDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.repository.BuildingRepository;
import net.njit.ms.cs.repository.DepartmentRepository;
import net.njit.ms.cs.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final BuildingRepository buildingRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
                             FacultyRepository facultyRepository,
                             BuildingRepository buildingRepository) {
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
        this.buildingRepository = buildingRepository;
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
        try {
            this.departmentRepository.delete(this.getDepartmentById(code));
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting staff with code: %s to backend", code);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private Department getCreateOrReplacedDepartment(Department department) {
        try {
            return this.departmentRepository.save(department);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing department with code: %s to backend",
                    department.getCode());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private void validateDepartment(DepartmentDto departmentDto) {
        String departmentCode = departmentDto.getCode();
        if (departmentCode == null || departmentCode.equals("")) {
            String message = String.format("Department code: %s is invalid", departmentCode);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        validateDepartmentChair(departmentDto.getChairSsn());
        validateDepartmentBuilding(departmentDto.getBuildingNumber());
    }

    private void validateDepartmentChair(String chairSsn) {
        if (chairSsn != null && !chairSsn.equals("")) {
            if (!this.facultyRepository.existsById(chairSsn)) {
                String message = String.format("Chair Ssn: %s does not exist", chairSsn);
                log.error(message);
                throw new BadRequestRequestException(message);
            }
        }
    }

    private void validateDepartmentBuilding(Long buildingNumber) {
        if (isBuildingNumberDefined(buildingNumber)) {
            if (!this.buildingRepository.existsById(buildingNumber)) {
                String message = String.format("Building number: %s does not exist", buildingNumber);
                log.error(message);
                throw new BadRequestRequestException(message);
            }
        }
    }

    private boolean isBuildingNumberDefined(Long buildingNumber) {
        return buildingNumber != null;
    }

    private Department getNewDepartmentForCreateRequest(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setCode(departmentDto.getCode());
        return getDepartmentForRequest(department, departmentDto);
    }

    private Department getDepartmentForRequest(Department department, DepartmentDto departmentDto) {
        Long buildingNumber = departmentDto.getBuildingNumber();

        if(isBuildingNumberDefined(departmentDto.getBuildingNumber())) {
            Building building = this.buildingRepository.findById(buildingNumber).orElseThrow(()  ->
                    new ResourceNotFoundException(String.format("Building number %s does not exist", buildingNumber)));
            department.setBuilding(building);
        }
        department.setChairSsn(departmentDto.getChairSsn());
        department.setName(departmentDto.getName());
        return department;
    }

}