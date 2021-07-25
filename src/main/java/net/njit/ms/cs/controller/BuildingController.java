package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.BuildingDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.service.BuildingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/building")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Building>> getAllBuilding() {
        return ResponseEntity.ok().body(this.buildingService.getAllBuildings());
    }

    @GetMapping("/{number}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Integer number) {
        return ResponseEntity.ok().body(this.buildingService.getBuildingById(number));
    }

    @PostMapping
    public ResponseEntity<Building> getCreatedBuilding(@RequestBody BuildingDto buildingDto) {
        return ResponseEntity.created(null).body(this.buildingService.getCreatedBuilding(buildingDto));
    }

    @PutMapping("/{number}")
    public ResponseEntity<Building> getUpdatedBuilding(@PathVariable Integer number, @RequestBody BuildingDto buildingDto) {
        return ResponseEntity.ok().body(this.buildingService.getUpdatedBuilding(number, buildingDto));
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<String> deleteBuilding(@PathVariable Integer number) {
        this.buildingService.deleteBuilding(number);
        return ResponseEntity.ok().body(String.format("Building with number %s has been deleted.", number));
    }
}