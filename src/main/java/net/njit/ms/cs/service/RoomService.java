package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.RoomDto;
import net.njit.ms.cs.model.dto.response.RoomResponse;
import net.njit.ms.cs.model.dto.response.RoomSectionInfo;
import net.njit.ms.cs.model.dto.response.SectionInfo;
import net.njit.ms.cs.model.entity.*;
import net.njit.ms.cs.repository.BuildingRepository;
import net.njit.ms.cs.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class RoomService {

    private RoomRepository roomRepository;

    private BuildingRepository buildingRepository;

    public RoomService(RoomRepository roomRepository,
                       BuildingRepository buildingRepository) {
        this.roomRepository = roomRepository;
        this.buildingRepository = buildingRepository;
    }

    public List<Room> getAllRooms() {
        return this.roomRepository.findAll();
    }

    public Room getRoomById(RoomId roomId) {
        return this.roomRepository.findById(roomId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Room with number: %s not found", roomId.getRoomNumber())));
    }

    public Room getCreatedRoom(RoomDto roomDto) {
        Integer number = roomDto.getRoomNumber();
        Integer buildingNumber = roomDto.getBuildingNumber();

        RoomId roomId = new RoomId(number, buildingNumber);

        if (this.roomRepository.existsById(roomId)) {
            String message = String.format("Room with number: %s already exists.", number);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedRoom(this.getNewRoom(roomDto));
    }
//
    public Room getUpdatedRoom(RoomDto roomDto) {
        RoomId roomId = new RoomId();
        roomId.setRoomNumber(roomDto.getRoomNumber());
        roomId.setBuildingNumber(roomDto.getBuildingNumber());
        Room room = this.getRoomById(roomId);
        room.setCapacity(roomDto.getCapacity());
        room.setAudioVisual(roomDto.getAudioVisual());
        return this.getCreateOrReplacedRoom(room);
    }

    public void deleteRoom(RoomId roomId) {
        Room room = this.getRoomById(roomId);
        try {
            this.roomRepository.delete(room);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting room with number: %s to backend", room.getRoomNumber());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    public static RoomResponse getRoomResponse(Room room) {
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setRoomNumber(room.getRoomNumber());
        roomResponse.setCapacity(room.getCapacity());
        roomResponse.setBuildingNumber(room.getBuildingNumber());
        roomResponse.setAudioVisual(room.getAudioVisual());

        Set<RoomSectionInfo> sections = new HashSet<>();
        room.getSectionRooms().forEach(section -> {
            RoomSectionInfo sectionInfo = new RoomSectionInfo();
            sectionInfo.setNumber(section.getSection().getNumber());
            sectionInfo.setCourseNumber(section.getSection().getCourseNumber());
            sectionInfo.setWeekday(section.getWeekday());
            sectionInfo.setTime(section.getTime());
            sections.add(sectionInfo);
        });
        roomResponse.setSections(sections);

        return roomResponse;
    }

    private Room getCreateOrReplacedRoom(Room room) {
        try {
            return this.roomRepository.save(room);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing room with number: %s to backend", room.getRoomNumber());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private Room getNewRoom(RoomDto roomDto) {
        Room room = new Room();
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setBuildingNumber(getValidatedBuilding(roomDto).getNumber());
        room.setCapacity(roomDto.getCapacity());
        room.setAudioVisual(roomDto.getAudioVisual());
        return room;
    }

    private Building getValidatedBuilding(RoomDto roomDto) {
        Integer buildingNumber = roomDto.getBuildingNumber();
        return this.buildingRepository.findById(buildingNumber).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format("Building with number %s does not exist", buildingNumber)));
    }
}