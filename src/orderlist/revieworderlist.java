package orderlist;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import product.ProductPage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class revieworderlist extends Application {
    private ObservableList<orderlist> orderItems;

    public revieworderlist(ObservableList<orderlist> orderItems) {
        this.orderItems = FXCollections.observableArrayList(orderItems);
    }

    private TableView<orderlist> tableView = new TableView<>();
    private String url = "jdbc:mysql://localhost:3306/InventoryDB";
    private String dbUser = "root";
    private String dbPassword = "Misa70656";

    @Override
    public void start(Stage primaryStage) {
        tableView.setItems(orderItems);
        tableView.setEditable(true);

        TableColumn<orderlist, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<orderlist, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<orderlist, String> inputColumn = new TableColumn<>("Input");
        inputColumn.setCellValueFactory(new PropertyValueFactory<>("input"));
        inputColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<orderlist, String> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalColumn.setCellFactory(column -> new TableCell<orderlist, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    orderlist orderItem = getTableView().getItems().get(getIndex());
                    double total = orderItem.calculateTotal();
                    setText(String.format("%.2f", total));
                }
            }
        });

        tableView.getColumns().addAll(itemColumn, priceColumn, inputColumn, totalColumn);

        Button btnGoBack = new Button("Go Back");
        btnGoBack.setOnAction(event -> {
            Stage currentStage = (Stage) btnGoBack.getScene().getWindow();
            currentStage.close();

            orderlistpage orderlistScreen = new orderlistpage();
            orderlistScreen.start(new Stage());
        });

        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(event -> {
            submitOrder(orderItems);
            Stage currentStage = (Stage) btnSubmit.getScene().getWindow();
            currentStage.close();

            ProductPage productPage = new ProductPage();
            productPage.start(new Stage());
        });

        HBox buttonBar = new HBox(10, btnGoBack, btnSubmit);
        buttonBar.setPadding(new Insets(15));
        buttonBar.setStyle("-fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setCenter(tableView);
        root.setBottom(buttonBar);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Review Order List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void submitOrder(List<orderlist> items) {
        String insertOrderSQL = "INSERT INTO orders (total_price, date) VALUES (?, ?)";
        String insertOrderItemSQL = "INSERT INTO order_products (order_id, product_id, quantity, price_per_item) VALUES (?, ?, ?, ?)";
        String getProductIdSQL = "SELECT product_id FROM products WHERE SKU = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, new String[]{"order_id"});
                 PreparedStatement itemStmt = conn.prepareStatement(insertOrderItemSQL);
                 PreparedStatement productIdStmt = conn.prepareStatement(getProductIdSQL)) {

                double totalPrice = items.stream().mapToDouble(orderlist::calculateTotal).sum();
                orderStmt.setDouble(1, totalPrice);
                orderStmt.setDate(2, Date.valueOf(LocalDate.now()));
                orderStmt.executeUpdate();

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);

                        for (orderlist item : items) {
                            try {
                                int quantity = Integer.parseInt(item.getInput());
                                if (quantity <= 0) {
                                    System.err.println("Invalid quantity: " + item.getInput());
                                    continue;
                                }

                                productIdStmt.setString(1, item.getSKU());
                                ResultSet productRs = productIdStmt.executeQuery();

                                if (productRs.next()) {
                                    int productId = productRs.getInt("product_id");

                                    itemStmt.setInt(1, orderId);
                                    itemStmt.setInt(2, productId);
                                    itemStmt.setInt(3, quantity);
                                    itemStmt.setDouble(4, item.getPrice());
                                    itemStmt.addBatch();
                                    System.out.println("Passing Item: " + item.getItem() + ", Input: " + item.getInput());
                                } else {
                                    System.err.println("Invalid SKU: " + item.getSKU());
                                }

                                productRs.close();

                            } catch (NumberFormatException e) {
                                System.err.println("Invalid SKU or quantity: " + e.getMessage());
                            }
                        }

                        itemStmt.executeBatch();
                    }
                }

                conn.commit(); // Commit transaction

            } catch (SQLException e) {
                conn.rollback(); // Rollback on error
                throw e;
            }

            System.out.println("Order submitted.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error submitting order: " + ex.getMessage());
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}

