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
	import javafx.util.converter.IntegerStringConverter;
	import product.ProductPage;

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;

	public class revieworderlist extends Application {

	    private ObservableList<orderlist> orderItems = FXCollections.observableArrayList();
	    private TableView<orderlist> tableView = new TableView<>();
	    private String url = "jdbc:mysql://localhost:3306/InventoryDB";
	    private String dbUser = "root";
	    private String dbPassword = "password"; // Changed for security reasons

	    @Override
	    public void start(Stage primaryStage) {
	        loadProductsFromDatabase();
	        tableView.setItems(orderItems);
	        tableView.setEditable(true);

	        TableColumn<orderlist, String> itemColumn = new TableColumn<>("Item");
	        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

	        TableColumn<orderlist, Double> priceColumn = new TableColumn<>("Price");
	        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

	        TableColumn<orderlist, String> inputColumn = new TableColumn<>("Input");
	        inputColumn.setCellValueFactory(new PropertyValueFactory<>("input"));
	        inputColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	        inputColumn.setOnEditCommit(event -> {
	        	orderlist orderItem = event.getRowValue();
	            orderItem.setInput(event.getNewValue());
	            tableView.refresh();
	        });

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
	                    setText(String.format("%.2f", orderItem.getPrice() * Integer.parseInt(orderItem.getInput())));
	                }
	            }
	        });

	        tableView.getColumns().addAll(itemColumn, priceColumn, inputColumn, totalColumn);

	        Button backButton = new Button("Back");
	        backButton.setOnAction(event -> primaryStage.close());

	        Button submitButton = new Button("Submit");
	        submitButton.setOnAction(event -> {
	            // Implement submit action
	        });

	        HBox buttonBar = new HBox(10, backButton, submitButton);
	        buttonBar.setPadding(new Insets(15));
	        buttonBar.setStyle("-fx-alignment: center;");

	        BorderPane root = new BorderPane();
	        root.setCenter(tableView);
	        root.setBottom(buttonBar);

	        Scene scene = new Scene(root, 800, 600);
	        primaryStage.setTitle("Order List Page");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }

	    private void loadProductsFromDatabase() {
	        String query = "SELECT name, price FROM products"; // Simplified query
	        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
	             PreparedStatement pstmt = conn.prepareStatement(query)) {
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                orderItems.add(new orderlist(query, query, 0, 0, query));
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
	}
