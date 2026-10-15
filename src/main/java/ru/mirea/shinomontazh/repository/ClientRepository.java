package ru.mirea.shinomontazh.repository;

import ru.mirea.shinomontazh.model.Client;
import ru.mirea.shinomontazh.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {

    public List<Client> getAll() throws Exception {

        List<Client> clients = new ArrayList<>();

        String sql = "SELECT * FROM clients ORDER BY id";

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Client client = new Client();

                client.setId(resultSet.getInt("id"));
                client.setFullName(resultSet.getString("full_name"));
                client.setPhone(resultSet.getString("phone"));
                client.setEmail(resultSet.getString("email"));

                clients.add(client);
            }
        }

        return clients;
    }

    public Client getById(int id) throws Exception {

        String sql = "SELECT * FROM clients WHERE id = ?";

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Client client = new Client();

                    client.setId(resultSet.getInt("id"));
                    client.setFullName(resultSet.getString("full_name"));
                    client.setPhone(resultSet.getString("phone"));
                    client.setEmail(resultSet.getString("email"));

                    return client;
                }
            }
        }

        return null;
    }

    public int create(Client client) throws Exception {

        String sql = """
                INSERT INTO clients
                (full_name, phone, email)
                VALUES (?, ?, ?)
                RETURNING id
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, client.getFullName());
            statement.setString(2, client.getPhone());
            statement.setString(3, client.getEmail());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }

        return -1;
    }

    public boolean update(Client client) throws Exception {

        String sql = """
                UPDATE clients
                SET full_name = ?,
                    phone = ?,
                    email = ?
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, client.getFullName());
            statement.setString(2, client.getPhone());
            statement.setString(3, client.getEmail());
            statement.setInt(4, client.getId());

            int updatedRows = statement.executeUpdate();

            return updatedRows > 0;
        }
    }

    public boolean delete(int id) throws Exception {

        String sql = "DELETE FROM clients WHERE id = ?";

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int deletedRows = statement.executeUpdate();

            return deletedRows > 0;
        }
    }

    public boolean phoneExists(String phone) throws Exception {

        String sql = "SELECT 1 FROM clients WHERE phone = ?";

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, phone);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean phoneExistsForOtherClient(
            String phone,
            int clientId
    ) throws Exception {

        String sql = """
                SELECT 1
                FROM clients
                WHERE phone = ?
                AND id <> ?
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, phone);
            statement.setInt(2, clientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean emailExists(String email) throws Exception {

        String sql = "SELECT 1 FROM clients WHERE email = ?";

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean emailExistsForOtherClient(
            String email,
            int clientId
    ) throws Exception {

        String sql = """
                SELECT 1
                FROM clients
                WHERE email = ?
                AND id <> ?
                """;

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);
            statement.setInt(2, clientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}