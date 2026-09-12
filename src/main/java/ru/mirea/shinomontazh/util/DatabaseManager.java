package ru.mirea.shinomontazh.util;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/shinomontazh_db";
    private static final String USER = "xmode";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}