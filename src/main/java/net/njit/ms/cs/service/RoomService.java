package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.RoomDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.model.entity.Room;
import net.njit.ms.cs.repository.BuildingRepository;
import net.njit.ms.cs.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Room getRoomById(Integer number) {
        return this.roomRepository.findById(number).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Room with number: %s not found", number)));
    }

    public Room getCreatedRoom(RoomDto roomDto) {
        Integer number = roomDto.getRoomNumber();

        if (this.roomRepository.existsById(number)) {
            String message = String.format("Room with number: %s already exists.", number);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedRoom(this.getNewRoom(roomDto));
    }
//
    public Room getUpdatedRoom(Integer number, RoomDto roomDto) {
        Room room = this.getRoomById(number);
        Integer buildingNumber = room.getBuilding().getNumber();
        if (!number.equals(roomDto.getRoomNumber()) || !buildingNumber.equals(roomDto.getBuildingNumber())) {
            String message = String.format(
                    "Room number: %s or Building number %s cannot be changed in update", number, buildingNumber);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedRoom(this.getNewRoom(roomDto));
    }

    public void deleteRoom(Integer number) {
        try {
            this.roomRepository.delete(this.getRoomById(number));
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting room with number: %s to backend", number);
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
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
        room.setBuilding(getValidatedBuilding(roomDto));
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