package edu.gmu.cs.infs614.webdealer.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// This class creates a semaphore connection to the database
//	Every model needs to use this connection.
//	This cannot be instantiated. It must be extended by a model connector.
// 		Example: Connection custConn = CustomerConnection(wward,password);
//	Instantiate once, and use that connector for subsequent model classes.
//		Example: Customer(custConn, First, Last, Age, EmailAddress, Gender);
public abstract class OracleConnection {

	protected final static String url = "jdbc:oracle:thin:@apollo.ite.gmu.edu:1521:ite10g";
	protected static Connection connection = null;
	
	// Connection will be created per view
	OracleConnection(String username, String password)  {
		try {
			connection = DriverManager.getConnection(url, username, password);
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
