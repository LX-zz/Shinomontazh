package ru.mirea.shinomontazh.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import ru.mirea.shinomontazh.model.Client;
import ru.mirea.shinomontazh.service.ClientService;

import java.util.Optional;

public class ClientController {

        @FXML
        private TableView<Client> clientsTable;

        @FXML
        private TableColumn<Client, Integer> idColumn;

        @FXML
        private TableColumn<Client, String> fullNameColumn;

        @FXML
        private TableColumn<Client, String> phoneColumn;

        @FXML
        private TableColumn<Client, String> emailColumn;

        private final ClientService clientService;

        public ClientController() {
                clientService = new ClientService();
        }

        @FXML
        public void initialize() {

                idColumn.setCellValueFactory(
                                new PropertyValueFactory<>("id"));

                fullNameColumn.setCellValueFactory(
                                new PropertyValueFactory<>("fullName"));

                phoneColumn.setCellValueFactory(
                                new PropertyValueFactory<>("phone"));

                emailColumn.setCellValueFactory(
                                new PropertyValueFactory<>("email"));

                loadClients();
        }

        // =====================================================
        // ЗАГРУЗКА КЛИЕНТОВ
        // =====================================================

        private void loadClients() {

                try {

                        ObservableList<Client> clients = FXCollections.observableArrayList(
                                        clientService.getAllClients());

                        clientsTable.setItems(clients);

                } catch (Exception e) {

                        showError(
                                        "Ошибка загрузки клиентов",
                                        e.getMessage());
                }
        }

        // =====================================================
        // ОБНОВИТЬ
        // =====================================================

        @FXML
        private void onRefreshButtonClick() {

                loadClients();
        }

        // =====================================================
        // ДОБАВИТЬ
        // =====================================================

        @FXML
        private void onAddButtonClick() {

                try {

                        Optional<Client> result = showClientDialog(null);

                        if (result.isPresent()) {

                                clientService.createClient(
                                                result.get());

                                loadClients();

                                showInfo(
                                                "Клиент создан",
                                                "Новый клиент успешно добавлен.");
                        }

                } catch (Exception e) {

                        showError(
                                        "Ошибка добавления клиента",
                                        e.getMessage());
                }
        }

        // =====================================================
        // ИЗМЕНИТЬ
        // =====================================================

        @FXML
        private void onEditButtonClick() {

                Client selectedClient = clientsTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (selectedClient == null) {

                        showError(
                                        "Клиент не выбран",
                                        "Сначала выберите клиента в таблице.");

                        return;
                }

                try {

                        Optional<Client> result = showClientDialog(selectedClient);

                        if (result.isPresent()) {

                                clientService.updateClient(
                                                result.get());

                                loadClients();

                                showInfo(
                                                "Клиент изменён",
                                                "Данные клиента успешно обновлены.");
                        }

                } catch (Exception e) {

                        showError(
                                        "Ошибка изменения клиента",
                                        e.getMessage());
                }
        }

        // =====================================================
        // УДАЛИТЬ
        // =====================================================

        @FXML
        private void onDeleteButtonClick() {

                Client selectedClient = clientsTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (selectedClient == null) {

                        showError(
                                        "Клиент не выбран",
                                        "Сначала выберите клиента в таблице.");

                        return;
                }

                Alert confirm = new Alert(
                                Alert.AlertType.CONFIRMATION);

                confirm.setTitle(
                                "Подтверждение удаления");

                confirm.setHeaderText(
                                "Удалить клиента №"
                                                + selectedClient.getId()
                                                + "?");

                confirm.setContentText(
                                selectedClient.getFullName()
                                                + "\n\n"
                                                + "Внимание: связанные заказы клиента "
                                                + "также могут быть удалены.");

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isEmpty()
                                || result.get() != ButtonType.OK) {

                        return;
                }

                try {

                        clientService.deleteClient(
                                        selectedClient.getId());

                        loadClients();

                        showInfo(
                                        "Клиент удалён",
                                        "Клиент №"
                                                        + selectedClient.getId()
                                                        + " успешно удалён.");

                } catch (Exception e) {

                        showError(
                                        "Ошибка удаления клиента",
                                        e.getMessage());
                }
        }

        // =====================================================
        // ОБЩАЯ ФОРМА
        // ДОБАВЛЕНИЕ / РЕДАКТИРОВАНИЕ
        // =====================================================

        private Optional<Client> showClientDialog(
                        Client existingClient) {

                boolean editMode = existingClient != null;

                Dialog<Client> dialog = new Dialog<>();

                if (editMode) {

                        dialog.setTitle(
                                        "Редактирование клиента");

                        dialog.setHeaderText(
                                        "Изменение клиента №"
                                                        + existingClient.getId());

                } else {

                        dialog.setTitle(
                                        "Добавление клиента");

                        dialog.setHeaderText(
                                        "Введите данные нового клиента");
                }

                ButtonType saveButton = new ButtonType(
                                "Сохранить",
                                ButtonBar.ButtonData.OK_DONE);

                dialog.getDialogPane()
                                .getButtonTypes()
                                .addAll(
                                                saveButton,
                                                ButtonType.CANCEL);

                // =================================================
                // ПОЛЯ
                // =================================================

                TextField fullNameField = new TextField();

                TextField phoneField = new TextField();

                TextField emailField = new TextField();

                if (editMode) {

                        fullNameField.setText(
                                        existingClient.getFullName());

                        phoneField.setText(
                                        existingClient.getPhone());

                        emailField.setText(
                                        existingClient.getEmail());

                } else {

                        fullNameField.setPromptText(
                                        "Иван Иванов");

                        phoneField.setPromptText(
                                        "+79991234567");

                        emailField.setPromptText(
                                        "ivan@mail.ru");
                }

                // =================================================
                // GRID
                // =================================================

                GridPane grid = new GridPane();

                grid.setHgap(10);
                grid.setVgap(10);

                grid.setPadding(
                                new Insets(
                                                20,
                                                20,
                                                10,
                                                20));

                grid.add(
                                new Label("ФИО:"),
                                0,
                                0);

                grid.add(
                                fullNameField,
                                1,
                                0);

                grid.add(
                                new Label("Телефон:"),
                                0,
                                1);

                grid.add(
                                phoneField,
                                1,
                                1);

                grid.add(
                                new Label("Email:"),
                                0,
                                2);

                grid.add(
                                emailField,
                                1,
                                2);

                dialog.getDialogPane()
                                .setContent(grid);

                // =================================================
                // РЕЗУЛЬТАТ
                // =================================================

                dialog.setResultConverter(
                                button -> {

                                        if (button != saveButton) {

                                                return null;
                                        }

                                        Client client = new Client();

                                        if (editMode) {

                                                client.setId(
                                                                existingClient.getId());
                                        }

                                        client.setFullName(
                                                        fullNameField
                                                                        .getText()
                                                                        .trim());

                                        client.setPhone(
                                                        phoneField
                                                                        .getText()
                                                                        .trim());

                                        client.setEmail(
                                                        emailField
                                                                        .getText()
                                                                        .trim());

                                        return client;
                                });

                return dialog.showAndWait();
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

        // =====================================================
        // INFO
        // =====================================================

        private void showInfo(
                        String title,
                        String message) {

                Alert alert = new Alert(
                                Alert.AlertType.INFORMATION);

                alert.setTitle(
                                "Информация");

                alert.setHeaderText(
                                title);

                alert.setContentText(
                                message);

                alert.showAndWait();
        }
}