package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.RoomDto;
import net.njit.ms.cs.model.dto.response.RoomResponse;
import net.njit.ms.cs.model.entity.Room;
import net.njit.ms.cs.model.entity.RoomId;
import net.njit.ms.cs.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<Room> roomList = this.roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        roomList.forEach(room -> roomResponses.add(RoomService.getRoomResponse(room)));
        return ResponseEntity.ok().body(roomResponses);
    }

    @GetMapping
    public ResponseEntity<RoomResponse> getRoomById(@RequestBody RoomId roomId) {
        Room room = this.roomService.getRoomById(roomId);
        return ResponseEntity.ok().body(RoomService.getRoomResponse(room));
    }

    @PostMapping
    public ResponseEntity<RoomResponse> getCreatedRoom(@RequestBody RoomDto roomDto) {
        Room room = this.roomService.getCreatedRoom(roomDto);
        return ResponseEntity.created(null).body(RoomService.getRoomResponse(room));
    }

    @PutMapping
    public ResponseEntity<RoomResponse> getUpdatedRoom(@RequestBody RoomDto roomDto) {
        Room room = this.roomService.getUpdatedRoom(roomDto);
        return ResponseEntity.ok().body(RoomService.getRoomResponse(room));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteRoom(@RequestBody RoomId roomId) {
        this.roomService.deleteRoom(roomId);
        String message = String.format("Room with number %s has been deleted.", roomId.getRoomNumber());
        return ResponseEntity.ok().body(message);
    }
}
