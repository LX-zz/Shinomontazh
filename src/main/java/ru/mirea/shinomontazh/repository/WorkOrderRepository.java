package ru.mirea.shinomontazh.repository;

import ru.mirea.shinomontazh.model.OrderStatus;
import ru.mirea.shinomontazh.model.WorkOrder;
import ru.mirea.shinomontazh.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorkOrderRepository {

    public List<WorkOrder> getAll() throws Exception {

        List<WorkOrder> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders ORDER BY id";

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                WorkOrder order = new WorkOrder();

                order.setId(resultSet.getInt("id"));
                order.setCarBrand(resultSet.getString("car_brand"));
                order.setCarNumber(resultSet.getString("car_number"));
                order.setServiceName(resultSet.getString("service_type"));

                order.setStatus(
                        OrderStatus.valueOf(
                                resultSet.getString("status")
                        )
                );

                order.setPrice(resultSet.getBigDecimal("price"));

                order.setCreateDate(
                        resultSet.getTimestamp("create_date")
                                .toLocalDateTime()
                );

                order.setClientId(
                        resultSet.getInt("client_id")
                );

                orders.add(order);
            }
        }

        return orders;
    }

    public WorkOrder getById(int id) throws Exception {

        String sql = "SELECT * FROM orders WHERE id = ?";

        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    WorkOrder order = new WorkOrder();

                    order.setId(resultSet.getInt("id"));
                    order.setCarBrand(resultSet.getString("car_brand"));
                    order.setCarNumber(resultSet.getString("car_number"));
                    order.setServiceName(resultSet.getString("service_type"));

                    order.setStatus(
                            OrderStatus.valueOf(
                                    resultSet.getString("status")
                            )
                    );

                    order.setPrice(resultSet.getBigDecimal("price"));

                    order.setCreateDate(
                            resultSet.getTimestamp("create_date")
                                    .toLocalDateTime()
                    );

                    order.setClientId(
                            resultSet.getInt("client_id")
                    );

                    return order;
                }
            }
        }

        return null;
    }
}