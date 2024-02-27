package invdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvData {
    // Define the database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/InventoryDB";
    private static final String USER = "root";
    private static final String PASS = "Misa70656";

    Connection connection;

    public InvData() {
        // Load the Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Establish a connection
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Database Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUserCredentials(String email, String password) {
        try {
            String sql = "SELECT password_hash FROM users WHERE email = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String inputHash = HashingUtil.hashPassword(password);

                System.out.println("Stored Hash: " + storedHash);
                System.out.println("Input Hash: " + inputHash);

                if (storedHash.equals(inputHash)) {
                    return true; // Passwords match
                }
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Email not found or passwords don't match
    }
}

