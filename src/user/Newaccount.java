package user;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Newaccount extends Application {

    private final String url = "jdbc:mysql://localhost:3306/InventoryDB";
    private final String dbUser = "root";
    private final String dbPassword = "Misa70656";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Account");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Create Account");
        scenetitle.setFont(Font.font("Arial", 30));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userNameLabel = new Label("First Name:");
        grid.add(userNameLabel, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 2);

        TextField lastNameTextField = new TextField();
        grid.add(lastNameTextField, 1, 2);

        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 3);

        TextField emailTextField = new TextField();
        grid.add(emailTextField, 1, 3);

        Label pwLabel = new Label("Password:");
        grid.add(pwLabel, 0, 4);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 4);

        ToggleGroup group = new ToggleGroup();
        RadioButton rbAdmin = new RadioButton("Admin user");
        rbAdmin.setToggleGroup(group);
        rbAdmin.setSelected(true);

        HBox userTypeBox = new HBox(20);
        userTypeBox.getChildren().add(rbAdmin);
        grid.add(userTypeBox, 1, 5);

        Button btn = new Button("Sign up");
        btn.setOnAction(e -> {
            insertUser(userTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), pwBox.getText(), rbAdmin.isSelected() ? "Admin" : "Regular");

            // After insertion logic
            Stage currentStage = (Stage) btn.getScene().getWindow();
            currentStage.close();

            Loginpage loginScreen = new Loginpage();
            Stage loginStage = new Stage();
            loginScreen.start(loginStage);
        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        // Add a Back button
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            Stage currentStage = (Stage) backBtn.getScene().getWindow();
            currentStage.close();

            Loginpage loginScreen = new Loginpage();
            Stage loginStage = new Stage();
            loginScreen.start(loginStage);
        });
        HBox hbBackBtn = new HBox(10);
        hbBackBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBackBtn.getChildren().add(backBtn);
        grid.add(hbBackBtn, 0, 6);

        Scene scene = new Scene(grid, 800, 475);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (SQLException e) {
            System.out.println("Could not connect to the database.");
            e.printStackTrace();
            return null;
        }
    }

    private void insertUser(String firstName, String lastName, String email, String password, String userType) {
        String query = "INSERT INTO users (username, password, full_name, email, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName + " " + lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, userType);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("User registration failed.");
            }
        } catch (SQLException ex) {
            System.out.println("Error during database operation.");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}