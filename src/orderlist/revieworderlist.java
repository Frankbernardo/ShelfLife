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

    TableColumn<orderlist, String> totalColumn = new TableColumn<>("Total");
    totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
    totalColumn.setCellFactory(column -> new TableCell<orderlist, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                orderlist orderItem = getTableView().getItems().get(getIndex());
                try {
                    double total = orderItem.getPrice() * Double.parseDouble(orderItem.getInput());
                    setText(String.format("%.2f", total));
                    System.out.println("Review Total for " + orderItem.getItem() + ": " + total);  // Debugging total calculation
                } catch (NumberFormatException e) {
                    setText("Error");
                    System.err.println("Error calculating total: " + e.getMessage());
                }
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

}
