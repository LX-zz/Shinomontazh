package ru.mirea.shinomontazh.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import ru.mirea.shinomontazh.model.OrderStatus;
import ru.mirea.shinomontazh.service.StatisticsService;

import java.math.BigDecimal;

public class StatisticsController {

        @FXML
        private Label totalOrdersLabel;

        @FXML
        private Label newOrdersLabel;

        @FXML
        private Label inProgressOrdersLabel;

        @FXML
        private Label doneOrdersLabel;

        @FXML
        private Label cancelledOrdersLabel;

        @FXML
        private Label totalPriceLabel;

        @FXML
        private Label averagePriceLabel;

        @FXML
        private Label maxPriceLabel;

        private final StatisticsService statisticsService;

        public StatisticsController() {

                statisticsService = new StatisticsService();
        }

        @FXML
        public void initialize() {

                loadStatistics();
        }

        @FXML
        private void onRefreshButtonClick() {

                loadStatistics();
        }

        private void loadStatistics() {

                try {

                        long total = statisticsService
                                        .getTotalOrders();

                        long newOrders = statisticsService
                                        .getCountByStatus(
                                                        OrderStatus.NEW);

                        long inProgress = statisticsService
                                        .getCountByStatus(
                                                        OrderStatus.IN_PROGRESS);

                        long done = statisticsService
                                        .getCountByStatus(
                                                        OrderStatus.DONE);

                        long cancelled = statisticsService
                                        .getCountByStatus(
                                                        OrderStatus.CANCELLED);

                        BigDecimal totalPrice = statisticsService
                                        .getTotalPrice();

                        BigDecimal averagePrice = statisticsService
                                        .getAveragePrice();

                        BigDecimal maxPrice = statisticsService
                                        .getMaxPrice();

                        totalOrdersLabel.setText(
                                        String.valueOf(total));

                        newOrdersLabel.setText(
                                        String.valueOf(newOrders));

                        inProgressOrdersLabel.setText(
                                        String.valueOf(inProgress));

                        doneOrdersLabel.setText(
                                        String.valueOf(done));

                        cancelledOrdersLabel.setText(
                                        String.valueOf(cancelled));

                        totalPriceLabel.setText(
                                        totalPrice + " ₽");

                        averagePriceLabel.setText(
                                        averagePrice + " ₽");

                        maxPriceLabel.setText(
                                        maxPrice + " ₽");

                } catch (Exception e) {

                        showError(
                                        "Ошибка загрузки статистики",
                                        e.getMessage());
                }
        }

        private void showError(
                        String title,
                        String message) {

                Alert alert = new Alert(
                                Alert.AlertType.ERROR);

                alert.setTitle(
                                "Ошибка");

                alert.setHeaderText(
                                title);

                alert.setContentText(
                                message);

                alert.showAndWait();
        }
}