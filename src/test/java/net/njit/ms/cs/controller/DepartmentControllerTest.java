package net.njit.ms.cs.controller;


import net.njit.ms.cs.model.dto.request.DepartmentDto;
import net.njit.ms.cs.model.dto.response.DepartmentResponse;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.service.DepartmentService;
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
public class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private Department department;

    protected String code = "CS";
    protected String name = "Computer Science";

    @BeforeEach
    void init() {
        department = new Department();
        department.setCode(code);
        department.setName(name);
    }

    @Test
    public void testGetAllDepartments() {
        Mockito.when(this.departmentService.getAllDepartments()).thenReturn(new ArrayList<>());

        ResponseEntity<List<DepartmentResponse>> foundDepartmentList = this.departmentController.getAllDepartments();
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundDepartmentList).isNotNull();
            softly.assertThat(foundDepartmentList.getBody()).isNotNull();
            softly.assertThat(foundDepartmentList.getBody().size()).isEqualTo(0);
            softly.assertThat(foundDepartmentList.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Test
    public void testGetDepartmentById() {
        Mockito.when(this.departmentService.getDepartmentById(any())).thenReturn(department);

        ResponseEntity<DepartmentResponse> foundDepartment = this.departmentController.getDepartmentById(code);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundDepartment).isNotNull();
            softly.assertThat(foundDepartment.getBody()).isNotNull();
            softly.assertThat(foundDepartment.getBody().getCode()).isEqualTo(code);
            softly.assertThat(foundDepartment.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Test
    public void testGetCreatedDepartment() {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setCode(code);
        departmentDto.setName(name);

        Mockito.when(this.departmentService.getCreatedDepartment(any(DepartmentDto.class))).thenReturn(department);

        ResponseEntity<DepartmentResponse> foundDepartment = this.departmentController.getCreatedDepartment(departmentDto);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundDepartment).isNotNull();
            softly.assertThat(foundDepartment.getBody()).isNotNull();
            softly.assertThat(foundDepartment.getBody().getCode()).isEqualTo(code);
            softly.assertThat(foundDepartment.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }
    }

    @Test
    public void testGetUpdatedDepartment() {
        String newName = "newName";

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setCode(code);
        departmentDto.setName(newName);

        department.setName(newName);

        Mockito.when(this.departmentService.getUpdatedDepartment(any(String.class), any(DepartmentDto.class))).thenReturn(department);

        ResponseEntity<DepartmentResponse> foundDepartment = this.departmentController.getUpdatedDepartment(code, departmentDto);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundDepartment).isNotNull();
            softly.assertThat(foundDepartment.getBody()).isNotNull();
            softly.assertThat(foundDepartment.getBody().getCode()).isEqualTo(code);
            softly.assertThat(foundDepartment.getBody().getName()).isEqualTo(newName);
            softly.assertThat(foundDepartment.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Test
    public void testDeleteDepartment() {
        ResponseEntity<String> deletedDepartment = this.departmentController.deleteDepartment(code);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(deletedDepartment).isNotNull();
            softly.assertThat(deletedDepartment.getBody()).isNotNull();
            softly.assertThat(deletedDepartment.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

}
