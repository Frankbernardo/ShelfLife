package login;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// SQL Imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Loginpage extends Application {

	private Connection connect() {
	    // Actual database connection details
	    String url = "jdbc:mysql://localhost:3306/InventoryDB"; 
	    String user = "root"; 
	    String password = "Misa70656"; 
	    Connection conn = null;
	    try {
	        conn = DriverManager.getConnection(url, user, password);
	        System.out.println("Connected to the database successfully.");
	    } catch (SQLException e) {
	        System.out.println("Could not connect to the database.");
	        e.printStackTrace();
	    }
	    return conn;
	}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Page");

        // Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Email label and text field
        Label lblUser = new Label("Email:");
        grid.add(lblUser, 0, 1);

        TextField txtUser = new TextField();
        grid.add(txtUser, 1, 1);

        // Password label and password field
        Label lblPassword = new Label("Password:");
        grid.add(lblPassword, 0, 2);

        PasswordField txtPassword = new PasswordField();
        grid.add(txtPassword, 1, 2);

        // Login button
        Button btnLogin = new Button("Login");
        Button btnRegister = new Button("Register");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btnLogin, btnRegister);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btnLogin.setOnAction(e -> {
            String sql = "SELECT password FROM users WHERE email = ?";
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtUser.getText());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getString("password").equals(txtPassword.getText())) {
                        actiontarget.setText("Login Successful!");
                    } else {
                        actiontarget.setText("Incorrect password or ID.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        });

        btnRegister.setOnAction(e -> {
            String sqlInsert = "INSERT INTO users(email, password) VALUES(?,?)";
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setString(1, txtUser.getText());
                pstmt.setString(2, txtPassword.getText());
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    actiontarget.setText("Account Created Successfully!");
                } else {
                    actiontarget.setText("Account creation failed!");
                }
            } catch (SQLException ex) {
                actiontarget.setText("Account already exists!");
                System.out.println(ex.getMessage());
            }
        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
