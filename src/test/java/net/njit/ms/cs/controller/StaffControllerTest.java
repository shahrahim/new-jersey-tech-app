package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.StaffDto;
import net.njit.ms.cs.model.entity.Staff;
import net.njit.ms.cs.service.StaffService;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class StaffControllerTest {

//    @Mock
//    private StaffService staffService;
//
//    @InjectMocks
//    private StaffController staffController;
//
//    private Staff staff;
//
//    protected String name = "Jon Doe";
//    protected String address = "123 F drive";
//    protected Integer salary = 100000;
//    protected String ssn = "111-11-1111";
//
//    @BeforeEach
//    void init() {
//        staff = new Staff();
//        staff.setSsn(ssn);
//        staff.setName(name);
//        staff.setAddress(address);
//        staff.setSalary(salary);
//    }
//
//    @Test
//    public void testGetAllStaff() {
//        Mockito.when(this.staffService.getAllStaff()).thenReturn(new ArrayList<>());
//
//        ResponseEntity<List<Staff>> foundStaffList = this.staffController.getAllStaff();
//        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(foundStaffList).isNotNull();
//            softly.assertThat(foundStaffList.getBody()).isNotNull();
//            softly.assertThat(foundStaffList.getBody().size()).isEqualTo(0);
//            softly.assertThat(foundStaffList.getStatusCode()).isEqualTo(HttpStatus.OK);
//        }
//    }
//
//    @Test
//    public void testGetStaffById() {
//        Mockito.when(this.staffService.getStaffById(any())).thenReturn(staff);
//
//        ResponseEntity<Staff> foundStaff = this.staffController.getStaffById(ssn);
//        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(foundStaff).isNotNull();
//            softly.assertThat(foundStaff.getBody()).isNotNull();
//            softly.assertThat(foundStaff.getBody().getSsn()).isEqualTo(ssn);
//            softly.assertThat(foundStaff.getStatusCode()).isEqualTo(HttpStatus.OK);
//        }
//    }
//
//    @Test
//    public void testGetCreatedStaff() {
//        StaffDto staffDto = new StaffDto();
//        staffDto.setSsn(ssn);
//        staffDto.setSalary(salary);
//        staffDto.setName(name);
//        staffDto.setAddress(address);
//
//        Mockito.when(this.staffService.getCreatedStaff(any(StaffDto.class))).thenReturn(staff);
//
//        ResponseEntity<Staff> foundStaff = this.staffController.getCreatedStaff(staffDto);
//        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(foundStaff).isNotNull();
//            softly.assertThat(foundStaff.getBody()).isNotNull();
//            softly.assertThat(foundStaff.getBody().getSsn()).isEqualTo(ssn);
//            softly.assertThat(foundStaff.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        }
//    }
//
//    @Test
//    public void testGetUpdatedStaff() {
//        String newName = "newName";
//
//        StaffDto staffDto = new StaffDto();
//        staffDto.setSsn(ssn);
//        staffDto.setSalary(salary);
//        staffDto.setName(newName);
//        staffDto.setAddress(address);
//
//        staff.setName(newName);
//
//        Mockito.when(this.staffService.getUpdatedStaff(any(String.class), any(StaffDto.class))).thenReturn(staff);
//
//        ResponseEntity<Staff> foundStaff = this.staffController.getUpdatedStaff(ssn, staffDto);
//        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(foundStaff).isNotNull();
//            softly.assertThat(foundStaff.getBody()).isNotNull();
//            softly.assertThat(foundStaff.getBody().getSsn()).isEqualTo(ssn);
//            softly.assertThat(foundStaff.getBody().getName()).isEqualTo(newName);
//            softly.assertThat(foundStaff.getStatusCode()).isEqualTo(HttpStatus.OK);
//        }
//    }
//
//    @Test
//    public void testDeleteStaff() {
//
//        ResponseEntity<String> deletedStaff = this.staffController.deleteStaff(ssn);
//        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(deletedStaff).isNotNull();
//            softly.assertThat(deletedStaff.getBody()).isNotNull();
//            softly.assertThat(deletedStaff.getStatusCode()).isEqualTo(HttpStatus.OK);
//        }
//    }


}
