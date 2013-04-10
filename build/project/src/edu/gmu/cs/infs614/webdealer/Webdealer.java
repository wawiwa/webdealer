package edu.gmu.cs.infs614.webdealer;

/*******************************************************************
 This program accesses relations created by the program "demobld".
 It connects to ORACLE, fetches the names and salaries of all employees, 
 and displays the results.
 *******************************************************************/

import java.sql.*; //Import the java SQL library


class Webdealer // Create a new class to encapsulate the program
{

	private static final String SCHEMA_FILE = "schema.sql";
	private static final String VIEWS_FILE = "views.sql";
	private static final String VIEW_TABLES = "view_tables.sql";
	
	public static void SQLError(Exception e) // Our function for handling SQL
												// errors
	{
		System.out.println("ORACLE error detected:");
		e.printStackTrace();
	}

	private static void dispResultSet (ResultSet rs)
			throws SQLException
		{
			int i;

			// Get the ResultSetMetaData.  This will be used for
			// the column headings

			ResultSetMetaData rsmd = rs.getMetaData ();

			// Get the number of columns in the result set

			int numCols = rsmd.getColumnCount ();

			// Display column headings

			for (i=1; i<=numCols; i++) {
				if (i > 1) System.out.print(",");
				System.out.print(rsmd.getColumnLabel(i));
			}
			System.out.println("");
			
			// Display data, fetching until end of the result set

			boolean more = rs.next ();
			while (more) {

				// Loop through each column, getting the
				// column data and displaying

				for (i=1; i<=numCols; i++) {
					if (i > 1) System.out.print(",");
					System.out.print(rs.getString(i));
				}
				System.out.println("");

				// Fetch the next result set row

				more = rs.next ();
			}
		}
	
	public static void main(String args[]) // The main function

	{
		
		try { // Keep an eye open for errors

			String driverName = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driverName);

			System.out.println("Connecting   to Oracle...");

			String url = "jdbc:oracle:thin:@apollo.ite.gmu.edu:1521:ite10g";
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(url, "wward5","password");
			}
			catch (SQLException e1) {
				try {
					conn = DriverManager.getConnection(url, "mjoy","password"+e1);
				}
				catch (SQLException e2) {
					System.err.println("Neither login (wward5 or mjoy was successful. "+e2);
				}
			}
			// needed this 
			conn.setAutoCommit(true);
			
			System.out.println("Connected!");

			
			
			Statement statement = null;
	
			SchemaInterpreter si_schema = new SchemaInterpreter(SCHEMA_FILE);
			for(String line : si_schema) {
				//PreparedStatement pStmt = conn.prepareStatement(line);
				try {
					statement = conn.createStatement(); // Create a new statement
					statement.addBatch(line);
					System.out.println(line);
					statement.executeBatch();
					statement.close();
				} catch (Exception e) {
					System.err.println("Most likely a DDL error, not a problem."+e);
				}
			}
			
			Statement views = null;
			
			SchemaInterpreter si_views = new SchemaInterpreter(VIEWS_FILE);
			for(String line : si_views) {
				//PreparedStatement pStmt = conn.prepareStatement(line);
				try {
					views = conn.createStatement(); // Create a new statement
					views.addBatch(line);
					System.out.println(line);
					views.executeBatch();
					views.close();
				} catch (Exception e) {
					System.err.println("Most likely a DDL error, not a problem."+e);
				}
			}
			
			Statement view_tables = null;
			SchemaInterpreter si_view_tables = new SchemaInterpreter(VIEW_TABLES);
			
			for(String line : si_view_tables) {
				//PreparedStatement pStmt = conn.prepareStatement(line);
				try {
					view_tables = conn.createStatement(); // Create a new statement
					System.out.println(line);
					ResultSet rs = view_tables.executeQuery(line);
					dispResultSet(rs);
					view_tables.close();
				} catch (Exception e) {
					System.err.println("View error!"+e);
				}
			}
			
			
			
			conn.commit();
			conn.close();
			

		} catch (Exception e) {
			SQLError(e);
		} // if any error occurred in the try..catch block, call the SQLError
			// function

	}
}
