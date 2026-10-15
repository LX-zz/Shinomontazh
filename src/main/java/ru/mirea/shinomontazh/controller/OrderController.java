package ru.mirea.shinomontazh.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import ru.mirea.shinomontazh.model.Client;
import ru.mirea.shinomontazh.model.OrderStatus;
import ru.mirea.shinomontazh.model.WorkOrder;
import ru.mirea.shinomontazh.service.ClientService;
import ru.mirea.shinomontazh.service.WorkOrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javafx.stage.FileChooser;
import ru.mirea.shinomontazh.util.ExcelExporter;

import java.io.File;
import java.util.ArrayList;

public class OrderController {

        // =====================================================
        // ТАБЛИЦА
        // =====================================================

        @FXML
        private TableView<WorkOrder> ordersTable;

        @FXML
        private TableColumn<WorkOrder, Integer> idColumn;

        @FXML
        private TableColumn<WorkOrder, String> carBrandColumn;

        @FXML
        private TableColumn<WorkOrder, String> carNumberColumn;

        @FXML
        private TableColumn<WorkOrder, String> serviceColumn;

        @FXML
        private TableColumn<WorkOrder, OrderStatus> statusColumn;

        @FXML
        private TableColumn<WorkOrder, BigDecimal> priceColumn;

        @FXML
        private TableColumn<WorkOrder, LocalDateTime> dateColumn;

        @FXML
        private TableColumn<WorkOrder, Integer> clientIdColumn;

        // =====================================================
        // ПОИСК / ФИЛЬТРЫ / СОРТИРОВКА
        // =====================================================

        @FXML
        private TextField searchField;

        @FXML
        private ComboBox<OrderStatus> statusFilter;

        @FXML
        private ComboBox<Client> clientFilter;

        @FXML
        private ComboBox<String> sortComboBox;

        @FXML
        private Label resultCountLabel;

        @FXML
        private void onExportButtonClick() {

                try {

                        if (ordersTable.getItems().isEmpty()) {

                                showError(
                                                "Нет данных",
                                                "В таблице нет заказов для экспорта.");

                                return;
                        }

                        FileChooser fileChooser = new FileChooser();

                        fileChooser.setTitle(
                                        "Сохранить заказы");

                        fileChooser.setInitialFileName(
                                        "orders.xlsx");

                        fileChooser.getExtensionFilters().add(
                                        new FileChooser.ExtensionFilter(
                                                        "Excel (*.xlsx)",
                                                        "*.xlsx"));

                        File selectedFile = fileChooser.showSaveDialog(
                                        ordersTable
                                                        .getScene()
                                                        .getWindow());

                        if (selectedFile == null) {
                                return;
                        }

                        String filePath = selectedFile.getAbsolutePath();

                        if (!filePath
                                        .toLowerCase()
                                        .endsWith(".xlsx")) {

                                filePath += ".xlsx";
                        }

                        ExcelExporter exporter = new ExcelExporter();

                        exporter.export(
                                        new ArrayList<>(
                                                        ordersTable.getItems()),
                                        filePath);

                        showInfo(
                                        "Экспорт выполнен",
                                        "Файл успешно сохранён:\n"
                                                        + filePath);

                } catch (Exception e) {

                        showError(
                                        "Ошибка экспорта",
                                        e.getMessage());
                }
        }

        // =====================================================
        // SERVICE
        // =====================================================

        private final WorkOrderService workOrderService;
        private final ClientService clientService;

        // =====================================================
        // ВСЕ ЗАКАЗЫ ИЗ БАЗЫ
        // =====================================================

        private final ObservableList<WorkOrder> allOrders = FXCollections.observableArrayList();

        public OrderController() {

                workOrderService = new WorkOrderService();

                clientService = new ClientService();
        }

        // =====================================================
        // INITIALIZE
        // =====================================================

        @FXML
        public void initialize() {

                configureTable();

                configureFilters();

                loadOrders();
        }

        // =====================================================
        // НАСТРОЙКА ТАБЛИЦЫ
        // =====================================================

        private void configureTable() {

                idColumn.setCellValueFactory(
                                new PropertyValueFactory<>("id"));

                carBrandColumn.setCellValueFactory(
                                new PropertyValueFactory<>("carBrand"));

                carNumberColumn.setCellValueFactory(
                                new PropertyValueFactory<>("carNumber"));

                serviceColumn.setCellValueFactory(
                                new PropertyValueFactory<>("serviceName"));

                statusColumn.setCellValueFactory(
                                new PropertyValueFactory<>("status"));

                priceColumn.setCellValueFactory(
                                new PropertyValueFactory<>("price"));

                dateColumn.setCellValueFactory(
                                new PropertyValueFactory<>("createDate"));

                clientIdColumn.setCellValueFactory(
                                new PropertyValueFactory<>("clientId"));
        }

