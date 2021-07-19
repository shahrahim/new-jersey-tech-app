package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.model.dto.ClientDto;
import net.njit.ms.cs.model.entity.Client;
import net.njit.ms.cs.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return this.clientRepository.findAll();
    }

    public Client getClientByName(String name) {

        return this.clientRepository.findById(name).orElseThrow(RuntimeException::new);
    }

    public Client getCreatedClient(ClientDto clientDto) {
        if(this.clientRepository.findById(clientDto.getName()).isPresent()) {
            throw new RuntimeException("Client with name already exists");
        } else {
            return this.getCreateOrReplacedClient(this.getNewClient(clientDto));
        }
    }

    public Client getUpdatedClient(String name, ClientDto clientDto) {
        Client client = this.getClientByName(name);
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        return this.getCreateOrReplacedClient(client);
    }

    public void deleteClient(String name) {
        try {
            this.clientRepository.delete(this.clientRepository.findById(name).orElseThrow(RuntimeException::new));
        } catch (Exception e) {
            log.error("Something went wrong deleting client {} {}", name, e.getMessage());
        }
    }

    private Client getCreateOrReplacedClient(Client client) {
        try {
            return this.clientRepository.save(client);
        } catch(Exception e) {
            log.error("Something went wrong saving/replacing client {} {}", client.getName(), e.getMessage());
            throw new RuntimeException();
        }
    }

    private Client getNewClient(ClientDto clientDto) {
        Client client = new Client();
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        return client;
    }

}
