package ru.mirea.shinomontazh.service;

import ru.mirea.shinomontazh.model.OrderStatus;
import ru.mirea.shinomontazh.model.WorkOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class StatisticsService {

    private final WorkOrderService workOrderService;

    public StatisticsService() {
        workOrderService = new WorkOrderService();
    }

    public List<WorkOrder> getAllOrders()
            throws Exception {

        return workOrderService.getAllOrders();
    }

    public long getTotalOrders()
            throws Exception {

        return getAllOrders().size();
    }

    public long getCountByStatus(
            OrderStatus status
    ) throws Exception {

        return getAllOrders()
                .stream()
                .filter(order ->
                        order.getStatus() == status
                )
                .count();
    }

    public BigDecimal getTotalPrice()
            throws Exception {

        return getAllOrders()
                .stream()
                .map(WorkOrder::getPrice)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    public BigDecimal getAveragePrice()
            throws Exception {

        List<WorkOrder> orders =
                getAllOrders();

        if (orders.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total =
                orders
                        .stream()
                        .map(WorkOrder::getPrice)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return total.divide(
                BigDecimal.valueOf(
                        orders.size()
                ),
                2,
                RoundingMode.HALF_UP
        );
    }

    public BigDecimal getMaxPrice()
            throws Exception {

        return getAllOrders()
                .stream()
                .map(WorkOrder::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}