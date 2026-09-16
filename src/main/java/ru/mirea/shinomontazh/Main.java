// package ru.mirea.shinomontazh;

// import ru.mirea.shinomontazh.model.Client;
// import ru.mirea.shinomontazh.service.ClientService;
// import java.util.List;


// public class Main {
//     public static void main(String[]args) {
//         System.out.println("Система шиномонтажа запущена");

//         ClientService clientService = new ClientService();

//         try {

//             List<Client> clients = clientService.getAllClients();

//             System.out.println("Список клиентов:");

//             for (Client client : clients) {

//                 System.out.println(
//                         client.getId() + " | " +
//                         client.getFullName() + " | " +
//                         client.getPhone() + " | " +
//                         client.getEmail()
//                 );
//             }

//         } catch (Exception e) {

//             System.out.println("Ошибка:");
//             System.out.println(e.getMessage());

//         }
//     }
// }





// package ru.mirea.shinomontazh;

// import ru.mirea.shinomontazh.model.WorkOrder;
// import ru.mirea.shinomontazh.repository.WorkOrderRepository;

// import java.util.List;

// public class Main {

//     public static void main(String[] args) {

//         WorkOrderRepository repository = new WorkOrderRepository();

//         try {

//             List<WorkOrder> orders = repository.getAll();

//             System.out.println("Список заказов:");
//             System.out.println();

//             for (WorkOrder order : orders) {
//                 System.out.println(order);
//             }

//         } catch (Exception e) {

//             System.out.println("Ошибка:");
//             System.out.println(e.getMessage());
//         }
//     }
// }

package ru.mirea.shinomontazh;

import ru.mirea.shinomontazh.model.OrderStatus;
import ru.mirea.shinomontazh.model.WorkOrder;
import ru.mirea.shinomontazh.repository.WorkOrderRepository;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        WorkOrderRepository repository = new WorkOrderRepository();

        try {

            WorkOrder order = new WorkOrder();

            order.setCarBrand("Mercedes C200");
            order.setCarNumber("Т777ЕЕ77");
            order.setServiceName("Замена шин");
            order.setStatus(OrderStatus.NEW);
            order.setPrice(new BigDecimal("3500"));
            order.setClientId(1);

            int newId = repository.create(order);

            System.out.println("Заказ успешно создан!");
            System.out.println("ID нового заказа: " + newId);

            WorkOrder createdOrder = repository.getById(newId);

            System.out.println("Созданный заказ:");
            System.out.println(createdOrder);

        } catch (Exception e) {

            System.out.println("Ошибка:");
            System.out.println(e.getMessage());
        }
    }
}