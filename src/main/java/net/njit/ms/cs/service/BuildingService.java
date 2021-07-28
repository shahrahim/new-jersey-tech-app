package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.BuildingDto;
import net.njit.ms.cs.model.dto.response.BuildingResponse;
import net.njit.ms.cs.model.dto.response.CodeDto;
import net.njit.ms.cs.model.dto.response.NumberDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.model.entity.Department;
import net.njit.ms.cs.model.entity.Room;
import net.njit.ms.cs.repository.BuildingRepository;
import org.springframework.stereotype.Service;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final StaffService staffService;
    private final StudentService studentService;

    public BuildingService(BuildingRepository buildingRepository,
                           StaffService staffService,
                           StudentService studentService) {
        this.buildingRepository = buildingRepository;
        this.staffService = staffService;
        this.studentService = studentService;
    }

    public List<Building> getAllBuildings() {
        return this.buildingRepository.findAll();
    }

    public Building getBuildingById(Integer number) {
        return this.buildingRepository.findById(number).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Building with number: %s not found", number)));
    }

    public Building getCreatedBuilding(BuildingDto buildingDto) {
        if (this.buildingRepository.existsById(buildingDto.getNumber())) {
            String message = String.format("Building with number: %s already exists.", buildingDto.getNumber());
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedBuilding(this.getNewBuilding(buildingDto));
    }

    public Building getUpdatedBuilding(Integer number, BuildingDto buildingDto) {
        Building building = this.getBuildingById(number);
        if (!number.equals(buildingDto.getNumber())) {
            String message = String.format("Building number: %s cannot be changed in update", number);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        building.setName(buildingDto.getName());
        building.setLocation(buildingDto.getLocation());
        return this.getCreateOrReplacedBuilding(building);
    }

    public void deleteBuilding(Integer number) {
        Building building = this.getBuildingById(number);
        handleFacultyForDepartmentDeletion(building);
        handleStudentsForDepartmentDeletion(building);
        try {
            this.buildingRepository.delete(building);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting building with number: %s to backend", number);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    public static BuildingResponse getBuildingResponse(Building building) {
//        @Id
//        private Integer number;
//
//        @OneToMany(mappedBy="buildingNumber", cascade = CascadeType.ALL)
//        private Set<Department> departments = new HashSet<>();
//
//        @OneToMany(mappedBy="buildingNumber", cascade = CascadeType.ALL)
//        private Set<Room> rooms = new HashSet<>();
//
//        @NotNull
//        private String name;
//
//        private String location;

        BuildingResponse buildingResponse = new BuildingResponse();
        buildingResponse.setNumber(building.getNumber());
        buildingResponse.setName(building.getName());
        buildingResponse.setLocation(building.getLocation());

        Set<CodeDto> departments = new HashSet<>();
        building.getDepartments().forEach(department -> {
            CodeDto codeDto = new CodeDto();
            codeDto.setCode(department.getCode());
            departments.add(codeDto);
        });
        buildingResponse.setDepartments(departments);

        Set<NumberDto> rooms = new HashSet<>();
        building.getRooms().forEach(room -> {
            NumberDto numberDto = new NumberDto();
            numberDto.setNumber(room.getRoomNumber());
            rooms.add(numberDto);
        });
        buildingResponse.setRooms(rooms);

        return buildingResponse;

    }

    private Building getCreateOrReplacedBuilding(Building building) {
        try {
            return this.buildingRepository.save(building);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing building with number: %s to backend %s",
                    building.getNumber(), e.getCause());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private void handleFacultyForDepartmentDeletion(Building building) {
        building.getDepartments().forEach(department -> {
            department.getFaculties().forEach(faculty -> {
                faculty.getDepartments().remove(department);
                this.staffService.getCreateOrReplacedStaff(faculty);
            });

        });
    }

    private void handleStudentsForDepartmentDeletion(Building building) {
        building.getDepartments().forEach(department -> {
            department.getStudents().forEach(student -> {
                student.setDepartmentCode(null);
                this.studentService.getCreateOrReplacedStudent(student);
            });

        });
    }


    private Building getNewBuilding(BuildingDto buildingDto) {
        Building building = new Building();
        building.setNumber(buildingDto.getNumber());
        building.setName(buildingDto.getName());
        building.setLocation(buildingDto.getLocation());
        return building;
    }


}
