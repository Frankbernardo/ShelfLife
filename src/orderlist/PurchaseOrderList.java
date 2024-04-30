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

import java.time.LocalDate;

public class PurchaseOrderList extends Application {
    private ObservableList<PurchaseOrder> orders = FXCollections.observableArrayList();

    private TableView<PurchaseOrder> tableView = new TableView<>();
    private String url = "jdbc:mysql://localhost:3306/InventoryDB";
    private String dbUser = "root";
    private String dbPassword = "Misa70656";

    @Override
    public void start(Stage primaryStage) {
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

    private static class ActionCell extends TableCell<PurchaseOrder, Void> {
        private final Button actionButton = new Button("View");

        public ActionCell() {
            actionButton.setOnAction(event -> {
                PurchaseOrder order = getTableView().getItems().get(getIndex());
                // You could open a new window or a dialog with the order details here
                System.out.println("Viewing order: " + order);
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
