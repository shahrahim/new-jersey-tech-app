package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.FacultyDto;
import net.njit.ms.cs.model.dto.request.FacultyUpdateDto;
import net.njit.ms.cs.model.dto.response.FacultyResponse;
import net.njit.ms.cs.model.entity.Faculty;
import net.njit.ms.cs.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<FacultyResponse>> getAllFaculty() {
        List<Faculty> facultyList = this.facultyService.getAllFaculty();
        List<FacultyResponse> facultyResponses = new ArrayList<>();

        facultyList.forEach(faculty -> {
            facultyResponses.add(FacultyService.getFacultyResponse(faculty));
        });
        return ResponseEntity.ok().body(facultyResponses);
    }

    @GetMapping("/{ssn}")
    public ResponseEntity<FacultyResponse> getFacultyById(@PathVariable String ssn) {
        Faculty faculty = this.facultyService.getFacultyById(ssn);
        return ResponseEntity.ok().body(FacultyService.getFacultyResponse(faculty));
    }

    @PostMapping
    public ResponseEntity<FacultyResponse> getCreatedFaculty(@RequestBody FacultyDto facultyDto) {
        return ResponseEntity.created(null)
                .body(FacultyService.getFacultyResponse(this.facultyService.getCreatedFaculty(facultyDto)));
    }

    @PutMapping("/{ssn}")
    public ResponseEntity<FacultyResponse> getUpdatedFaculty(@PathVariable String ssn, @RequestBody FacultyUpdateDto facultyDto) {
        Faculty faculty = this.facultyService.getUpdatedFaculty(ssn, facultyDto);
        return ResponseEntity.ok().body(FacultyService.getFacultyResponse(faculty));
    }

    @DeleteMapping("/{ssn}")
    public ResponseEntity<String> deleteFaculty(@PathVariable String ssn) {
        this.facultyService.deleteFaculty(ssn);
        String message = String.format("Faculty with ssn %s has been deleted.", ssn);
        return ResponseEntity.ok().body(message);
    }
}
