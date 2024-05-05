package orderlist;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import product.ProductPage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderList extends Application {
    private ObservableList<PurchaseOrder> orders = FXCollections.observableArrayList();
    private TableView<PurchaseOrder> tableView = new TableView<>();
    private String url = "jdbc:mysql://localhost:3306/InventoryDB";
    private String dbUser = "root";
    private String dbPassword = "Misa70656";

    @Override
    public void start(Stage primaryStage) {
        loadOrdersFromDatabase();
        tableView.setItems(orders);
        tableView.setEditable(false);

        TableColumn<PurchaseOrder, Number> orderNumberColumn = new TableColumn<>("Order Number");
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));

        TableColumn<PurchaseOrder, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<PurchaseOrder, Double> totalColumn = new TableColumn<>("Total Price");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<PurchaseOrder, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(column -> new ActionCell());

        tableView.getColumns().addAll(orderNumberColumn, dateColumn, totalColumn, actionColumn);

        Button btnGoBack = new Button("Go Back");
        btnGoBack.setOnAction(event -> {
            primaryStage.close();
            ProductPage productPage = new ProductPage();
            productPage.start(new Stage());
        });

        HBox buttonBar = new HBox(10, btnGoBack);
        buttonBar.setPadding(new Insets(15));
        buttonBar.setStyle("-fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setCenter(tableView);
        root.setBottom(buttonBar);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Purchase Order List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class ActionCell extends TableCell<PurchaseOrder, Void> {
        private final Button actionButton = new Button("View");

        public ActionCell() {
            actionButton.setOnAction(event -> {
                PurchaseOrder order = getTableView().getItems().get(getIndex());
                viewOrderDetails(order);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(actionButton);
            }
        }
    }

    private void viewOrderDetails(PurchaseOrder order) {
        Stage detailsStage = new Stage();
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.setTitle("Order Details - " + order.getOrderNumber());

        ObservableList<orderlist> orderItems = FXCollections.observableArrayList(order.getItems());
        TableView<orderlist> itemsTable = new TableView<>(orderItems);

        if (orderItems.isEmpty()) {
            BorderPane detailsRoot = new BorderPane(new Text("No contents"));
            Scene detailsScene = new Scene(detailsRoot, 600, 400);
            detailsStage.setScene(detailsScene);
            detailsStage.showAndWait();
            return;
        }

        TableColumn<orderlist, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<orderlist, String> SKUColumn = new TableColumn<>("SKU");
        SKUColumn.setCellValueFactory(new PropertyValueFactory<>("SKU"));

        TableColumn<orderlist, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<orderlist, Integer> quantityColumn = new TableColumn<>("Input");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("input"));

        itemsTable.getColumns().addAll(itemColumn, SKUColumn, priceColumn, quantityColumn);

        BorderPane detailsRoot = new BorderPane();
        detailsRoot.setCenter(itemsTable);
        Scene detailsScene = new Scene(detailsRoot, 600, 400);

        detailsStage.setScene(detailsScene);
        detailsStage.showAndWait();
    }

    private void loadOrdersFromDatabase() {
        String orderQuery = "SELECT order_id, date, total_price FROM orders";
        String orderItemsQuery = "SELECT products.name, products.SKU, order_products.price_per_item, order_products.quantity " +
                "FROM order_products " +
                "JOIN products ON order_products.product_id = products.product_id " +
                "WHERE order_products.order_id = ?";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement orderStmt = conn.prepareStatement(orderQuery);
             PreparedStatement itemsStmt = conn.prepareStatement(orderItemsQuery)) {

            ResultSet orderRs = orderStmt.executeQuery();
            while (orderRs.next()) {
                int orderId = orderRs.getInt("order_id");
                LocalDate date = orderRs.getDate("date").toLocalDate();
                double totalPrice = orderRs.getDouble("total_price");

                itemsStmt.setInt(1, orderId);
                ResultSet itemsRs = itemsStmt.executeQuery();
                List<orderlist> orderItems = new ArrayList<>();

                while (itemsRs.next()) {
                    String productName = itemsRs.getString("name");
                    String sku = itemsRs.getString("SKU");
                    double pricePerItem = itemsRs.getDouble("price_per_item");
                    int quantity = itemsRs.getInt("quantity");

                    orderItems.add(new orderlist(productName, sku, pricePerItem, 0, Integer.toString(quantity)));
                }

                orders.add(new PurchaseOrder(orderId, date, totalPrice, orderItems));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

