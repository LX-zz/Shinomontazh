package ru.mirea.shinomontazh;

import ru.mirea.shinomontazh.model.Client;
import ru.mirea.shinomontazh.repository.ClientRepository;
import java.util.List;


public class Main {
    public static void main(String[]args) {
        System.out.println("Система шиномонтажа запущена");

        ClientRepository clientRepository = new ClientRepository();
    
        try {

            List<Client> clients = clientRepository.findAll();

            System.out.println("Список клиентов:");

            for (Client client : clients) {

                System.out.println(
                        client.getId() + " | " +
                        client.getFullName() + " | " +
                        client.getPhone() + " | " +
                        client.getEmail()
                );
            }

        } catch (Exception e) {

            System.out.println("Ошибка:");
            System.out.println(e.getMessage());

        }
    }
}