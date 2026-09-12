package ru.mirea.shinomontazh;

import ru.mirea.shinomontazh.model.Client;
import ru.mirea.shinomontazh.service.ClientService;
import java.util.List;


public class Main {
    public static void main(String[]args) {
        System.out.println("Система шиномонтажа запущена");

        ClientService clientService = new ClientService();

        try {

            List<Client> clients = clientService.getAllClients();

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