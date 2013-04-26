package edu.gmu.cs.infs614.webdealer.model.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.gmu.cs.infs614.webdealer.AppUtil;

//import oracle.jdbc.driver.OracleDatabaseMetaData;


// The goal was to create a singleton, but subclassing an abstract singleton is a bit complex..
//	Every model needs to use this connection.
//	This cannot be instantiated. It must be extended by a model connector.
// 		Example: Connection custConn = CustomerConnection(wward,password);
//	Instantiate once, and use that connector for subsequent model classes.
//		Example: Customer(custConn, First, Last, Age, EmailAddress, Gender);
public abstract class OracleConnection {

	public static String user="wward5";
	public static String pass="sqlplus";
	protected final static String url = "jdbc:oracle:thin:@apollo.ite.gmu.edu:1521:ite10g";
	protected Connection connection;
	public boolean isConnected = false;
	

	//private static final String VIEWS_FILE = "views.sql";
	//private static final String VIEW_TABLES = "view_tables.sql";
	
	public static void SQLError(Exception e) { // Our function for handling SQL// errors
		AppUtil.console("ORACLE error detected:");
		AppUtil.console(e.toString());
	}
	
	// Connection will be created per view
	OracleConnection(String username, String password) {
		
		try {
			String driverName = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driverName);
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			AppUtil.console(e1.toString());
		}
		try {
			connection = DriverManager.getConnection(url, username, password);
			connection.setAutoCommit(true);
			isConnected = true;
			AppUtil.console("Oracle connection created.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			AppUtil.console(e.toString());
			isConnected = false;
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
}
