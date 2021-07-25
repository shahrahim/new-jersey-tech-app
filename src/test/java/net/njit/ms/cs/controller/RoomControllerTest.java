package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.RoomDto;
import net.njit.ms.cs.model.entity.Building;
import net.njit.ms.cs.model.entity.Room;
import net.njit.ms.cs.service.RoomService;
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
public class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private Room room;

    private Building building;

    private final Integer ROOM_NUMBER = 1;

    private final Integer BUILDING_NUMBER = 123;

    Integer CAPACITY = 100;
    String AUDIO_VISUAL = "audioVisual";

    @BeforeEach
    void init() {
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
        Mockito.when(this.roomService.getAllRooms()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Room>> foundRoomList = this.roomController.getAllRooms();
        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundRoomList).isNotNull();
            softly.assertThat(foundRoomList.getBody()).isNotNull();
            softly.assertThat(foundRoomList.getBody().size()).isEqualTo(0);
            softly.assertThat(foundRoomList.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Test
    public void testGetRoomById() {
        Mockito.when(this.roomService.getRoomById(any())).thenReturn(room);

        ResponseEntity<Room> foundRoom = this.roomController.getRoomById(ROOM_NUMBER);
        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundRoom).isNotNull();
            softly.assertThat(foundRoom.getBody()).isNotNull();
            softly.assertThat(foundRoom.getBody().getRoomNumber()).isEqualTo(ROOM_NUMBER);
            softly.assertThat(foundRoom.getBody().getBuilding().getNumber()).isEqualTo(BUILDING_NUMBER);
            softly.assertThat(foundRoom.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Test
    public void testGetCreatedRoom() {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomNumber(ROOM_NUMBER);
        roomDto.setBuildingNumber(BUILDING_NUMBER);
        roomDto.setCapacity(CAPACITY);
        roomDto.setAudioVisual(AUDIO_VISUAL);

        Mockito.when(this.roomService.getCreatedRoom(any(RoomDto.class))).thenReturn(room);

        ResponseEntity<Room> foundRoom = this.roomController.getCreatedRoom(roomDto);
        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundRoom).isNotNull();
            softly.assertThat(foundRoom.getBody()).isNotNull();
            softly.assertThat(foundRoom.getBody().getRoomNumber()).isEqualTo(ROOM_NUMBER);
            softly.assertThat(foundRoom.getBody().getBuilding().getNumber()).isEqualTo(BUILDING_NUMBER);
            softly.assertThat(foundRoom.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }
    }

    @Test
    public void testGetUpdatedRoom() {
        Integer capacity = 10000;

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomNumber(ROOM_NUMBER);
        roomDto.setBuildingNumber(BUILDING_NUMBER);
        roomDto.setCapacity(capacity);
        roomDto.setAudioVisual(AUDIO_VISUAL);

        room.setCapacity(capacity);

        Mockito.when(this.roomService.getUpdatedRoom(any(Integer.class), any(RoomDto.class))).thenReturn(room);

        ResponseEntity<Room> foundRoom = this.roomController.getUpdatedRoom(ROOM_NUMBER, roomDto);
        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(foundRoom).isNotNull();
            softly.assertThat(foundRoom.getBody()).isNotNull();
            softly.assertThat(foundRoom.getBody().getRoomNumber()).isEqualTo(ROOM_NUMBER);
            softly.assertThat(foundRoom.getBody().getBuilding().getNumber()).isEqualTo(BUILDING_NUMBER);
            softly.assertThat(foundRoom.getBody().getCapacity()).isEqualTo(capacity);
            softly.assertThat(foundRoom.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Test
    public void testDeleteRoom() {

        ResponseEntity<String> deletedRoom = this.roomController.deleteRoom(ROOM_NUMBER);
        try(AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(deletedRoom).isNotNull();
            softly.assertThat(deletedRoom.getBody()).isNotNull();
            softly.assertThat(deletedRoom.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }


}
