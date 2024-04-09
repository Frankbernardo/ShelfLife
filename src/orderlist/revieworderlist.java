package orderlist;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import product.ProductPage;

public class revieworderlist extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Riview Page");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        TableColumn<orderlist, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<orderlist, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<orderlist, String> inputColumn = new TableColumn<>("Input");
        inputColumn.setCellValueFactory(new PropertyValueFactory<>("input"));
        
        Label totalLabel = new Label("Total:");
        grid.add(totalLabel, 0, 2);
        
        TextField totalTextField = new TextField();
        totalTextField.setEditable(false);
        grid.add(totalTextField, 1, 2);

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

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btnGoBack);
        hbBtn.getChildren().add(btnSubmit);
        grid.add(hbBtn, 1, 4);

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

