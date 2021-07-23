package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.StaffDto;
import net.njit.ms.cs.model.entity.Staff;
import net.njit.ms.cs.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = this.staffService.getAllStaff();
        return ResponseEntity.ok().body(staffList);
    }

    @GetMapping("/{ssn}")
    public ResponseEntity<Staff> getStaffById(@PathVariable String ssn) {
        Staff staff = this.staffService.getStaffById(ssn);
        return ResponseEntity.ok().body(staff);
    }

    @PostMapping
    public ResponseEntity<Staff> getCreatedStaff(@RequestBody StaffDto staffDto) {
        return ResponseEntity.created(null).body(this.staffService.getCreatedStaff(staffDto));
    }

    @PutMapping("/{ssn}")
    public ResponseEntity<Staff> getUpdatedStaff(@PathVariable String ssn, @RequestBody StaffDto staffDto) {
        Staff staff = this.staffService.getUpdatedStaff(ssn, staffDto);
        return ResponseEntity.ok().body(staff);
    }

    @DeleteMapping("/{ssn}")
    public ResponseEntity<String> deleteStaff(@PathVariable String ssn) {
        this.staffService.deleteStaff(ssn);
        String message = String.format("Staff with ssn %s has been deleted.", ssn);
        return ResponseEntity.ok().body(message);
    }


}
