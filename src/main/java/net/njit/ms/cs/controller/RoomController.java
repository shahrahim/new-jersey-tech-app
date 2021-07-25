package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.request.RoomDto;
import net.njit.ms.cs.model.entity.Room;
import net.njit.ms.cs.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> roomList = this.roomService.getAllRooms();
        return ResponseEntity.ok().body(roomList);
    }

    @GetMapping("/{number}")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer number) {
        Room room = this.roomService.getRoomById(number);
        return ResponseEntity.ok().body(room);
    }

    @PostMapping
    public ResponseEntity<Room> getCreatedRoom(@RequestBody RoomDto roomDto) {
        return ResponseEntity.created(null).body(this.roomService.getCreatedRoom(roomDto));
    }

    @PutMapping("/{number}")
    public ResponseEntity<Room> getUpdatedRoom(@PathVariable Integer number, @RequestBody RoomDto roomDto) {
        Room room = this.roomService.getUpdatedRoom(number, roomDto);
        return ResponseEntity.ok().body(room);
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<String> deleteRoom(@PathVariable Integer number) {
        this.roomService.deleteRoom(number);
        String message = String.format("Room with number %s has been deleted.", number);
        return ResponseEntity.ok().body(message);
    }
}
