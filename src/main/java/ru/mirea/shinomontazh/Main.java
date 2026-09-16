package ru.mirea.shinomontazh;

import ru.mirea.shinomontazh.model.WorkOrder;
import ru.mirea.shinomontazh.repository.WorkOrderRepository;

public class Main {

    public static void main(String[] args) {

        WorkOrderRepository repository = new WorkOrderRepository();

        try {

            WorkOrder order = repository.getById(11);

            if (order == null) {
                System.out.println("Заказ не найден");
                return;
            }

            System.out.println("Удаляем заказ:");
            System.out.println(order);

            boolean deleted = repository.delete(11);

            if (deleted) {
                System.out.println();
                System.out.println("Заказ успешно удален!");
            } else {
                System.out.println("Не удалось удалить заказ");
            }

            WorkOrder checkOrder = repository.getById(11);

            if (checkOrder == null) {
                System.out.println("Проверка: заказа №11 больше нет в базе.");
            }

        } catch (Exception e) {

            System.out.println("Ошибка:");
            System.out.println(e.getMessage());
        }
    }
}