package ru.mirea.shinomontazh.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {

        Properties properties =
                new Properties();

        try (
                InputStream input =
                        DatabaseManager.class
                                .getResourceAsStream(
                                        "/database.properties"
                                )
        ) {

            if (input == null) {

                throw new RuntimeException(
                        "Файл database.properties не найден."
                );
            }

            properties.load(input);

            URL =
                    properties.getProperty(
                            "db.url"
                    );

            USER =
                    properties.getProperty(
                            "db.user"
                    );

            PASSWORD =
                    properties.getProperty(
                            "db.password"
                    );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Ошибка чтения database.properties.",
                    e
            );
        }
    }

    private DatabaseManager() {
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}