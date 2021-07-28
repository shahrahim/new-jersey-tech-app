package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.BuildingDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.service.BuildingService;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class BuildingControllerTest {
//
//    @Mock
//    private BuildingService buildingService;
//
//    @InjectMocks
//    private BuildingController buildingController;
//
//    private Building building;
//
//    private final Integer NUMBER = 123;
//
//    String name = "New Jersey Building";
//    String location = "123 F drive";
//
//    @BeforeEach
//    void init() {
//        building = new Building();
//        building.setNumber(NUMBER);
//        building.setName(name);
//        building.setLocation(location);
//    }
//
//    @Test
//    public void testGetAllBuilding() {
//        Mockito.when(this.buildingService.getAllBuildings()).thenReturn(new ArrayList<>());
//
//        ResponseEntity<List<Building>> foundBuildingList = this.buildingController.getAllBuilding();
//        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(foundBuildingList).isNotNull();
//            softly.assertThat(foundBuildingList.getBody()).isNotNull();
//            softly.assertThat(foundBuildingList.getBody().size()).isEqualTo(0);
//            softly.assertThat(foundBuildingList.getStatusCode()).isEqualTo(HttpStatus.OK);
//        }
//    }
//
//    @Test
//    public void testGetBuildingById() {
//        Mockito.when(this.buildingService.getBuildingById(any(Integer.class))).thenReturn(building);
//
//        ResponseEntity<Building> foundBuilding = this.buildingController.getBuildingById(NUMBER);
//        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(foundBuilding).isNotNull();
//            softly.assertThat(foundBuilding.getBody()).isNotNull();
//            softly.assertThat(foundBuilding.getBody().getNumber()).isEqualTo(NUMBER);
//            softly.assertThat(foundBuilding.getStatusCode()).isEqualTo(HttpStatus.OK);
//        }
//    }
//
//    @Test
//    public void testGetCreatedBuilding() {
//        BuildingDto buildingDto = new BuildingDto();
//        buildingDto.setNumber(NUMBER);
//        buildingDto.setName(name);
//        buildingDto.setLocation(location);
//
//        Mockito.when(this.buildingService.getCreatedBuilding(any(BuildingDto.class))).thenReturn(building);
//
//        ResponseEntity<Building> foundBuilding = this.buildingController.getCreatedBuilding(buildingDto);
//        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(foundBuilding).isNotNull();
//            softly.assertThat(foundBuilding.getBody()).isNotNull();
//            softly.assertThat(foundBuilding.getBody().getNumber()).isEqualTo(NUMBER);
//            softly.assertThat(foundBuilding.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        }
//    }
//
//    @Test
//    public void testGetUpdatedBuilding() {
//        String newName = "newName";
//
//        BuildingDto buildingDto = new BuildingDto();
//        buildingDto.setNumber(NUMBER);
//        buildingDto.setName(newName);
//        buildingDto.setLocation(location);
//
//        building.setName(newName);
//
//        Mockito.when(this.buildingService.getUpdatedBuilding(any(Integer.class), any(BuildingDto.class)))
//                .thenReturn(building);
//
//        ResponseEntity<Building> foundBuilding = this.buildingController.getUpdatedBuilding(NUMBER, buildingDto);
//        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(foundBuilding).isNotNull();
//            softly.assertThat(foundBuilding.getBody()).isNotNull();
//            softly.assertThat(foundBuilding.getBody().getNumber()).isEqualTo(NUMBER);
//            softly.assertThat(foundBuilding.getBody().getName()).isEqualTo(newName);
//            softly.assertThat(foundBuilding.getStatusCode()).isEqualTo(HttpStatus.OK);
//        }
//    }
//
//    @Test
//    public void testDeleteStaff() {
//        ResponseEntity<String> deletedBuilding = this.buildingController.deleteBuilding(NUMBER);
//        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(deletedBuilding).isNotNull();
//            softly.assertThat(deletedBuilding.getBody()).isNotNull();
//            softly.assertThat(deletedBuilding.getStatusCode()).isEqualTo(HttpStatus.OK);
//        }
//    }
}