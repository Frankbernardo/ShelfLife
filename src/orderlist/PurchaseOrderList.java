package orderlist;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import product.ProductPage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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

        TableColumn<PurchaseOrder, Integer> orderNumberColumn = new TableColumn<>("Order Number");
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

    private void loadOrdersFromDatabase() {
        String query = "SELECT order_id, order_date, total_price FROM orders";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(new PurchaseOrder(
                    rs.getInt("order_id"),
                    rs.getDate("order_date").toLocalDate(),
                    rs.getDouble("total_price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class ActionCell extends TableCell<PurchaseOrder, Void> {
        private final Button actionButton = new Button("View");

        public ActionCell() {
            actionButton.setOnAction(event -> {
                PurchaseOrder order = getTableView().getItems().get(getIndex());
                System.out.println("Viewing order: " + order);
                // You could open a new window or a dialog with the order details here
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

    public static void main(String[] args) {
        launch(args);
    }
}