        // =====================================================
        // НАСТРОЙКА ФИЛЬТРОВ
        // =====================================================

        private void configureFilters() {

                // ---------------------------------------------
                // Статусы
                // ---------------------------------------------

                statusFilter.setItems(
                                FXCollections.observableArrayList(
                                                OrderStatus.values()));

                statusFilter.setPromptText(
                                "Все статусы");

                // ---------------------------------------------
                // Клиенты
                // ---------------------------------------------

                try {

                        List<Client> clients = clientService.getAllClients();

                        clientFilter.setItems(
                                        FXCollections.observableArrayList(
                                                        clients));

                } catch (Exception e) {

                        showError(
                                        "Ошибка загрузки клиентов",
                                        e.getMessage());
                }

                clientFilter.setPromptText(
                                "Все клиенты");

                clientFilter.setConverter(
                                new StringConverter<>() {

                                        @Override
                                        public String toString(
                                                        Client client) {

                                                if (client == null) {

                                                        return "";
                                                }

                                                return client.getId()
                                                                + " — "
                                                                + client.getFullName();
                                        }

                                        @Override
                                        public Client fromString(
                                                        String string) {

                                                return null;
                                        }
                                });

                // ---------------------------------------------
                // Сортировка
                // ---------------------------------------------

                sortComboBox.setItems(
                                FXCollections.observableArrayList(
                                                "Без сортировки",
                                                "Цена ↑",
                                                "Цена ↓",
                                                "Дата ↑",
                                                "Дата ↓"));

                sortComboBox.setValue(
                                "Без сортировки");
        }

        // =====================================================
        // ЗАГРУЗКА ЗАКАЗОВ ИЗ POSTGRESQL
        // =====================================================

        private void loadOrders() {

                try {

                        allOrders.setAll(
                                        workOrderService.getAllOrders());

                        applyFiltersAndSorting();

                } catch (Exception e) {

                        showError(
                                        "Ошибка загрузки заказов",
                                        e.getMessage());
                }
        }

        // =====================================================
        // ПОИСК + ФИЛЬТРЫ + СОРТИРОВКА
        // =====================================================

        private void applyFiltersAndSorting() {

                Stream<WorkOrder> stream = allOrders.stream();

                // =================================================
                // ПОИСК
                // По марке ИЛИ госномеру
                // =================================================

                String searchText = searchField
                                .getText()
                                .trim()
                                .toLowerCase(Locale.ROOT);

                if (!searchText.isEmpty()) {

                        stream = stream.filter(
                                        order -> {

                                                String brand = order
                                                                .getCarBrand()
                                                                .toLowerCase(
                                                                                Locale.ROOT);

                                                String number = order
                                                                .getCarNumber()
                                                                .toLowerCase(
                                                                                Locale.ROOT);

                                                return brand.contains(
                                                                searchText)
                                                                ||
                                                                number.contains(
                                                                                searchText);
                                        });
                }

                // =================================================
                // ФИЛЬТР №1
                // СТАТУС
                // =================================================

                OrderStatus selectedStatus = statusFilter.getValue();

                if (selectedStatus != null) {

                        stream = stream.filter(
                                        order -> order.getStatus() == selectedStatus);
                }

                // =================================================
                // ФИЛЬТР №2
                // КЛИЕНТ
                // =================================================

                Client selectedClient = clientFilter.getValue();

                if (selectedClient != null) {

                        stream = stream.filter(
                                        order -> order.getClientId() == selectedClient.getId());
                }

                // =================================================
                // СОРТИРОВКА
                // =================================================

                String sortType = sortComboBox.getValue();

                if (sortType != null) {

                        switch (sortType) {

                                case "Цена ↑" ->

                                        stream = stream.sorted(
                                                        Comparator.comparing(
                                                                        WorkOrder::getPrice));

                                case "Цена ↓" ->

                                        stream = stream.sorted(
                                                        Comparator.comparing(
                                                                        WorkOrder::getPrice).reversed());

                                case "Дата ↑" ->

                                        stream = stream.sorted(
                                                        Comparator.comparing(
                                                                        WorkOrder::getCreateDate));

                                case "Дата ↓" ->

                                        stream = stream.sorted(
                                                        Comparator.comparing(
                                                                        WorkOrder::getCreateDate).reversed());

                                default -> {

                                        // Без сортировки
                                }
                        }
                }

                // =================================================
                // РЕЗУЛЬТАТ
                // =================================================

                List<WorkOrder> result = stream.toList();

                ordersTable.setItems(
                                FXCollections.observableArrayList(
                                                result));

                resultCountLabel.setText(
                                "Найдено заказов: "
                                                + result.size());
        }

