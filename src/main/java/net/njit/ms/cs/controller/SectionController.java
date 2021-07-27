package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.SectionDto;
import net.njit.ms.cs.model.dto.request.SectionUpdateDto;
import net.njit.ms.cs.model.dto.response.SectionResponse;
import net.njit.ms.cs.model.entity.Section;
import net.njit.ms.cs.model.entity.SectionId;
import net.njit.ms.cs.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/section")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<SectionResponse>> getAllSections() {
        List<Section> sectionList = this.sectionService.getAllSections();
        List<SectionResponse> sectionResponses = new ArrayList<>();

        sectionList.forEach(section -> {
            SectionResponse sectionResponse = SectionService.getSectionResponse(section);
            sectionResponses.add(sectionResponse);
        });
        return ResponseEntity.ok().body(sectionResponses);
    }

    @GetMapping
    public ResponseEntity<SectionResponse> getSectionById(@RequestBody SectionId sectionId) {
        Section section = this.sectionService.getSectionById(sectionId);
        return ResponseEntity.ok().body(SectionService.getSectionResponse(section));
    }

    @PostMapping
    public ResponseEntity<SectionResponse> getCreatedSection(@RequestBody SectionDto sectionDto) {
        Section section = this.sectionService.getCreatedSection(sectionDto);
        return ResponseEntity.created(null).body(SectionService.getSectionResponse(section));
    }

    @PutMapping
    public ResponseEntity<SectionResponse> getUpdatedSection(@RequestBody SectionUpdateDto sectionDto) {
        Section section = this.sectionService.getUpdatedSection(sectionDto);
        return ResponseEntity.ok().body(SectionService.getSectionResponse(section));
    }
//
//    @DeleteMapping("/{sid}")
//    public ResponseEntity<String> deleteSection(@PathVariable String sid) {
//        this.sectionService.deleteSection(sid);
//        String message = String.format("Section with sid %s has been deleted.", sid);
//        return ResponseEntity.ok().body(message);
//    }


}
