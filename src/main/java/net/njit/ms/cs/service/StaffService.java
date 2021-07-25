package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.StaffDto;
import net.njit.ms.cs.model.entity.Staff;
import net.njit.ms.cs.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StaffService {

    private StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
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
        try {
            this.staffRepository.delete(staff);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting staff with ssn: %s to backend", ssn);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    private Staff getCreateOrReplacedStaff(Staff staff) {
        try {
            return this.staffRepository.save(staff);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing staff with ssn: %s to backend", staff.getSsn());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private Staff getNewStaff(StaffDto staffDto) {
        Staff staff = new Staff();
        staff.setSsn(staffDto.getSsn());
        staff.setName(staffDto.getName());
        staff.setAddress(staffDto.getAddress());
        staff.setSalary(staffDto.getSalary());
        return staff;
    }

}
