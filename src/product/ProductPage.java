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
import user.Loginpage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProductPage extends Application {

    private ObservableList<Product> products = FXCollections.observableArrayList();
    private String url = "jdbc:mysql://localhost:3306/InventoryDB";
    private String dbUser = "root";
    private String dbPassword = "Misa70656";

    @Override
    public void start(Stage primaryStage) {
        VBox leftMenu = createLeftMenu(primaryStage);
        // Rest of your code...
    

        loadProductsFromDatabase();

        // Your existing UI setup code...

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

        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> {
            // Close the current stage
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

            // Open the sign-in page in a new stage
            Loginpage loginScreen = new Loginpage();
            Stage loginStage = new Stage();
            loginScreen.start(loginStage);
        });




        leftMenu.getChildren().addAll(shelfLifeLabel, new Button("Reports"), new Button("Inventory"), new Button("Purchase Order List"), new Button("Purchase Order Form"), logoutButton);
        return leftMenu;
    }

    private VBox createProductListSection() {
        VBox productListSection = new VBox(10);
        productListSection.setPadding(new Insets(10));
        productListSection.setStyle("-fx-background-color: #F8F8F8;");

        Label productListLabel = new Label("PRODUCT LIST");
        productListLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");

        Button addButton = new Button("Add Product");
        addButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        addButton.setOnAction(event -> showAddProductDialog());

        productListSection.getChildren().addAll(productListLabel, addButton, setupProductTable());
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

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");
        dialog.setHeaderText("Enter product details");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        TextField SKUField = new TextField();
        SKUField.setPromptText("SKU");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descriptionField, 1, 2);
        grid.add(new Label("SKU:"), 0, 3);
        grid.add(SKUField, 1, 3);
        grid.add(new Label("Quantity:"), 0, 4);
        grid.add(quantityField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    return new Product(
                        0, // Assuming product_id is auto-incremented in the database
                        nameField.getText(),
                        Double.parseDouble(priceField.getText()),
                        0, // Assuming category_id will be set later or is not needed for this dialog
                        descriptionField.getText(),
                        SKUField.getText(),
                        Integer.parseInt(quantityField.getText())
                    );
                } catch (NumberFormatException e) {
                    // Log error or inform user
                    System.out.println("Error: Invalid number format.");
                    return null;
                }
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();

        result.ifPresent(product -> {
            products.add(product);
            addProductToDatabase(product);
        });
    }

    private void addProductToDatabase(Product product) {
        String query = "INSERT INTO products (name, price, description, SKU) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getDescription());
            pstmt.setString(4, product.getSKU());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadProductsFromDatabase() {
        String query = "SELECT product_id, name, price, description, SKU FROM products";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    0, // Assuming category_id is not needed for display
                    rs.getString("description"),
                    rs.getString("SKU"),
                    0  // Assuming quantity is handled separately
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


