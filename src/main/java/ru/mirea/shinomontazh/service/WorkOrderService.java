package ru.mirea.shinomontazh.service;

import ru.mirea.shinomontazh.exception.BusinessException;
import ru.mirea.shinomontazh.exception.EntityNotFoundException;
import ru.mirea.shinomontazh.model.OrderStatus;
import ru.mirea.shinomontazh.model.WorkOrder;
import ru.mirea.shinomontazh.repository.WorkOrderRepository;

import java.math.BigDecimal;
import java.util.List;

public class WorkOrderService {

    private final WorkOrderRepository repository;

    public WorkOrderService() {
        repository = new WorkOrderRepository();
    }

    public List<WorkOrder> getAllOrders() throws Exception {
        return repository.getAll();
    }

    public WorkOrder getOrderById(int id) throws Exception {

        if (id <= 0) {
            throw new BusinessException(
                    "ID заказа должен быть больше 0."
            );
        }

        WorkOrder order = repository.getById(id);

        if (order == null) {
            throw new EntityNotFoundException(
                    "Заказ с ID " + id + " не найден."
            );
        }

        return order;
    }

    public int createOrder(WorkOrder order) throws Exception {

        validateOrder(order);

        return repository.create(order);
    }

    public boolean updateOrder(WorkOrder order) throws Exception {

        WorkOrder oldOrder = repository.getById(order.getId());

        if (oldOrder == null) {
            throw new EntityNotFoundException(
                    "Заказ с ID " + order.getId() + " не найден."
            );
        }

        validateOrder(order);

        checkStatusChange(
                oldOrder.getStatus(),
                order.getStatus()
        );

        return repository.update(order);
    }

    public boolean deleteOrder(int id) throws Exception {

        WorkOrder order = repository.getById(id);

        if (order == null) {
            throw new EntityNotFoundException(
                    "Заказ с ID " + id + " не найден."
            );
        }

        return repository.delete(id);
    }

    private void validateOrder(WorkOrder order) throws Exception {

        if (order.getCarBrand() == null
                || order.getCarBrand().isBlank()) {

            throw new BusinessException(
                    "Марка автомобиля не может быть пустой."
            );
        }

        if (order.getCarNumber() == null
                || order.getCarNumber().isBlank()) {

            throw new BusinessException(
                    "Госномер автомобиля не может быть пустым."
            );
        }

        if (order.getServiceName() == null
                || order.getServiceName().isBlank()) {

            throw new BusinessException(
                    "Название услуги не может быть пустым."
            );
        }

        if (order.getPrice() == null
                || order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {

            throw new BusinessException(
                    "Цена должна быть больше 0."
            );
        }

        if (order.getStatus() == null) {

            throw new BusinessException(
                    "Статус заказа не указан."
            );
        }

        if (order.getClientId() <= 0) {

            throw new BusinessException(
                    "ID клиента должен быть больше 0."
            );
        }

        if (!repository.clientExists(order.getClientId())) {

            throw new BusinessException(
                    "Клиент с ID "
                            + order.getClientId()
                            + " не существует."
            );
        }
    }

    private void checkStatusChange(
            OrderStatus oldStatus,
            OrderStatus newStatus
    ) throws BusinessException {

        if (oldStatus == OrderStatus.DONE
                && newStatus != OrderStatus.DONE) {

            throw new BusinessException(
                    "Выполненный заказ нельзя вернуть в другой статус."
            );
        }

        if (oldStatus == OrderStatus.CANCELLED
                && newStatus != OrderStatus.CANCELLED) {

            throw new BusinessException(
                    "Отмененный заказ нельзя вернуть в работу."
            );
        }

        if (oldStatus == OrderStatus.IN_PROGRESS
                && newStatus == OrderStatus.NEW) {

            throw new BusinessException(
                    "Заказ в работе нельзя вернуть в статус NEW."
            );
        }
    }
}