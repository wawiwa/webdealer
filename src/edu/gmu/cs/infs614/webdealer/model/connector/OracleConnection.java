package edu.gmu.cs.infs614.webdealer.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleDatabaseMetaData;

//import oracle.jdbc.driver.OracleDatabaseMetaData;

import edu.gmu.cs.infs614.webdealer.SchemaInterpreter;

// This class creates a semaphore connection to the database
//	Every model needs to use this connection.
//	This cannot be instantiated. It must be extended by a model connector.
// 		Example: Connection custConn = CustomerConnection(wward,password);
//	Instantiate once, and use that connector for subsequent model classes.
//		Example: Customer(custConn, First, Last, Age, EmailAddress, Gender);
public abstract class OracleConnection {

	protected final static String url = "jdbc:oracle:thin:@apollo.ite.gmu.edu:1521:ite10g";
	protected Connection connection;
	

	private static final String VIEWS_FILE = "views.sql";
	private static final String VIEW_TABLES = "view_tables.sql";
	
	public static void SQLError(Exception e) { // Our function for handling SQL// errors
		System.out.println("ORACLE error detected:");
		e.printStackTrace();
	}
	
	// Connection will be created per view
	OracleConnection(String username, String password)  {
		
		try {
			String driverName = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driverName);
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			
			connection = DriverManager.getConnection(url, username, password);
			connection.setAutoCommit(true);
			System.out.println("Oracle connection created.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
}
