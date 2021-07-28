package net.njit.ms.cs.controller;

import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.model.dto.request.SectionDto;
import net.njit.ms.cs.model.dto.request.SectionRoomDto;
import net.njit.ms.cs.model.dto.request.SectionUpdateDto;
import net.njit.ms.cs.model.dto.response.SectionResponse;
import net.njit.ms.cs.model.dto.response.SectionRoomResponse;
import net.njit.ms.cs.model.entity.Section;
import net.njit.ms.cs.model.entity.SectionId;
import net.njit.ms.cs.model.entity.SectionRoom;
import net.njit.ms.cs.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/room")
    public ResponseEntity<SectionRoomResponse> getCreatedSectionRoom(@RequestBody SectionRoomDto sectionRoomDto) {
        SectionRoom sectionRoom = this.sectionService.getCreatedSectionRoom(sectionRoomDto);
        return ResponseEntity.created(null).body(SectionService.getSectionRoomResponse(sectionRoom));
    }


    @PutMapping
    public ResponseEntity<SectionResponse> getUpdatedSection(@RequestBody SectionUpdateDto sectionDto) {
        Section section = this.sectionService.getUpdatedSection(sectionDto);
        return ResponseEntity.ok().body(SectionService.getSectionResponse(section));
    }

    @PutMapping("/registration/{sid}")
    public ResponseEntity<SectionResponse> getUpdatedSectionWithRegistration(@PathVariable String sid,
                                                                             @RequestParam Optional<Integer> courseNumber,
                                                                             @RequestParam Optional<String> time,
                                                                             @RequestParam Optional<Integer> sectionNumber) {
        if(courseNumber.isPresent() && time.isPresent() && sectionNumber.isPresent()) {
            throw new BadRequestRequestException("In registration, all params cannot be defined");
        }

        if(courseNumber.isPresent() && time.isPresent()) {
            Section section = this.sectionService.getStudentRegisteredSectionByCourseAndTime(sid,
                    courseNumber.get(), time.get());
            return ResponseEntity.ok().body(SectionService.getSectionResponse(section));
        }

        if(sectionNumber.isPresent()) {
            Section section = this.sectionService.getStudentRegisteredSectionBySection(sid, sectionNumber.get());
            return ResponseEntity.ok().body(SectionService.getSectionResponse(section));
        }

        throw new BadRequestRequestException("Registration is not valid");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSection(@RequestBody SectionId sectionId) {
        this.sectionService.deleteSection(sectionId);
        String message = String.format("Section with sid %s has been deleted.", sectionId);
        return ResponseEntity.ok().body(message);
    }


}
