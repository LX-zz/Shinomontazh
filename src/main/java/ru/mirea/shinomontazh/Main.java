package ru.mirea.shinomontazh;
import ru.mirea.shinomontazh.util.DatabaseManager;
import java.sql.Connection;


public class Main {
    public static void main(String[]args) {
        System.out.println("Система шиномонтажа запущена");
    
        try (Connection connection = DatabaseManager.getConnection()) {
        System.out.println("Подключение к базе данных успешно");
        }
            catch(Exception e){
                System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
            }
        }
}