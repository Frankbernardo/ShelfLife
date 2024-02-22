package user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Newaccount extends JFrame implements ActionListener {

    private Container container;
    private JLabel titleLabel;
    private JLabel firstNameLabel;
    private JTextField firstNameTextField;
    private JLabel lastNameLabel;
    private JTextField lastNameTextField;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JCheckBox adminCheckBox;
    private JCheckBox regularCheckBox;
    private JButton submitButton;
    private JButton signInButton; // Added Sign In button

    private String url = "jdbc:mysql://localhost:3306/InventoryDB"; 
    private String dbUser = "root"; 
    private String dbPassword = "Misa70656";

    public Newaccount() {
        setTitle("Create Account");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);

        titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        titleLabel.setSize(300, 30);
        titleLabel.setLocation(300, 30);
        container.add(titleLabel);

        firstNameLabel = new JLabel("First Name");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        firstNameLabel.setSize(100, 20);
        firstNameLabel.setLocation(100, 100);
        container.add(firstNameLabel);

        firstNameTextField = new JTextField();
        firstNameTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        firstNameTextField.setSize(190, 20);
        firstNameTextField.setLocation(200, 100);
        container.add(firstNameTextField);

        lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        lastNameLabel.setSize(100, 20);
        lastNameLabel.setLocation(400, 100);
        container.add(lastNameLabel);

        lastNameTextField = new JTextField();
        lastNameTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        lastNameTextField.setSize(190, 20);
        lastNameTextField.setLocation(500, 100);
        container.add(lastNameTextField);

        emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        emailLabel.setSize(100, 20);
        emailLabel.setLocation(100, 150);
        container.add(emailLabel);

        emailTextField = new JTextField();
        emailTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        emailTextField.setSize(190, 20);
        emailTextField.setLocation(200, 150);
        container.add(emailTextField);

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordLabel.setSize(100, 20);
        passwordLabel.setLocation(100, 200);
        container.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setSize(190, 20);
        passwordField.setLocation(200, 200);
        container.add(passwordField);

        adminCheckBox = new JCheckBox("Admin user");
        adminCheckBox.setFont(new Font("Arial", Font.PLAIN, 15));
        adminCheckBox.setSize(150, 20);
        adminCheckBox.setLocation(200, 250);
        container.add(adminCheckBox);

        regularCheckBox = new JCheckBox("Regular user");
        regularCheckBox.setFont(new Font("Arial", Font.PLAIN, 15));
        regularCheckBox.setSize(150, 20);
        regularCheckBox.setLocation(200, 275);
        container.add(regularCheckBox);

        // Ensure that admin and regular are mutually exclusive
        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(adminCheckBox);
        userTypeGroup.add(regularCheckBox);

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 15));
        submitButton.setSize(100, 20);
        submitButton.setLocation(150, 350);
        submitButton.addActionListener(this);
        container.add(submitButton);

        signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("Arial", Font.PLAIN, 15));
        signInButton.setSize(100, 20);
        signInButton.setLocation(260, 350);
        signInButton.addActionListener(this);
        container.add(signInButton);

        setVisible(true);
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, dbUser, dbPassword);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            System.out.println("Could not connect to the database.");
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String pass = new String(passwordField.getPassword());
            String userType = adminCheckBox.isSelected() ? "Admin" : "Regular";

            // Insert user data into database
            insertUser(firstName, lastName, email, pass, userType);
        } else if (e.getSource() == signInButton) {
            // Handle Sign In action
            // You might want to display a login form or dialog here
            JOptionPane.showMessageDialog(this, "Sign In button clicked");
        }
    }

    private void insertUser(String firstName, String lastName, String email, String password, String userType) {
        String query = "INSERT INTO users (first_name, last_name, email, password, user_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password); // Hashing should be done here, left as an exercise
            pstmt.setString(5, userType);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "User registered successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "User registration failed.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error during database operation.");
            ex.printStackTrace();
        }
    }
/*
    public static void main(String[] args) {
        // Set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new Newaccount();
    }
    */
}

