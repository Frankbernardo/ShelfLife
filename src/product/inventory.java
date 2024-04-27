package product;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import orderlist.orderlistpage;
import user.Loginpage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class inventory extends Application {

	private ObservableList<Product> products = FXCollections.observableArrayList();
    private String url = "jdbc:mysql://localhost:3306/InventoryDB";
    private String dbUser = "root";
    private String dbPassword = "Misa70656";

    @Override
    public void start(Stage primaryStage) {
        VBox leftMenu = createLeftMenu(primaryStage);
     
        loadProductsFromDatabase();
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(createMainLayout(), 960, 540));
        primaryStage.show();
    }

    private VBox createLeftMenu(Stage primaryStage) {
    	VBox leftMenu = new VBox(10);
        leftMenu.setPadding(new Insets(10));
        leftMenu.setStyle("-fx-background-color: #E8E8E8;");

        Label shelfLifeLabel = new Label("Shelf life");
        shelfLifeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");

        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> {
            
            Stage currentStage = (Stage) homeButton.getScene().getWindow();
            currentStage.close();
            
           
            ProductPage productPage = new ProductPage();
            productPage.start(new Stage());
        });

        Button purchaseOrderListButton = new Button("Purchase Order List");
        Button PurchaseOrderFormButton = new Button("Purchase Order Form");
        PurchaseOrderFormButton.setOnAction(e -> {
           
            Stage currentStage = (Stage) PurchaseOrderFormButton.getScene().getWindow();
            currentStage.close();
            
           
            orderlistpage  orderlistScreen = new orderlistpage();
            Stage orderlistStage = new Stage();
            orderlistScreen.start(orderlistStage);
        });
        
        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> {
            
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

           
            Loginpage loginScreen = new Loginpage();
            Stage loginStage = new Stage();
            loginScreen.start(loginStage);
        });

        leftMenu.getChildren().addAll(shelfLifeLabel, homeButton, purchaseOrderListButton, PurchaseOrderFormButton, logoutButton);
        return leftMenu;
    }

    private VBox createProductListSection() {
        VBox productListSection = new VBox(10);
        productListSection.setPadding(new Insets(10));
        productListSection.setStyle("-fx-background-color: #F8F8F8;");

        Label productListLabel = new Label("INVENTORY LIST");
        productListLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search Products");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> searchProduct(searchField.getText()));

        productListSection.getChildren().addAll(productListLabel, searchField, searchButton, setupProductTable());
        return productListSection;
    }

    private HBox createMainLayout() {
        HBox root = new HBox(10);
        root.getChildren().addAll(createLeftMenu(null), createProductListSection());
        return root;
    }

    private TableView<Product> setupProductTable() {
        TableView<Product> table = new TableView<>();
        table.setItems(products);

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Product, String> SKUColumn = new TableColumn<>("SKU");
        SKUColumn.setCellValueFactory(new PropertyValueFactory<>("SKU"));
        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table.getColumns().addAll(nameColumn, priceColumn, descriptionColumn, SKUColumn, quantityColumn);
        return table;
    }

    private void searchProduct(String searchText) {
        products.clear();
        String query = "SELECT p.product_id, p.name, p.price, p.description, p.SKU, i.quantity " +
                       "FROM products p " +
                       "JOIN inventory i ON p.product_id = i.product_id " +
                       "WHERE p.name LIKE ?";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    0,
                    rs.getString("description"),
                    rs.getString("SKU"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadProductsFromDatabase() {
        String query = "SELECT p.product_id, p.name, p.price, p.description, p.SKU, i.quantity " +
                       "FROM products p " +
                       "JOIN inventory i ON p.product_id = i.product_id";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    0,
                    rs.getString("description"),
                    rs.getString("SKU"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
