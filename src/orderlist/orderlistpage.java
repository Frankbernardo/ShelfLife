package orderlist;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class orderlistpage extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Creating the table view.
        TableView<String[]> tableView = new TableView<>();

        // Define the columns.
        String[] columnNames = {"Item", "SKU", "Price", "Inventory", "Input"};
        for (String columnName : columnNames) {
            TableColumn<String[], String> column = new TableColumn<>(columnName);
            // Normally, you'd set up a cell value factory here to populate the cells.
            tableView.getColumns().add(column);
        }

        // Creating the buttons.
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            // Handle back button action.
        });

        Button reviewButton = new Button("Review");
        reviewButton.setOnAction(event -> {
            // Handle review button action.
        });

        // Creating the button bar.
        HBox buttonBar = new HBox(10, backButton, reviewButton);
        buttonBar.setStyle("-fx-alignment: center; -fx-padding: 10px;");

        // Assembling the layout.
        BorderPane root = new BorderPane();
        root.setCenter(tableView);
        root.setBottom(buttonBar);

        // Setting the stage.
        Scene scene = new Scene(root, 800, 600); // Adjust the size as necessary
        primaryStage.setTitle("Order List Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
