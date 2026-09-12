package ru.mirea.shinomontazh.service;
import ru.mirea.shinomontazh.model.Client;
import ru.mirea.shinomontazh.repository.ClientRepository;

import java.sql.SQLException;
import java.util.List;

public class ClientService{

    private final ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepository();
    }

    public List<Client> getAllClients() throws SQLException {
        return clientRepository.findAll();
    }
}