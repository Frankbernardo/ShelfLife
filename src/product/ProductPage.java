package product;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import user.Loginpage;

public class ProductPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Left side menu
        VBox leftMenu = new VBox();
        leftMenu.setPadding(new Insets(10));
        leftMenu.setSpacing(8);
        leftMenu.setStyle("-fx-background-color: #E8E8E8;");

        Label shelfLifeLabel = new Label("Shelf life");
        shelfLifeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");

        Button loginButton = new Button("Log In");
        loginButton.setOnAction(e -> {
            // Switch to the login screen
            Loginpage loginScreen = new Loginpage();
            loginScreen.start(primaryStage);
        });
       /* Button loginButton = new Button("Log In");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        // Event handler for Log In button
        loginButton.setOnAction(event -> System.out.println("Log In clicked!"));
        */
        
        // Menu items
        Button reportsButton = new Button("Reports");
        reportsButton.setOnAction(event -> System.out.println("Reports clicked!"));
        Button inventoryButton = new Button("Inventory");
        inventoryButton.setOnAction(event -> System.out.println("Inventory clicked!"));
        Button poListButton = new Button("Purchase Order List");
        poListButton.setOnAction(event -> System.out.println("Purchase Order List clicked!"));
        Button poFormButton = new Button("Purchase Order Form");
        poFormButton.setOnAction(event -> System.out.println("Purchase Order Form clicked!"));

        leftMenu.getChildren().addAll(shelfLifeLabel, loginButton, reportsButton, inventoryButton, poListButton, poFormButton);

        // Product list section on the right
        VBox productListSection = new VBox();
        productListSection.setPadding(new Insets(10));
        productListSection.setSpacing(8);
        productListSection.setStyle("-fx-background-color: #F8F8F8;");

        Label productListLabel = new Label("PRODUCT LIST");
        productListLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");

        Button addButton = new Button("Add Product");
        addButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        // Event handler for Add Product button
        addButton.setOnAction(event -> System.out.println("Add Product clicked!"));

        // Search field and button
        HBox searchBox = new HBox(5);
        TextField searchField = new TextField();
        searchField.setPromptText("Search for items...");
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        // Event handler for Search button
        searchButton.setOnAction(event -> System.out.println("Search clicked with query: " + searchField.getText()));

        searchBox.getChildren().addAll(searchField, searchButton);

        // Table for displaying products
        TableView<Product> productTable = new TableView<>();
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> upcColumn = new TableColumn<>("UPC");
        TableColumn<Product, String> itemNameColumn = new TableColumn<>("Item Name");
        TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        TableColumn<Product, Double> costColumn = new TableColumn<>("Cost");
        TableColumn<Product, Integer> quantityLeftColumn = new TableColumn<>("Quantity Left");

        productTable.getColumns().addAll(upcColumn, itemNameColumn, descriptionColumn, categoryColumn, costColumn, quantityLeftColumn);

        productListSection.getChildren().addAll(productListLabel, addButton, searchBox, productTable);

        // Root layout
        HBox root = new HBox(10);
        root.getChildren().addAll(leftMenu, productListSection);

        // Create scene
        Scene scene = new Scene(root, 960, 540);

        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
