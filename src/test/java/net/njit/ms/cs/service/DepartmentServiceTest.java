package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.DepartmentDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.repository.BuildingRepository;
import net.njit.ms.cs.repository.DepartmentRepository;
import net.njit.ms.cs.repository.FacultyRepository;
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
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private BuildingRepository buildingRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    private Building building;

    private DepartmentDto departmentDto;

    private final String CODE = "CS";

    private final Integer BUILDING_NUMBER = 123;

    @BeforeEach
    void init() {
        String name = "Computer Science";
        String address = "123 F drive";

        departmentDto = new DepartmentDto();
        departmentDto.setCode(CODE);
        departmentDto.setName(name);
        departmentDto.setBuildingNumber(BUILDING_NUMBER);

        department = new Department();
        department.setCode(CODE);
        department.setName(name);

        building = new Building();
        building.setNumber(BUILDING_NUMBER);
    }

    @Test
    public void testGetAllDepartments() {
        List<Department> departmentList = Collections.singletonList(department);

        Mockito.when(this.departmentRepository.findAll()).thenReturn(departmentList);

        List<Department> foundDepartmentList = this.departmentService.getAllDepartments();
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundDepartmentList).isNotNull();
            softly.assertThat(foundDepartmentList.size()).isEqualTo(1);
            softly.assertThat(foundDepartmentList.get(0).getCode()).isEqualTo(CODE);
        }
    }

    @Test
    public void testGetDepartmentById() {
        Mockito.when(this.departmentRepository.findById(CODE)).thenReturn(Optional.of(department));

        Department foundDepartment = this.departmentService.getDepartmentById(CODE);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundDepartment).isNotNull();
            softly.assertThat(foundDepartment.getCode()).isEqualTo(CODE);
        }
    }

    @Test
    public void testGetDepartmentById_throws_ResourceNotFoundException() {
        Mockito.when(this.departmentRepository.findById(CODE)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.departmentService.getDepartmentById(CODE);
        });
    }

    @Test
    public void testGetCreatedStaff() {
        Mockito.when(this.departmentRepository.existsById(any(String.class))).thenReturn(false);
        Mockito.when(this.buildingRepository.findById(any(Integer.class))).thenReturn(Optional.of(building));
        Mockito.when(this.departmentRepository.save(any(Department.class))).thenReturn(department);

        Department foundDepartment = this.departmentService.getCreatedDepartment(departmentDto);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundDepartment).isNotNull();
            softly.assertThat(foundDepartment.getCode()).isEqualTo(CODE);
        }
    }

    @Test
    public void testGetCreatedStaff_throws_BadRequestException() {
        Mockito.when(this.departmentRepository.existsById(CODE)).thenReturn(true);

        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.departmentService.getCreatedDepartment(departmentDto);
        });
    }

    @Test
    public void testGetCreatedDepartment_throws_ResourceNotCreatedException() {
        Mockito.when(this.departmentRepository.existsById(any(String.class))).thenReturn(false);
        Mockito.when(this.buildingRepository.findById(any(Integer.class))).thenReturn(Optional.of(building));
        Mockito.when(this.departmentRepository.save(any(Department.class))).thenThrow(new RuntimeException());

        Assertions.assertThrows(ResourceNotCreatedException.class, () -> {
            this.departmentService.getCreatedDepartment(departmentDto);
        });
    }

    @Test
    public void testGetCreatedStaff_throws_BadRequestException_2() {
        departmentDto.setCode(null);
        Mockito.when(this.departmentRepository.existsById(departmentDto.getCode())).thenReturn(false);
        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.departmentService.getCreatedDepartment(departmentDto);
        });
    }

    @Test
    public void testGetCreatedStaff_throws_ResourceNotFoundException() {
        departmentDto.setBuildingNumber(123);

        Mockito.when(this.departmentRepository.existsById(departmentDto.getCode())).thenReturn(false);
        Mockito.lenient().when(this.buildingRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.departmentService.getCreatedDepartment(departmentDto);
        });
    }

    @Test
    public void testGetCreatedStaff_throws_BadRequestException_4() {
        departmentDto.setChairSsn("test-ssn");

        Mockito.when(this.departmentRepository.existsById(departmentDto.getCode())).thenReturn(false);
        Mockito.lenient().when(this.facultyRepository.existsById(any())).thenReturn(false);

        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.departmentService.getCreatedDepartment(departmentDto);
        });
    }

    @Test
    public void testGetUpdatedStaff_throws_BadRequestException_5() {
        departmentDto.setCode("12312312");

        Mockito.when(this.departmentRepository.findById(CODE)).thenReturn(Optional.of(department));

        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.departmentService.getUpdatedDepartment(CODE, departmentDto);
        });
    }

    @Test
    public void testGetUpdatedStaff() {
        String newName = "new-name";
        departmentDto.setCode(CODE);
        departmentDto.setName(newName);

        Mockito.when(this.departmentRepository.findById(any(String.class))).thenReturn(Optional.of(department));
        Mockito.when(this.buildingRepository.findById(any(Integer.class))).thenReturn(Optional.of(building));
        Mockito.when(this.departmentRepository.save(any(Department.class))).thenReturn(department);

        Department foundDepartment = this.departmentService.getUpdatedDepartment(CODE, departmentDto);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundDepartment).isNotNull();
            softly.assertThat(foundDepartment.getCode()).isEqualTo(CODE);
            softly.assertThat(foundDepartment.getName()).isEqualTo(newName);
        }
    }

    @Test
    public void testDeleteDepartment_throws_ResourceNotDeletedException() {
        Mockito.when(this.departmentRepository.findById(CODE)).thenReturn(Optional.of(department));

        Mockito.doThrow(RuntimeException.class).when(this.departmentRepository).delete(department);
        Assertions.assertThrows(ResourceNotDeletedException.class, () -> {
            this.departmentService.deleteDepartment(CODE);
        });
    }

}
