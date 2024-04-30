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

    public static void main(String[] args) {
        launch(args);
    }
}
