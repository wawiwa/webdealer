package edu.gmu.cs.infs614.webdealer.model;

import java.sql.Connection;
import java.sql.Statement;


public final class Schema {

	private static final String SCHEMA_FILE = "schema.sql";
	private Connection conn;
	
	public Schema(Connection conn) {
		this.conn = conn;
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
			System.err.println("Schema error: "+e);
		} // if any error occurred in the try..catch block, call the SQLError
			// function

	}
}
