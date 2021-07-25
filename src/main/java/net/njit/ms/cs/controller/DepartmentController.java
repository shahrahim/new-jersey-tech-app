package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.DepartmentDto;
import net.njit.ms.cs.model.dto.response.DepartmentResponse;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        List<Department> departments = this.departmentService.getAllDepartments();
        List<DepartmentResponse> departmentResponses = new ArrayList<>();
        departments.forEach(department -> {
            departmentResponses.add(DepartmentService.getDepartmentResponse(department));
        });
        return ResponseEntity.ok().body(departmentResponses);
    }

    @GetMapping("/{code}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable String code) {
        return ResponseEntity.ok().body(DepartmentService.getDepartmentResponse(this.departmentService.getDepartmentById(code)));
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> getCreatedDepartment(@RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.created(null).body(
                DepartmentService.getDepartmentResponse(this.departmentService.getCreatedDepartment(departmentDto)));
    }

    @PutMapping("/{code}")
    public ResponseEntity<DepartmentResponse> getUpdatedDepartment(@PathVariable String code, @RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok().body(DepartmentService.getDepartmentResponse(
                this.departmentService.getUpdatedDepartment(code, departmentDto)));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteDepartment(@PathVariable String code) {
        this.departmentService.deleteDepartment(code);
        return ResponseEntity.ok().body(String.format("Department with code %s has been deleted.", code));
    }

}