        // =====================================================
        // КНОПКА НАЙТИ / ПРИМЕНИТЬ
        // =====================================================

        @FXML
        private void onSearchButtonClick() {

                applyFiltersAndSorting();
        }

        // =====================================================
        // СБРОСИТЬ
        // =====================================================

        @FXML
        private void onResetButtonClick() {

                searchField.clear();

                statusFilter.setValue(
                                null);

                clientFilter.setValue(
                                null);

                sortComboBox.setValue(
                                "Без сортировки");

                applyFiltersAndSorting();
        }

        // =====================================================
        // ОБНОВИТЬ
        // =====================================================

        @FXML
        private void onRefreshButtonClick() {

                loadOrders();
        }

        // =====================================================
        // ДОБАВИТЬ
        // =====================================================

        @FXML
        private void onAddButtonClick() {

                try {

                        Optional<WorkOrder> result = showOrderDialog(null);

                        if (result.isPresent()) {

                                workOrderService.createOrder(
                                                result.get());

                                loadOrders();

                                showInfo(
                                                "Заказ создан",
                                                "Новый заказ успешно добавлен.");
                        }

                } catch (Exception e) {

                        showError(
                                        "Ошибка добавления заказа",
                                        e.getMessage());
                }
        }

        // =====================================================
        // ИЗМЕНИТЬ
        // =====================================================

        @FXML
        private void onEditButtonClick() {

                WorkOrder selectedOrder = ordersTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (selectedOrder == null) {

                        showError(
                                        "Заказ не выбран",
                                        "Сначала выберите заказ в таблице.");

                        return;
                }

                try {

                        Optional<WorkOrder> result = showOrderDialog(
                                        selectedOrder);

                        if (result.isPresent()) {

                                workOrderService.updateOrder(
                                                result.get());

                                loadOrders();

                                showInfo(
                                                "Заказ изменён",
                                                "Данные заказа успешно обновлены.");
                        }

                } catch (Exception e) {

                        showError(
                                        "Ошибка изменения заказа",
                                        e.getMessage());
                }
        }

        // =====================================================
        // УДАЛИТЬ
        // =====================================================

        @FXML
        private void onDeleteButtonClick() {

                WorkOrder selectedOrder = ordersTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (selectedOrder == null) {

                        showError(
                                        "Заказ не выбран",
                                        "Сначала выберите заказ в таблице.");

                        return;
                }

                Alert confirm = new Alert(
                                Alert.AlertType.CONFIRMATION);

                confirm.setTitle(
                                "Подтверждение удаления");

                confirm.setHeaderText(
                                "Удалить заказ №"
                                                + selectedOrder.getId()
                                                + "?");

                confirm.setContentText(
                                selectedOrder.getCarBrand()
                                                + " | "
                                                + selectedOrder.getCarNumber());

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isEmpty()
                                ||
                                result.get() != ButtonType.OK) {

                        return;
                }

