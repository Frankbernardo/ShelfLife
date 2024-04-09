package orderlist;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import product.ProductPage;

public class revieworderlist extends Application {
	
	private ObservableList<orderlist> orderItems = FXCollections.observableArrayList();
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
        
        TableColumn<orderlist, String> totalAmoutnColumn = new TableColumn<>("Total Amount");
        totalAmoutnColumn.setCellValueFactory(new PropertyValueFactory<>("Toatl Amount"));

        Button btnGoBack = new Button("Go Back");
        btnGoBack.setOnAction(event -> {
            Stage currentStage = (Stage) btnGoBack.getScene().getWindow();
            currentStage.close();
            
            orderlistpage  orderlistScreen = new orderlistpage();
            Stage orderlistStage = new Stage();
            orderlistScreen.start(orderlistStage);
    	
        });
        
        Button btnSubmit = new Button("Submit");
        	btnSubmit.setOnAction(event -> {
                Stage currentStage = (Stage) btnSubmit.getScene().getWindow();
                currentStage.close();

                ProductPage productPage = new ProductPage();
                productPage.start(new Stage());
        	
        });

    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

