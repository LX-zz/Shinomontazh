package ru.mirea.shinomontazh;

import ru.mirea.shinomontazh.model.Client;
import ru.mirea.shinomontazh.model.OrderStatus;
import ru.mirea.shinomontazh.model.WorkOrder;
import ru.mirea.shinomontazh.service.ClientService;
import ru.mirea.shinomontazh.service.WorkOrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final WorkOrderService workOrderService =
            new WorkOrderService();

    private static final ClientService clientService =
            new ClientService();

    public static void main(String[] args) {

        boolean work = true;

        while (work) {

            System.out.println();
            System.out.println("1. Клиенты");
            System.out.println("2. Показать все заказы");
            System.out.println("3. Найти заказ по ID");
            System.out.println("4. Создать заказ");
            System.out.println("5. Изменить заказ");
            System.out.println("6. Удалить заказ");
            System.out.println("7. Поиск заказов");
            System.out.println("8. Фильтрация заказов");
            System.out.println("9. Сортировка заказов");
            System.out.println("10. Статистика");
            System.out.println("11. Экспорт в Excel");
            System.out.println("0. Выход");

            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    clientsMenu();
                    break;

                case "2":
                    showAllOrders();
                    break;

                case "3":
                    findOrderById();
                    break;

                case "4":
                    createOrder();
                    break;

                case "5":
                    updateOrder();
                    break;

                case "6":
                    deleteOrder();
                    break;

                case "7":
                    searchOrders();
                    break;

                case "8":
                    filterOrders();
                    break;

                case "9":
                    sortOrders();
                    break;

                case "10":
                    showStatistics();
                    break;

                case "11":
                    exportToExcel();
                    break;

                case "0":
                    work = false;
                    System.out.println("Программа завершена.");
                    break;

                default:
                    System.out.println("Такого пункта меню нет.");
            }
        }
    }

    private static void clientsMenu() {

        boolean clientsMenu = true;

        while (clientsMenu) {

            System.out.println();
            System.out.println("1. Показать всех клиентов");
            System.out.println("0. Назад");

            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    showAllClients();
                    break;

                case "0":
                    clientsMenu = false;
                    break;

                default:
                    System.out.println("Такого пункта меню нет.");
            }
        }
    }

    private static void showAllClients() {

        try {

            List<Client> clients =
                    clientService.getAllClients();

            if (clients.isEmpty()) {

                System.out.println("Клиентов нет.");
                return;
            }

            System.out.println();
            System.out.println("Список клиентов:");

            for (Client client : clients) {

                System.out.println(
                        client.getId()
                                + " | "
                                + client.getFullName()
                                + " | "
                                + client.getPhone()
                                + " | "
                                + client.getEmail()
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void showAllOrders() {

        try {

            List<WorkOrder> orders =
                    workOrderService.getAllOrders();

            printOrders(orders);

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void findOrderById() {

        int id = readInt(
                "Введите ID заказа: "
        );

        try {

            WorkOrder order =
                    workOrderService.getOrderById(id);

            System.out.println(order);

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void createOrder() {

        try {

            WorkOrder order = new WorkOrder();

            System.out.print(
                    "Марка автомобиля: "
            );

            order.setCarBrand(
                    scanner.nextLine()
            );

            System.out.print(
                    "Госномер автомобиля: "
            );

            order.setCarNumber(
                    scanner.nextLine()
            );

            System.out.print(
                    "Название услуги: "
            );

            order.setServiceName(
                    scanner.nextLine()
            );

            order.setStatus(
                    readStatus()
            );

            order.setPrice(
                    readPrice()
            );

            System.out.println();
            System.out.println(
                    "Перед вводом ID клиента можно посмотреть список клиентов через пункт 1 главного меню."
            );

            int clientId = readInt(
                    "ID клиента: "
            );

            order.setClientId(
                    clientId
            );

            int newId =
                    workOrderService.createOrder(order);

            System.out.println(
                    "Заказ создан."
            );

            System.out.println(
                    "ID нового заказа: " + newId
            );

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void updateOrder() {

        int id = readInt(
                "Введите ID заказа: "
        );

        try {

            WorkOrder order =
                    workOrderService.getOrderById(id);

            System.out.println(
                    "Текущий заказ:"
            );

            System.out.println(order);

            System.out.println();
            System.out.println(
                    "Введите новый статус:"
            );

            order.setStatus(
                    readStatus()
            );

            order.setPrice(
                    readPrice()
            );

            boolean updated =
                    workOrderService.updateOrder(order);

            if (updated) {

                System.out.println(
                        "Заказ изменен."
                );

            } else {

                System.out.println(
                        "Не удалось изменить заказ."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void deleteOrder() {

        int id = readInt(
                "Введите ID заказа: "
        );

        try {

            WorkOrder order =
                    workOrderService.getOrderById(id);

            System.out.println(
                    "Удаляется заказ:"
            );

            System.out.println(order);

            boolean deleted =
                    workOrderService.deleteOrder(id);

            if (deleted) {

                System.out.println(
                        "Заказ удален."
                );

            } else {

                System.out.println(
                        "Не удалось удалить заказ."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void searchOrders() {

        boolean searchMenu = true;

        while (searchMenu) {

            System.out.println();
            System.out.println(
                    "1. Поиск по марке автомобиля"
            );
            System.out.println(
                    "2. Поиск по госномеру"
            );
            System.out.println(
                    "0. Назад"
            );

            System.out.print(
                    "Выберите действие: "
            );

            String choice =
                    scanner.nextLine();

            switch (choice) {

                case "1":
                    searchByCarBrand();
                    break;

                case "2":
                    searchByCarNumber();
                    break;

                case "0":
                    searchMenu = false;
                    break;

                default:
                    System.out.println(
                            "Такого пункта меню нет."
                    );
            }
        }
    }

    private static void searchByCarBrand() {

        System.out.print(
                "Введите марку автомобиля: "
        );

        String carBrand =
                scanner.nextLine();

        try {

            List<WorkOrder> orders =
                    workOrderService.searchByCarBrand(
                            carBrand
                    );

            printOrders(orders);

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void searchByCarNumber() {

        System.out.print(
                "Введите госномер автомобиля: "
        );

        String carNumber =
                scanner.nextLine();

        try {

            List<WorkOrder> orders =
                    workOrderService.searchByCarNumber(
                            carNumber
                    );

            printOrders(orders);

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void filterOrders() {

        boolean filterMenu = true;

        while (filterMenu) {

            System.out.println();
            System.out.println(
                    "1. Фильтр по статусу"
            );
            System.out.println(
                    "2. Фильтр по клиенту"
            );
            System.out.println(
                    "0. Назад"
            );

            System.out.print(
                    "Выберите действие: "
            );

            String choice =
                    scanner.nextLine();

            switch (choice) {

                case "1":
                    filterByStatus();
                    break;

                case "2":
                    filterByClient();
                    break;

                case "0":
                    filterMenu = false;
                    break;

                default:
                    System.out.println(
                            "Такого пункта меню нет."
                    );
            }
        }
    }

    private static void filterByStatus() {

        try {

            System.out.println(
                    "Выберите статус:"
            );

            OrderStatus status =
                    readStatus();

            List<WorkOrder> orders =
                    workOrderService.filterByStatus(
                            status
                    );

            printOrders(orders);

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void filterByClient() {

        int clientId = readInt(
                "Введите ID клиента: "
        );

        try {

            List<WorkOrder> orders =
                    workOrderService.filterByClientId(
                            clientId
                    );

            printOrders(orders);

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void sortOrders() {

        boolean sortMenu = true;

        while (sortMenu) {

            System.out.println();
            System.out.println(
                    "1. Сортировка по цене"
            );
            System.out.println(
                    "2. Сортировка по дате создания"
            );
            System.out.println(
                    "0. Назад"
            );

            System.out.print(
                    "Выберите действие: "
            );

            String choice =
                    scanner.nextLine();

            switch (choice) {

                case "1":
                    sortByPrice();
                    break;

                case "2":
                    sortByDate();
                    break;

                case "0":
                    sortMenu = false;
                    break;

                default:
                    System.out.println(
                            "Такого пункта меню нет."
                    );
            }
        }
    }

    private static void sortByPrice() {

        try {

            List<WorkOrder> orders =
                    workOrderService.sortByPrice();

            printOrders(orders);

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void sortByDate() {

        try {

            List<WorkOrder> orders =
                    workOrderService.sortByDate();

            printOrders(orders);

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void showStatistics() {

        try {

            long total =
                    workOrderService.getTotalOrders();

            long newOrders =
                    workOrderService.countByStatus(
                            OrderStatus.NEW
                    );

            long inProgress =
                    workOrderService.countByStatus(
                            OrderStatus.IN_PROGRESS
                    );

            long done =
                    workOrderService.countByStatus(
                            OrderStatus.DONE
                    );

            long cancelled =
                    workOrderService.countByStatus(
                            OrderStatus.CANCELLED
                    );

            BigDecimal totalPrice =
                    workOrderService.getTotalPrice();

            System.out.println();
            System.out.println(
                    "Статистика:"
            );

            System.out.println(
                    "Всего заказов: " + total
            );

            System.out.println(
                    "Новых заказов: " + newOrders
            );

            System.out.println(
                    "Заказов в работе: " + inProgress
            );

            System.out.println(
                    "Выполненных заказов: " + done
            );

            System.out.println(
                    "Отмененных заказов: " + cancelled
            );

            System.out.println(
                    "Общая стоимость заказов: "
                            + totalPrice
                            + " руб."
            );

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private static void exportToExcel() {

        try {

            String fileName =
                    "orders.xlsx";

            workOrderService.exportToExcel(
                    fileName
            );

            System.out.println(
                    "Данные экспортированы в файл: "
                            + fileName
            );

        } catch (Exception e) {

            System.out.println(
                    "Ошибка экспорта: "
                            + e.getMessage()
            );
        }
    }

    private static void printOrders(
            List<WorkOrder> orders
    ) {

        if (orders.isEmpty()) {

            System.out.println(
                    "Заказы не найдены."
            );

            return;
        }

        for (WorkOrder order : orders) {

            System.out.println(order);
        }
    }

    private static int readInt(
            String message
    ) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine();

            try {

                return Integer.parseInt(
                        input
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Ошибка: необходимо ввести целое число."
                );
            }
        }
    }

    private static BigDecimal readPrice() {

        while (true) {

            System.out.print(
                    "Цена: "
            );

            String input =
                    scanner.nextLine();

            try {

                return new BigDecimal(
                        input
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Ошибка: цена должна быть числом."
                );
            }
        }
    }

    private static OrderStatus readStatus() {

        while (true) {

            System.out.println(
                    "Доступные статусы:"
            );

            System.out.println(
                    "1. NEW"
            );

            System.out.println(
                    "2. IN_PROGRESS"
            );

            System.out.println(
                    "3. DONE"
            );

            System.out.println(
                    "4. CANCELLED"
            );

            System.out.print(
                    "Выберите статус: "
            );

            String status =
                    scanner.nextLine();

            switch (status) {

                case "1":
                    return OrderStatus.NEW;

                case "2":
                    return OrderStatus.IN_PROGRESS;

                case "3":
                    return OrderStatus.DONE;

                case "4":
                    return OrderStatus.CANCELLED;

                default:
                    System.out.println(
                            "Некорректный статус."
                    );
            }
        }
    }
}