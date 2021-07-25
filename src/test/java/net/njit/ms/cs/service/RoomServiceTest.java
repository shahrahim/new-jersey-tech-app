package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.RoomDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.model.entity.Room;
import net.njit.ms.cs.repository.BuildingRepository;
import net.njit.ms.cs.repository.RoomRepository;
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
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BuildingRepository buildingRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;

    private RoomDto roomDto;

    private Building building;

    private final Integer ROOM_NUMBER = 1;

    private final Integer BUILDING_NUMBER = 123;

    Integer CAPACITY = 100;
    String AUDIO_VISUAL = "audioVisual";

    @BeforeEach
    void init() {
        roomDto = new RoomDto();
        roomDto.setRoomNumber(ROOM_NUMBER);
        roomDto.setBuildingNumber(BUILDING_NUMBER);
        roomDto.setCapacity(CAPACITY);
        roomDto.setAudioVisual(AUDIO_VISUAL);

        building = new Building();
        building.setNumber(BUILDING_NUMBER);

        room = new Room();
        room.setRoomNumber(ROOM_NUMBER);
        room.setBuilding(building);
        room.setCapacity(CAPACITY);
        room.setAudioVisual(AUDIO_VISUAL);
    }

    @Test
    public void testGetAllRoom() {
        List<Room> roomList = Collections.singletonList(room);

        Mockito.when(this.roomRepository.findAll()).thenReturn(roomList);

        List<Room> foundRoomList = this.roomService.getAllRooms();
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundRoomList).isNotNull();
            softly.assertThat(foundRoomList.size()).isEqualTo(1);
            softly.assertThat(foundRoomList.get(0).getRoomNumber()).isEqualTo(ROOM_NUMBER);
        }
    }

    @Test
    public void testGetRoomById() {
        Mockito.when(this.roomRepository.findById(any(Integer.class))).thenReturn(Optional.of(room));

        Room foundRoom = this.roomService.getRoomById(ROOM_NUMBER);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundRoom).isNotNull();
            softly.assertThat(foundRoom.getRoomNumber()).isEqualTo(ROOM_NUMBER);
            softly.assertThat(foundRoom.getBuilding().getNumber()).isEqualTo(BUILDING_NUMBER);
        }
    }

    @Test
    public void testGetRoomById_throws_ResourceNotFoundException() {
        Mockito.when(this.roomRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.roomService.getRoomById(ROOM_NUMBER);
        });
    }

    @Test
    public void testGetCreatedRoom() {
        Mockito.when(this.roomRepository.existsById(ROOM_NUMBER)).thenReturn(false);
        Mockito.when(this.buildingRepository.findById(any(Integer.class))).thenReturn(Optional.of(building));
        Mockito.when(this.roomRepository.save(any(Room.class))).thenReturn(room);

        Room foundRoom = this.roomService.getCreatedRoom(roomDto);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundRoom).isNotNull();
            softly.assertThat(foundRoom.getRoomNumber()).isEqualTo(ROOM_NUMBER);
            softly.assertThat(foundRoom.getBuilding().getNumber()).isEqualTo(BUILDING_NUMBER);
        }
    }

    @Test
    public void testGetCreatedRoom_throws_BadRequestException() {
        Mockito.when(this.roomRepository.existsById(any(Integer.class))).thenReturn(true);

        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.roomService.getCreatedRoom(roomDto);
        });
    }

    @Test
    public void testGetCreatedRoom_throws_ResourceNotFoundException() {
        Mockito.when(this.roomRepository.existsById(any(Integer.class))).thenReturn(false);
        Mockito.when(this.buildingRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.roomService.getCreatedRoom(roomDto);
        });
    }

    @Test
    public void testGetCreatedRoom_throws_Exception() {
        Mockito.when(this.roomRepository.existsById(any(Integer.class))).thenReturn(false);
        Mockito.when(this.buildingRepository.findById(any(Integer.class))).thenReturn(Optional.of(building));
        Mockito.when(this.roomRepository.save(any(Room.class))).thenThrow(new RuntimeException());

        Assertions.assertThrows(ResourceNotCreatedException.class, () -> {
            this.roomService.getCreatedRoom(roomDto);
        });
    }

    @Test
    public void testGetUpdatedRoom() {
        Integer capacity = 10000;

        roomDto.setCapacity(capacity);

        Room newRoom = new Room();
        newRoom.setRoomNumber(ROOM_NUMBER);
        newRoom.setBuilding(building);
        newRoom.setAudioVisual(AUDIO_VISUAL);
        newRoom.setCapacity(capacity);

        Mockito.when(this.roomRepository.findById(any(Integer.class))).thenReturn(Optional.of(room));
        Mockito.when(this.buildingRepository.findById(any(Integer.class))).thenReturn(Optional.of(building));
        Mockito.when(this.roomRepository.save(any(Room.class))).thenReturn(newRoom);

        Room foundRoom = this.roomService.getUpdatedRoom(ROOM_NUMBER, roomDto);
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundRoom).isNotNull();
            softly.assertThat(foundRoom.getRoomNumber()).isEqualTo(ROOM_NUMBER);
            softly.assertThat(foundRoom.getBuilding().getNumber()).isEqualTo(BUILDING_NUMBER);
            softly.assertThat(foundRoom.getCapacity()).isEqualTo(capacity);
        }
    }

    @Test
    public void testGetUpdatedRoom_throws_BadRequestException() {
        roomDto.setRoomNumber(0);

        Mockito.when(this.roomRepository.findById(any(Integer.class))).thenReturn(Optional.of(room));
        Assertions.assertThrows(BadRequestRequestException.class, () -> {
            this.roomService.getUpdatedRoom(ROOM_NUMBER, roomDto);
        });
    }

    @Test
    public void testDeleteRoom_throws_ResourceNotDeletedException() {
        Mockito.when(this.roomRepository.findById(any(Integer.class))).thenReturn(Optional.of(room));

        Mockito.doThrow(RuntimeException.class).when(this.roomRepository).delete(room);
        Assertions.assertThrows(ResourceNotDeletedException.class, () -> {
            this.roomService.deleteRoom(ROOM_NUMBER);
        });
    }
}
