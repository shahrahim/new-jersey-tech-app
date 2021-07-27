package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.StaffDto;
import net.njit.ms.cs.model.dto.response.StaffResponse;
import net.njit.ms.cs.model.entity.Staff;
import net.njit.ms.cs.repository.StaffRepository;
import net.njit.ms.cs.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        List<Staff> staffList = this.staffService.getAllStaff();
        List<StaffResponse> staffResponses = new ArrayList<>();
        staffList.forEach(staff -> staffResponses.add(StaffService.getStaffResponse(staff)));
        return ResponseEntity.ok().body(staffResponses);
    }

    @GetMapping("/{ssn}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable String ssn) {
        Staff staff = this.staffService.getStaffById(ssn);
        return ResponseEntity.ok().body(StaffService.getStaffResponse(staff));
    }

    @PostMapping
    public ResponseEntity<StaffResponse> getCreatedStaff(@RequestBody StaffDto staffDto) {
        Staff staff = this.staffService.getCreatedStaff(staffDto);
        return ResponseEntity.created(null).body(StaffService.getStaffResponse(staff));
    }

    @PutMapping("/{ssn}")
    public ResponseEntity<StaffResponse> getUpdatedStaff(@PathVariable String ssn, @RequestBody StaffDto staffDto) {
        Staff staff = this.staffService.getUpdatedStaff(ssn, staffDto);
        return ResponseEntity.ok().body(StaffService.getStaffResponse(staff));
    }

    @DeleteMapping("/{ssn}")
    public ResponseEntity<String> deleteStaff(@PathVariable String ssn) {
        this.staffService.deleteStaff(ssn);
        String message = String.format("Staff with ssn %s has been deleted.", ssn);
        return ResponseEntity.ok().body(message);
    }


}
