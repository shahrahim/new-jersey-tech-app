package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.StaffDto;
import net.njit.ms.cs.model.entity.Staff;
import net.njit.ms.cs.repository.StaffRepository;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffService staffService;

    private Staff staff;

    private StaffDto staffDto;

    private final String ssn = "111-11-1111";

    @BeforeEach
    void init() {
        String name = "Jon Doe";
        String address = "123 F drive";
        Integer salary = 100000;

        staffDto = new StaffDto();
        staffDto.setSsn(ssn);
        staffDto.setName(name);
        staffDto.setAddress(address);
        staffDto.setSalary(salary);

        staff = new Staff();
        staff.setSsn(ssn);
        staff.setName(name);
        staff.setAddress(address);
        staff.setSalary(salary);

    }

    @Test
    public void testGetAllStaff() {
        List<Staff> staffList = Collections.singletonList(staff);

        Mockito.when(this.staffRepository.findAll()).thenReturn(staffList);

        List<Staff> foundStaffList = this.staffService.getAllStaff();
        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundStaffList).isNotNull();
            softly.assertThat(foundStaffList.size()).isEqualTo(1);
            softly.assertThat(foundStaffList.get(0).getSsn()).isEqualTo("111-11-1111");
        }
    }

    @Test
    public void testGetStaffById() {
        Mockito.when(this.staffRepository.findById(ssn)).thenReturn(Optional.of(staff));

        Staff foundStaff = this.staffService.getStaffById(ssn);
        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundStaff).isNotNull();
            softly.assertThat(foundStaff.getSsn()).isEqualTo("111-11-1111");
        }
    }

    @Test
    public void testGetStaffById_throws_ResourceNotFoundException() {
        Mockito.when(this.staffRepository.findById(ssn)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.staffService.getStaffById(ssn);
        });
    }

    @Test
    public void testGetCreatedStaff() {
        Mockito.when(this.staffRepository.existsById(ssn)).thenReturn(false);
        Mockito.when(this.staffRepository.save(any(Staff.class))).thenReturn(staff);

        Staff foundStaff = this.staffService.getCreatedStaff(staffDto);
        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundStaff).isNotNull();
            softly.assertThat(foundStaff.getSsn()).isEqualTo("111-11-1111");
        }
    }

    @Test
    public void testGetCreatedStaff_throws_BadRequestException() {
        Mockito.when(this.staffRepository.existsById(ssn)).thenReturn(true);

        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.staffService.getCreatedStaff(staffDto);
        });
    }

    @Test
    public void testGetCreatedStaff_throws_Exception() {
        Mockito.when(this.staffRepository.existsById(ssn)).thenReturn(false);
        Mockito.when(this.staffRepository.save(any(Staff.class))).thenThrow(new RuntimeException());

        Assertions.assertThrows(ResourceNotCreatedException.class, () -> {
            this.staffService.getCreatedStaff(staffDto);
        });
    }

    @Test
    public void testGetUpdatedStaff() {
        String name = "new-name";

        staffDto.setName(name);
        staffDto.setSsn(ssn);

        Staff newStaff = new Staff();
        newStaff.setSsn(ssn);
        newStaff.setName(name);

        Mockito.when(this.staffRepository.findById(ssn)).thenReturn(Optional.of(staff));
        Mockito.when(this.staffRepository.save(any(Staff.class))).thenReturn(newStaff);

        Staff foundStaff = this.staffService.getUpdatedStaff(ssn, staffDto);
        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundStaff).isNotNull();
            softly.assertThat(foundStaff.getSsn()).isEqualTo("111-11-1111");
            softly.assertThat(foundStaff.getName()).isEqualTo(name);

        }
    }

    @Test
    public void testGetUpdatedStaff_throws_BadRequestException() {
        staffDto.setSsn("");

        Mockito.when(this.staffRepository.findById(ssn)).thenReturn(Optional.of(staff));
        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.staffService.getUpdatedStaff(ssn, staffDto);
        });
    }

    @Test
    public void testDeleteStaff_throws_ResourceNotDeletedException() {
        Mockito.when(this.staffRepository.findById(ssn)).thenReturn(Optional.of(staff));

        Mockito.doThrow(RuntimeException.class).when(this.staffRepository).delete(staff);
        Assertions.assertThrows(ResourceNotDeletedException.class, () -> {
            this.staffService.deleteStaff(ssn);
        });
    }

}
