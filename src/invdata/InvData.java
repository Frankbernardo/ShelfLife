package invdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class InvData {
	Connection connection;
	Statement statement;
	ResultSet resultSet;



public InvData() {

	//Load the Driver
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("Driver Loaded");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//Establish a connection
	try {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/InventoryDB", "root", "Misa70656");
		System.out.println("Database Connected");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//Create a statement object
	try {
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}




}
