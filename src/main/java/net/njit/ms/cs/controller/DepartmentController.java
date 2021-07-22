package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.DepartmentDto;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok().body(this.departmentService.getAllDepartments());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable String code) {
        return ResponseEntity.ok().body(this.departmentService.getDepartmentById(code));
    }

    @PostMapping
    public ResponseEntity createDepartment(@RequestBody DepartmentDto departmentDto) throws URISyntaxException {
        return ResponseEntity.accepted().body(this.departmentService.getCreatedDepartment(departmentDto));
    }

    @PutMapping("/{code}")
    public ResponseEntity updateDepartment(@PathVariable String code, @RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok().body(this.departmentService.getUpdatedDepartment(code, departmentDto));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity deleteDepartment(@PathVariable String code) {
        this.departmentService.deleteDepartment(code);
        return ResponseEntity.ok().body(String.format("Department with code %s has been deleted.", code));
    }

}
