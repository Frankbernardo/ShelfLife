package user;

import javafx.application.Application;
//import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// SQL Imports
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;

public class Loginpage extends Application {

    private Connection connect() {
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
        primaryStage.setTitle("Welcome to Shelf Life");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Sign in to your account");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label lblUser = new Label("Email:");
        grid.add(lblUser, 0, 1);

        TextField txtUser = new TextField();
        grid.add(txtUser, 1, 1);

        Label lblPassword = new Label("Password:");
        grid.add(lblPassword, 0, 2);

        PasswordField txtPassword = new PasswordField();
        grid.add(txtPassword, 1, 2);

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot password");
        grid.add(forgotPasswordLink, 1, 3);

        Button btnLogin = new Button("Log in");
        HBox hbBtnLogin = new HBox(10);
        hbBtnLogin.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnLogin.getChildren().add(btnLogin);
        grid.add(hbBtnLogin, 0, 4);

        Button btnCreateAccount = new Button("New user");
        HBox hbBtnCreateAccount = new HBox(10);
        hbBtnCreateAccount.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnCreateAccount.getChildren().add(btnCreateAccount);
        grid.add(hbBtnCreateAccount, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btnLogin.setOnAction(e -> {
            // Login logic goes here
        });

        btnCreateAccount.setOnAction(e -> {
            // Open the account creation interface
        });

        forgotPasswordLink.setOnAction(e -> {
            // Forgot password logic goes here
        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
}
