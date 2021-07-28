package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.BuildingDto;
import net.njit.ms.cs.model.dto.response.BuildingResponse;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.service.BuildingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/building")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<BuildingResponse>> getAllBuilding() {
        List<Building> buildingList = this.buildingService.getAllBuildings();
        List<BuildingResponse> buildingResponses = new ArrayList<>();
        buildingList.forEach(building -> buildingResponses.add(BuildingService.getBuildingResponse(building)));
        return ResponseEntity.ok().body(buildingResponses);
    }

    @GetMapping("/{number}")
    public ResponseEntity<BuildingResponse> getBuildingById(@PathVariable Integer number) {
        return ResponseEntity.ok().body(BuildingService.getBuildingResponse(this.buildingService.getBuildingById(number)));
    }

    @PostMapping
    public ResponseEntity<BuildingResponse> getCreatedBuilding(@RequestBody BuildingDto buildingDto) {
        return ResponseEntity.created(null).body(BuildingService.getBuildingResponse(this.buildingService.getCreatedBuilding(buildingDto)));
    }

    @PutMapping("/{number}")
    public ResponseEntity<BuildingResponse> getUpdatedBuilding(@PathVariable Integer number, @RequestBody BuildingDto buildingDto) {
        return ResponseEntity.ok().body(BuildingService.getBuildingResponse(this.buildingService.getUpdatedBuilding(number, buildingDto)));
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<String> deleteBuilding(@PathVariable Integer number) {
        this.buildingService.deleteBuilding(number);
        return ResponseEntity.ok().body(String.format("Building with number %s has been deleted.", number));
    }
}