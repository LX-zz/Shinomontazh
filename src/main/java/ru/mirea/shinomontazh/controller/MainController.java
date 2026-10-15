package ru.mirea.shinomontazh.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

        @FXML
        private Label statusLabel;

        @FXML
        private StackPane contentPane;

        @FXML
        public void initialize() {

                statusLabel.setText(
                                "Система готова к работе");
        }

        // =====================================================
        // ЗАКАЗЫ
        // =====================================================

        @FXML
        private void onOrdersButtonClick() {

                loadView(
                                "/fxml/orders-view.fxml");

                statusLabel.setText(
                                "Раздел «Заказы»");
        }

        // =====================================================
        // КЛИЕНТЫ
        // =====================================================

        @FXML
        private void onClientsButtonClick() {

                loadView(
                                "/fxml/clients-view.fxml");

                statusLabel.setText(
                                "Раздел «Клиенты»");
        }

        @FXML
        private void onStatisticsButtonClick() {

                loadView(
                                "/fxml/statistics-view.fxml");

                statusLabel.setText(
                                "Раздел «Статистика»");
        }
        // =====================================================
        // ЗАГРУЗКА FXML
        // =====================================================

        private void loadView(
                        String path) {

                try {

                        Node view = FXMLLoader.load(
                                        getClass()
                                                        .getResource(path));

                        contentPane
                                        .getChildren()
                                        .setAll(view);

                } catch (IOException e) {

                        showError(
                                        "Не удалось открыть раздел",
                                        e.getMessage());
                }
        }

        // =====================================================
        // ERROR
        // =====================================================

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