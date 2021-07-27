package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.StaffDto;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.model.entity.Section;
import net.njit.ms.cs.model.entity.Staff;
import net.njit.ms.cs.repository.DepartmentRepository;
import net.njit.ms.cs.repository.SectionRepository;
import net.njit.ms.cs.repository.StaffRepository;
import net.njit.ms.cs.repository.StudentRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class StaffService {

    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final SectionService sectionService;
    private final SectionRepository sectionRepository;

    private static final String TA = "TA";
    private static final String FACULTY = "Faculty";


    public StaffService(StaffRepository staffRepository,
                        StudentRepository studentRepository,
                        DepartmentRepository departmentRepository,
                        @Lazy SectionService sectionService,
                        SectionRepository sectionRepository) {
        this.staffRepository = staffRepository;
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.sectionService = sectionService;
        this.sectionRepository = sectionRepository;
    }

    public List<Staff> getAllStaff() {
        return this.staffRepository.findAll();
    }

    public Staff getStaffById(String ssn) {
        return this.staffRepository.findById(ssn).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Staff with ssn: %s not found", ssn)));
    }

    public Staff getCreatedStaff(StaffDto staffDto) {
        if (this.staffRepository.existsById(staffDto.getSsn())) {
            String message = String.format("Staff with ssn: %s already exists.", staffDto.getSsn());
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        validateStaffType(staffDto);
        return this.getCreateOrReplacedStaff(this.getNewStaff(staffDto));
    }

    public Staff getUpdatedStaff(String ssn, StaffDto staffDto) {
        this.getStaffById(ssn);
        if (!ssn.equals(staffDto.getSsn())) {
            String message = String.format("Staff ssn: %s cannot be changed in update", ssn);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedStaff(this.getNewStaff(staffDto));
    }

    public void deleteStaff(String ssn) {
        Staff staff = this.getStaffById(ssn);
        handleFacultySectionsForDeletion(staff);
        try {
            this.staffRepository.delete(staff);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting staff with ssn: %s to backend", ssn);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    public Staff getCreateOrReplacedStaff(Staff staff) {
        try {
            return this.staffRepository.save(staff);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing staff with ssn: %s to backend", staff.getSsn());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private void validateStaffType(StaffDto staffDto) {
        if(staffDto.getType() == null && !staffDto.getType().equals(TA) &&
                !staffDto.getType().equals(FACULTY)) {
            String message = String.format("Staff type must be defined %s", staffDto.getType());
            log.error(message);
            throw new BadRequestRequestException(message);
        }

        if(staffDto.getType().equals(TA)) {
            if(this.studentRepository.findAllBySsn(staffDto.getSsn()).isEmpty()) {
                String message = String.format("TA with ssn %s must have a student entry", staffDto.getSsn());
                log.error(message);
                throw new BadRequestRequestException(message);
            }
        }
    }

    private void handleFacultySectionsForDeletion(Staff staff) {
        if(staff.getType().equals(FACULTY)) {
            this.sectionRepository.findAllByFacultySsn(staff.getSsn()).forEach(section -> {
                section.setFacultySsn(null);
                this.sectionService.getCreateOrReplacedSection(section);
            });
        }
    }

    private Staff getNewStaff(StaffDto staffDto) {
        Staff staff = new Staff();
        staff.setSsn(staffDto.getSsn());
        staff.setName(staffDto.getName());
        staff.setAddress(staffDto.getAddress());
        staff.setSalary(staffDto.getSalary());
        staff.setType(staffDto.getType());
        staff.setRank(staffDto.getRank());
        staff.setCourseLoad(staffDto.getCourseLoad());
        staff.setWorkHours(staffDto.getWorkHours());
        staff.setDepartments(this.getDepartmentsForRequest(staffDto.getDepartments()));
        return staff;
    }

    private Set<Department> getDepartmentsForRequest(Set<String> departmentCodes) {
        Set<Department> departments = new HashSet<>();
        departmentCodes.forEach(departmentCode -> {
            departments.add(this.departmentRepository.findById(departmentCode).orElseThrow(
                    () -> new BadRequestRequestException(String.format("Department %s does not exist", departmentCode))
            ));
        });
        return departments;
    }

}
