package edu.gmu.cs.infs614.webdealer;

/*******************************************************************
 This program accesses relations created by the program "demobld".
 It connects to ORACLE, fetches the names and salaries of all employees, 
 and displays the results.
 *******************************************************************/

import java.sql.*; //Import the java SQL library

class Webdealer // Create a new class to encapsulate the program
{

	public static void SQLError(Exception e) // Our function for handling SQL
												// errors
	{
		System.out.println("ORACLE error detected:");
		e.printStackTrace();
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
					conn = DriverManager.getConnection(url, "mjoy","password "+e1);
				}
				catch (SQLException e2) {
					System.err.println("Neither login (wward5 or mjoy was successful. "+e2);
				}
			}

			System.out.println("Connected!");

			Statement stmt = conn.createStatement(); // Create a new statement

			// Now we execute our query and store the results in the myresults
			// object:
			ResultSet myresults = stmt
					.executeQuery("SELECT ename, sal FROM emp");

			System.out.println("Employee_Name\tSalary");
			System.out.println("-------------\t------"); // Print a header

			while (myresults.next()) // pass to the next row and loop until the
										// last
			{
				System.out.println(myresults.getString("ename") + "\t\t"
						+ myresults.getString("sal")); // Print the current row
			}

			conn.close(); // Close our connection.

		} catch (Exception e) {
			SQLError(e);
		} // if any error occurred in the try..catch block, call the SQLError
			// function

	}
}
