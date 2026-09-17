package ru.mirea.shinomontazh.service;

import ru.mirea.shinomontazh.exception.BusinessException;
import ru.mirea.shinomontazh.exception.EntityNotFoundException;
import ru.mirea.shinomontazh.model.Client;
import ru.mirea.shinomontazh.repository.ClientRepository;

import java.util.List;

public class ClientService {

    private final ClientRepository repository;

    public ClientService() {
        repository = new ClientRepository();
    }

    public List<Client> getAllClients() throws Exception {
        return repository.getAll();
    }

    public Client getClientById(int id) throws Exception {

        if (id <= 0) {
            throw new BusinessException(
                    "ID клиента должен быть больше 0."
            );
        }

        Client client = repository.getById(id);

        if (client == null) {
            throw new EntityNotFoundException(
                    "Клиент с ID " + id + " не найден."
            );
        }

        return client;
    }

    public int createClient(Client client) throws Exception {

        validateClient(client, false);

        return repository.create(client);
    }

    public boolean updateClient(Client client) throws Exception {

        Client oldClient = repository.getById(client.getId());

        if (oldClient == null) {
            throw new EntityNotFoundException(
                    "Клиент с ID " + client.getId() + " не найден."
            );
        }

        validateClient(client, true);

        return repository.update(client);
    }

    public boolean deleteClient(int id) throws Exception {

        Client client = repository.getById(id);

        if (client == null) {
            throw new EntityNotFoundException(
                    "Клиент с ID " + id + " не найден."
            );
        }

        return repository.delete(id);
    }

    private void validateClient(
            Client client,
            boolean update
    ) throws Exception {

        if (client.getFullName() == null
                || client.getFullName().isBlank()) {

            throw new BusinessException(
                    "ФИО клиента не может быть пустым."
            );
        }

        if (client.getPhone() == null
                || client.getPhone().isBlank()) {

            throw new BusinessException(
                    "Телефон клиента не может быть пустым."
            );
        }

        if (client.getEmail() == null
                || client.getEmail().isBlank()) {

            throw new BusinessException(
                    "Email клиента не может быть пустым."
            );
        }

        if (!client.getEmail().contains("@")) {

            throw new BusinessException(
                    "Некорректный email."
            );
        }

        if (!update) {

            if (repository.phoneExists(client.getPhone())) {

                throw new BusinessException(
                        "Клиент с таким телефоном уже существует."
                );
            }

            if (repository.emailExists(client.getEmail())) {

                throw new BusinessException(
                        "Клиент с таким email уже существует."
                );
            }

        } else {

            if (repository.phoneExistsForOtherClient(
                    client.getPhone(),
                    client.getId()
            )) {

                throw new BusinessException(
                        "Другой клиент уже использует этот телефон."
                );
            }

            if (repository.emailExistsForOtherClient(
                    client.getEmail(),
                    client.getId()
            )) {

                throw new BusinessException(
                        "Другой клиент уже использует этот email."
                );
            }
        }
    }
}