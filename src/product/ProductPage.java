package product;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ProductPage extends Application {

    private ObservableList<Product> products = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        VBox leftMenu = new VBox();
        leftMenu.setPadding(new Insets(10));
        leftMenu.setSpacing(8);
        leftMenu.setStyle("-fx-background-color: #E8E8E8;");

        Label shelfLifeLabel = new Label("Shelf life");
        shelfLifeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");

        // Example menu items
        Button reportsButton = new Button("Reports");
        Button inventoryButton = new Button("Inventory");
        Button poListButton = new Button("Purchase Order List");
        Button poFormButton = new Button("Purchase Order Form");
        Button loginButton = new Button("Log In");

        leftMenu.getChildren().addAll(shelfLifeLabel, reportsButton, inventoryButton, poListButton, poFormButton, loginButton);

        VBox productListSection = new VBox();
        productListSection.setPadding(new Insets(10));
        productListSection.setSpacing(8);
        productListSection.setStyle("-fx-background-color: #F8F8F8;");

        Label productListLabel = new Label("PRODUCT LIST");
        productListLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");

        Button addButton = new Button("Add Product");
        addButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        addButton.setOnAction(event -> showAddProductDialog());

        productListSection.getChildren().addAll(productListLabel, addButton, setupProductTable());

        HBox root = new HBox(10);
        root.getChildren().addAll(leftMenu, productListSection);

        Scene scene = new Scene(root, 960, 540);

        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Product> setupProductTable() {
        TableView<Product> table = new TableView<>();
        table.setItems(products);

        TableColumn<Product, String> upcColumn = new TableColumn<>("UPC");
        upcColumn.setCellValueFactory(new PropertyValueFactory<>("upc"));

        TableColumn<Product, String> nameColumn = new TableColumn<>("Item Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Product, Double> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity Left");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityLeft"));

        table.getColumns().addAll(upcColumn, nameColumn, descriptionColumn, categoryColumn, costColumn, quantityColumn);

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

        TextField upcField = new TextField();
        upcField.setPromptText("UPC");
        TextField nameField = new TextField();
        nameField.setPromptText("Item Name");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        TextField costField = new TextField();
        costField.setPromptText("Cost");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity Left");

        grid.add(new Label("UPC:"), 0, 0);
        grid.add(upcField, 1, 0);
        grid.add(new Label("Item Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descriptionField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("Cost:"), 0, 4);
        grid.add(costField, 1, 4);
        grid.add(new Label("Quantity Left:"), 0, 5);
        grid.add(quantityField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    return new Product(
                        upcField.getText(),
                        nameField.getText(),
                        descriptionField.getText(),
                        categoryField.getText(),
                        Double.parseDouble(costField.getText()),
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

        result.ifPresent(product -> products.add(product));
    }

    public static void main(String[] args) {
        launch(args);
    }
}