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
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
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
        inputColumn.setCellFactory(column -> new TableCell<orderlist, String>() {
            private final TextField textField = new TextField();

            {
                textField.setOnAction(evt -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitEdit(textField.getText());
                    }
                });
                setGraphic(textField);

                textField.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1 && !isEmpty()) {
                        startEdit();
                    }
                });
            }

            @Override
            public void startEdit() {
                super.startEdit();
                textField.setText(getItem());
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                textField.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        textField.setText(item);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    } else {
                        setText(item);
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }
                }
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
        reviewButton.setOnAction(event -> {
            double total = calculateTotal();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Review");
            alert.setHeaderText("Total Price");
            alert.setContentText("The total price of your order is: $" + String.format("%.2f", total));
            alert.showAndWait();
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

    private double calculateTotal() {
        double total = 0;
        for (orderlist item : orderItems) {
            try {
                int quantity = Integer.parseInt(item.getInput());
                total += quantity * item.getPrice();
            } catch (NumberFormatException e) {
                // Handle invalid input
            }
        }
        return total;
    }

            public static void main(String[] args) {
                launch(args);
            }
        }


