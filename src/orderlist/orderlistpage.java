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

public class orderlistpage extends Application {

    private ObservableList<orderlist> orderItems = FXCollections.observableArrayList();
    private TableView<orderlist> tableView = new TableView<>();
    private String url = "jdbc:mysql://localhost:3306/InventoryDB";
    private String dbUser = "root";
    private String dbPassword = "Misa70656";

    @Override
    public void start(Stage primaryStage) {
        loadProductsFromDatabase();
        tableView.setItems(orderItems);
        tableView.setEditable(true);

        TableColumn<orderlist, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<orderlist, String> SKUColumn = new TableColumn<>("SKU");
        SKUColumn.setCellValueFactory(new PropertyValueFactory<>("SKU"));

        TableColumn<orderlist, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<orderlist, Integer> inventoryColumn = new TableColumn<>("Inventory");
        inventoryColumn.setCellValueFactory(new PropertyValueFactory<>("inventory"));

        TableColumn<orderlist, String> inputColumn = new TableColumn<>("Input");
        inputColumn.setCellValueFactory(new PropertyValueFactory<>("input"));
        inputColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        inputColumn.setOnEditCommit(event -> {
            orderlist item = event.getRowValue();
            item.setInput(event.getNewValue());
            System.out.println("Item: " + item.getItem() + ", Input: " + item.getInput());  // Logging input set
        });
        
        TableColumn<orderlist, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        inputColumn.setOnEditCommit(event -> {
            orderlist item = event.getRowValue();
            item.setInput(event.getNewValue());
            try {
                double total = Double.parseDouble(item.getInput()) * item.getPrice();
                item.setTotal(total);  // Assuming there is a setTotal method in orderlist class
                System.out.println("Updated Total: " + total);
            } catch (NumberFormatException e) {
                System.err.println("Invalid input for quantity");
            }
        });

        tableView.getColumns().addAll(itemColumn, SKUColumn, priceColumn, inventoryColumn, inputColumn);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            ProductPage productPage = new ProductPage();
            productPage.start(new Stage());
        });

        Button reviewButton = new Button("Review");
        reviewButton.setOnAction(e -> {
            ObservableList<orderlist> currentItems = FXCollections.observableArrayList(tableView.getItems());
            for (orderlist item : currentItems) {
                System.out.println("Passing Item: " + item.getItem() + ", Input: " + item.getInput());  // Confirm what's being passed
            }

            revieworderlist reviewPage = new revieworderlist(currentItems);
            Stage reviewStage = new Stage();
            reviewPage.start(reviewStage);

            Stage currentStage = (Stage) reviewButton.getScene().getWindow();
            currentStage.close();
        });

        HBox buttonBar = new HBox(10, backButton, reviewButton);
        buttonBar.setStyle("-fx-alignment: center; -fx-padding: 10px;");

        BorderPane root = new BorderPane();
        root.setCenter(tableView);
        root.setBottom(buttonBar);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Order List Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadProductsFromDatabase() {
        String query = "SELECT name, SKU, price, quantity FROM products JOIN inventory ON products.product_id = inventory.product_id";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orderItems.add(new orderlist(
                        rs.getString("name"),
                        rs.getString("SKU"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        "0"  
                    ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