                try {

                        workOrderService.deleteOrder(
                                        selectedOrder.getId());

                        loadOrders();

                        showInfo(
                                        "Заказ удалён",
                                        "Заказ успешно удалён.");

                } catch (Exception e) {

                        showError(
                                        "Ошибка удаления заказа",
                                        e.getMessage());
                }
        }

        // =====================================================
        // ФОРМА ДОБАВЛЕНИЯ / РЕДАКТИРОВАНИЯ
        // =====================================================

        private Optional<WorkOrder> showOrderDialog(
                        WorkOrder existingOrder) throws Exception {

                boolean editMode = existingOrder != null;

                Dialog<WorkOrder> dialog = new Dialog<>();

                if (editMode) {

                        dialog.setTitle(
                                        "Редактирование заказа");

                        dialog.setHeaderText(
                                        "Изменение заказа №"
                                                        + existingOrder.getId());

                } else {

                        dialog.setTitle(
                                        "Добавление заказа");

                        dialog.setHeaderText(
                                        "Введите данные нового заказа");
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

                TextField carBrandField = new TextField();

                TextField carNumberField = new TextField();

                TextField serviceField = new TextField();

                TextField priceField = new TextField();

                ComboBox<OrderStatus> statusBox = new ComboBox<>();

                statusBox.setItems(
                                FXCollections.observableArrayList(
                                                OrderStatus.values()));

                ComboBox<Client> clientBox = new ComboBox<>();

                List<Client> clients = clientService.getAllClients();

                clientBox.setItems(
                                FXCollections.observableArrayList(
                                                clients));

                clientBox.setPrefWidth(
                                250);

                clientBox.setConverter(
                                new StringConverter<>() {

                                        @Override
                                        public String toString(
                                                        Client client) {

                                                if (client == null) {

                                                        return "";
                                                }

                                                return client.getId()
                                                                + " — "
                                                                + client.getFullName();
                                        }

                                        @Override
                                        public Client fromString(
                                                        String string) {

                                                return null;
                                        }
                                });

                // =================================================
                // РЕДАКТИРОВАНИЕ
                // =================================================

                if (editMode) {

                        carBrandField.setText(
                                        existingOrder.getCarBrand());

                        carNumberField.setText(
                                        existingOrder.getCarNumber());

                        serviceField.setText(
                                        existingOrder.getServiceName());

                        priceField.setText(
                                        existingOrder
                                                        .getPrice()
                                                        .toString());

                        statusBox.setValue(
                                        existingOrder.getStatus());

                        for (Client client : clients) {

                                if (client.getId() == existingOrder.getClientId()) {

                                        clientBox.setValue(
                                                        client);

                                        break;
                                }
                        }

                } else {

                        statusBox.setValue(
                                        OrderStatus.NEW);

                        carBrandField.setPromptText(
                                        "Toyota Camry");

                        carNumberField.setPromptText(
                                        "А001МР77");

                        serviceField.setPromptText(
                                        "Замена шин");

                        priceField.setPromptText(
                                        "4500");
                }

                // =================================================
                // GRID
                // =================================================

                GridPane grid = new GridPane();

                grid.setHgap(
                                10);

                grid.setVgap(
                                10);

                grid.setPadding(
                                new Insets(
                                                20,
                                                20,
                                                10,
                                                20));

                grid.add(
                                new Label(
                                                "Марка автомобиля:"),
                                0,
                                0);

                grid.add(
                                carBrandField,
                                1,
                                0);

                grid.add(
                                new Label(
                                                "Госномер:"),
                                0,
                                1);

                grid.add(
                                carNumberField,
                                1,
                                1);

                grid.add(
                                new Label(
                                                "Услуга:"),
                                0,
                                2);

                grid.add(
                                serviceField,
                                1,
                                2);

                grid.add(
                                new Label(
                                                "Цена:"),
                                0,
                                3);

                grid.add(
                                priceField,
                                1,
                                3);

                grid.add(
                                new Label(
                                                "Статус:"),
                                0,
                                4);

                grid.add(
                                statusBox,
                                1,
                                4);

                grid.add(
                                new Label(
                                                "Клиент:"),
                                0,
                                5);

                grid.add(
                                clientBox,
                                1,
                                5);

                dialog.getDialogPane()
                                .setContent(
                                                grid);

                // =================================================
                // РЕЗУЛЬТАТ
                // =================================================

                dialog.setResultConverter(
                                button -> {

                                        if (button != saveButton) {

                                                return null;
                                        }

                                        try {

                                                Client selectedClient = clientBox.getValue();

                                                if (selectedClient == null) {

                                                        showError(
                                                                        "Ошибка",
                                                                        "Выберите клиента.");

                                                        return null;
                                                }

                                                BigDecimal price = new BigDecimal(
                                                                priceField
                                                                                .getText()
                                                                                .trim());

                                                WorkOrder order = new WorkOrder();

                                                if (editMode) {

                                                        order.setId(
                                                                        existingOrder.getId());

                                                        order.setCreateDate(
                                                                        existingOrder.getCreateDate());
                                                }

                                                order.setCarBrand(
                                                                carBrandField
                                                                                .getText()
                                                                                .trim());

                                                order.setCarNumber(
                                                                carNumberField
                                                                                .getText()
                                                                                .trim());

                                                order.setServiceName(
                                                                serviceField
                                                                                .getText()
                                                                                .trim());

                                                order.setPrice(
                                                                price);

                                                order.setStatus(
                                                                statusBox.getValue());

                                                order.setClientId(
                                                                selectedClient.getId());

                                                return order;

                                        } catch (NumberFormatException e) {

                                                showError(
                                                                "Ошибка",
                                                                "Цена должна быть числом.");

                                                return null;
                                        }
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