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
import java.util.HashMap;

public class Loginpage extends Application {
	   
	private HashMap<String, String> accounts = new HashMap<>();

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
	        hbBtn.getChildren().add(btnLogin);
	        hbBtn.getChildren().add(btnRegister);
	        grid.add(hbBtn, 1, 4);

	        final Text actiontarget = new Text();
	        grid.add(actiontarget, 1, 6);

	        btnLogin.setOnAction(e -> {
	            if (accounts.containsKey(txtUser.getText()) && accounts.get(txtUser.getText()).equals(txtPassword.getText())) {
	                actiontarget.setText("Login Successful!");
	            } else {
	                actiontarget.setText("Incorrect password or ID.");
	            }
	        });

	        btnRegister.setOnAction(e -> {
	            if (!accounts.containsKey(txtUser.getText())) {
	                accounts.put(txtUser.getText(), txtPassword.getText());
	                actiontarget.setText("Account Created Successfully!");
	            } else {
	                actiontarget.setText("Account already exists!");
	            }
	        });

	        Scene scene = new Scene(grid, 300, 275);
	        primaryStage.setScene(scene);

	        primaryStage.show();
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
	
}