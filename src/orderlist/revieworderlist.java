package orderlist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
import javafx.beans.property.ReadOnlyObjectWrapper;

public class revieworderlist extends Application {
    private ObservableList<orderlist> orderItems;

    public revieworderlist(ObservableList<orderlist> orderItems) {
        this.orderItems = FXCollections.observableArrayList(orderItems);  
    }

    private TableView<orderlist> tableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        tableView.setItems(orderItems);
        tableView.setEditable(false);

        TableColumn<orderlist, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<orderlist, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<orderlist, String> inputColumn = new TableColumn<>("Input");
        inputColumn.setCellValueFactory(new PropertyValueFactory<>("input"));

        TableColumn<orderlist, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().calculateTotal()));
        totalColumn.setCellFactory(column -> new TableCell<orderlist, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });

        tableView.getColumns().addAll(itemColumn, priceColumn, inputColumn, totalColumn);

        Button btnGoBack = new Button("Go Back");
        btnGoBack.setOnAction(event -> {
            primaryStage.close();
            orderlistpage orderlistScreen = new orderlistpage();
            orderlistScreen.start(new Stage());
        });

        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(event -> {
        	submitOrder(tableView.getItems());
            primaryStage.close();
            System.out.println("Order submitted.");
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
    
    public void submitOrder(ObservableList<orderlist> orderItems) {
        String url = "jdbc:mysql://localhost:3306/InventoryDB";// Update database name
        String user = "root";
        String password = "Misa70656";

        String sql = "INSERT INTO orders (product_name, sku, quantity, unit_price, total_price, order_date) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (orderlist item : orderItems) {
                pstmt.setString(1, item.getItem());
                pstmt.setString(2, item.getSKU());
                pstmt.setInt(3, Integer.parseInt(item.getInput()));
                pstmt.setDouble(4, item.getPrice());
                pstmt.setDouble(5, item.getPrice() * Integer.parseInt(item.getInput()));
                pstmt.executeUpdate();
            }
            System.out.println("Order submitted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error submitting order: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
