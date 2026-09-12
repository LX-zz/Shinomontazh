package ru.mirea.shinomontazh.repository;

import ru.mirea.shinomontazh.model.Client;
import ru.mirea.shinomontazh.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository {

    public List<Client> findAll() throws SQLException{
        List<Client> clients = new ArrayList<>();
        String sql = "Select id, full_name, phone, email FROM clients ORDER BY id";
    try (
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()
    ){
        while (resultSet.next()){
            Client client = new Client(
                resultSet.getInt("id"),
                resultSet.getString("full_name"),
                resultSet.getString("phone"),
                resultSet.getString("email")
            );
            clients.add(client);
            }
        }  
        return clients;
    }
}