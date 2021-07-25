package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.TeachingAssistantDto;
import net.njit.ms.cs.model.entity.TeachingAssistant;
import net.njit.ms.cs.model.entity.TeachingAssistantId;
import net.njit.ms.cs.service.TeachingAssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachingAssistant")
public class TeachingAssistantController {

    private final TeachingAssistantService teachingAssistantService;

    public TeachingAssistantController(TeachingAssistantService teachingAssistantService) {
        this.teachingAssistantService = teachingAssistantService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<TeachingAssistant>> getAllTeachingAssistant() {
        List<TeachingAssistant> teachingAssistantList = this.teachingAssistantService.getAllTeachingAssistant();
        return ResponseEntity.ok().body(teachingAssistantList);
    }

    @GetMapping
    public ResponseEntity<TeachingAssistant> getTeachingAssistantById(
            @RequestBody TeachingAssistantId teachingAssistantId) {
        TeachingAssistant teachingAssistant = this.teachingAssistantService
                .getTeachingAssistantById(teachingAssistantId);
        return ResponseEntity.ok().body(teachingAssistant);
    }

    @PostMapping
    public ResponseEntity<TeachingAssistant> getCreatedTeachingAssistant(@RequestBody TeachingAssistantDto teachingAssistantDto) {
        return ResponseEntity.created(null).body(this.teachingAssistantService.getCreatedTeachingAssistant(teachingAssistantDto));
    }

    @PutMapping
    public ResponseEntity<TeachingAssistant> getUpdatedTeachingAssistant(@RequestBody TeachingAssistantDto teachingAssistantDto) {
        TeachingAssistant teachingAssistant = this.teachingAssistantService.getUpdatedTeachingAssistant(teachingAssistantDto);
        return ResponseEntity.ok().body(teachingAssistant);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTeachingAssistant(@RequestBody TeachingAssistantId teachingAssistantId) {
        this.teachingAssistantService.deleteTeachingAssistant(teachingAssistantId);
        String message = String.format(
                "TeachingAssistant with ssn: %s and sid: %s has been deleted.",
                teachingAssistantId.getSsn(), teachingAssistantId.getSid());
        return ResponseEntity.ok().body(message);
    }


}
