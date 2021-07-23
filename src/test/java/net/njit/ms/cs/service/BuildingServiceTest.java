package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.BuildingDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.model.entity.Staff;
import net.njit.ms.cs.repository.BuildingRepository;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BuildingServiceTest {

    @Mock
    private BuildingRepository buildingRepository;

    @InjectMocks
    private BuildingService buildingService;

    private Building building;

    private BuildingDto buildingDto;

    private final Integer NUMBER = 123;

    @BeforeEach
    void init() {
        String name = "New Jersey Building";
        String location = "123 F drive";

        buildingDto = new BuildingDto();
        buildingDto.setNumber(NUMBER);
        buildingDto.setName(name);
        buildingDto.setLocation(location);

        building = new Building();
        building.setNumber(NUMBER);
        building.setName(name);
        building.setLocation(location);
    }

    @Test
    public void testGetAllBuilding() {
        List<Building> buildingList = Collections.singletonList(building);

        Mockito.when(this.buildingRepository.findAll()).thenReturn(buildingList);

        List<Building> foundBuildingList = this.buildingService.getAllBuildings();
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundBuildingList).isNotNull();
            softly.assertThat(foundBuildingList.size()).isEqualTo(1);
            softly.assertThat(foundBuildingList.get(0).getNumber()).isEqualTo(NUMBER);
        }
    }

    @Test
    public void testGetBuildingById() {
        Mockito.when(this.buildingRepository.findById(NUMBER)).thenReturn(Optional.of(building));

        Building foundBuilding = this.buildingService.getBuildingById(NUMBER);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundBuilding).isNotNull();
            softly.assertThat(foundBuilding.getNumber()).isEqualTo(NUMBER);
        }
    }

    @Test
    public void testGetBuildingById_throws_ResourceNotFoundException() {
        Mockito.when(this.buildingRepository.findById(NUMBER)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.buildingService.getBuildingById(NUMBER);
        });
    }

    @Test
    public void testGetCreatedBuilding() {
        Mockito.when(this.buildingRepository.existsById(NUMBER)).thenReturn(false);
        Mockito.when(this.buildingRepository.save(any(Building.class))).thenReturn(building);

        Building foundBuilding = this.buildingService.getCreatedBuilding(buildingDto);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundBuilding).isNotNull();
            softly.assertThat(foundBuilding.getNumber()).isEqualTo(NUMBER);
            softly.assertThat(foundBuilding.getLocation()).isEqualTo(buildingDto.getLocation());
        }
    }

    @Test
    public void testGetCreatedBuilding_throws_BadRequestException() {
        Mockito.when(this.buildingRepository.existsById(NUMBER)).thenReturn(true);

        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.buildingService.getCreatedBuilding(buildingDto);
        });
    }

    @Test
    public void testGetCreatedBuilding_throws_Exception() {
        Mockito.when(this.buildingRepository.existsById(NUMBER)).thenReturn(false);
        Mockito.when(this.buildingRepository.save(any(Building.class))).thenThrow(new RuntimeException());

        Assertions.assertThrows(ResourceNotCreatedException.class, () -> {
            this.buildingService.getCreatedBuilding(buildingDto);
        });
    }

    @Test
    public void testGetUpdatedBuilding() {
        String name = "new-name";

        buildingDto.setName(name);

        Building newBuilding = new Building();
        newBuilding.setNumber(NUMBER);
        newBuilding.setName(name);

        Mockito.when(this.buildingRepository.findById(NUMBER)).thenReturn(Optional.of(building));
        Mockito.when(this.buildingRepository.save(any(Building.class))).thenReturn(newBuilding);

        Building foundBuilding = this.buildingService.getUpdatedBuilding(NUMBER, buildingDto);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundBuilding).isNotNull();
            softly.assertThat(foundBuilding.getNumber()).isEqualTo(NUMBER);
            softly.assertThat(foundBuilding.getName()).isEqualTo(name);

        }
    }

    @Test
    public void testGetUpdatedBuilding_throws_BadRequestException() {
        buildingDto.setNumber(0);

        Mockito.when(this.buildingRepository.findById(NUMBER)).thenReturn(Optional.of(building));
        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.buildingService.getUpdatedBuilding(NUMBER, buildingDto);
        });
    }

    @Test
    public void testDeleteBuilding_throws_ResourceNotDeletedException() {
        Mockito.when(this.buildingRepository.findById(NUMBER)).thenReturn(Optional.of(building));

        Mockito.doThrow(RuntimeException.class).when(this.buildingRepository).delete(building);
        Assertions.assertThrows(ResourceNotDeletedException.class, () -> {
            this.buildingService.deleteBuilding(NUMBER);
        });
    }
}