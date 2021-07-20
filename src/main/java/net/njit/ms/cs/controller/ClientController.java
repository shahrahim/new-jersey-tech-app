package net.njit.ms.cs.controller;

import net.njit.ms.cs.model.dto.ClientDto;
import net.njit.ms.cs.model.entity.Client;
import net.njit.ms.cs.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/clients")
@CrossOrigin("*")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok().body(this.clientService.getAllClients());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Client> getClient(@PathVariable String name) {
        return ResponseEntity.ok().body(this.clientService.getClientByName(name));
    }

    @PostMapping
    public ResponseEntity createClient(@RequestBody ClientDto clientDto) throws URISyntaxException {
        return ResponseEntity.accepted().body(this.clientService.getCreatedClient(clientDto));
    }

    @PutMapping("/{name}")
    public ResponseEntity updateClient(@PathVariable String name, @RequestBody ClientDto clientDto) {
        return ResponseEntity.ok().body(this.clientService.getUpdatedClient(name, clientDto));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity deleteClient(@PathVariable String name) {
        this.clientService.deleteClient(name);
        return ResponseEntity.ok().body(String.format("Client with name %s has been deleted.", name));
    }
}
