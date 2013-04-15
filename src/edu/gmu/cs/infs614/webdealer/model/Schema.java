package edu.gmu.cs.infs614.webdealer.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.gmu.cs.infs614.webdealer.SchemaInterpreter;

public final class Schema extends OracleConnection {

	private static final String SCHEMA_FILE = "schema.sql";
	private Connection conn;
	
	public Schema(String username, String password) {
		super(username, password);
		System.out.println("Super Called");
		conn = super.getConnection();
		// TODO Auto-generated constructor stub
	}
	
	public void loadSchema() {
		
		try { 
	
			SchemaInterpreter si_schema = new SchemaInterpreter(SCHEMA_FILE);
			Statement statement = null;
			
			for(String line : si_schema) {
				//PreparedStatement pStmt = conn.prepareStatement(line);
				try {
					System.out.println(line);
					//System.err.println("conn NPE?");
					statement = conn.createStatement(); // Create a new statement
					//System.err.println("statement NPE?");
					statement.addBatch(line);
					
					statement.executeBatch();
					//conn.commit();
					statement.close();
				} catch (NullPointerException npe) {
					System.out.println("NPE: "+npe+" ON THIS SQL: " +line);
				}
				
				catch (Exception e) {
					System.err.println("Most likely a DDL error, not a problem."+e);
				}
			}

		} catch (Exception e) {
			SQLError(e);
		} // if any error occurred in the try..catch block, call the SQLError
			// function

	}
}
