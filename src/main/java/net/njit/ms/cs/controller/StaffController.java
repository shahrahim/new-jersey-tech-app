package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.ClientDto;
import net.njit.ms.cs.model.dto.StaffDto;
import net.njit.ms.cs.model.entity.Client;
import net.njit.ms.cs.model.entity.Staff;
import net.njit.ms.cs.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok().body(this.staffService.getAllStaff());
    }

    @GetMapping("/{ssn}")
    public ResponseEntity<Staff> getClient(@PathVariable String ssn) {
        return ResponseEntity.ok().body(this.staffService.getStaffById(ssn));
    }

    @PostMapping
    public ResponseEntity createStaff(@RequestBody StaffDto staffDto) throws URISyntaxException {
        return ResponseEntity.accepted().body(this.staffService.getCreatedStaff(staffDto));
    }

    @PutMapping("/{ssn}")
    public ResponseEntity updateStaff(@PathVariable String ssn, @RequestBody StaffDto staffDto) {
        return ResponseEntity.ok().body(this.staffService.getUpdatedStaff(ssn, staffDto));
    }

    @DeleteMapping("/{ssn}")
    public ResponseEntity deleteClient(@PathVariable String ssn) {
        this.staffService.deleteStaff(ssn);
        return ResponseEntity.ok().body(String.format("Staff with ssn %s has been deleted.", ssn));
    }


}
