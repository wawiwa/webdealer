package edu.gmu.cs.infs614.webdealer.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;


public final class Schema {

	private static final String SCHEMA_FILE = "schema.sql";
	private Connection conn;
	
	public Schema(Connection conn) {
		this.conn = conn;
	}
	
	public void loadVoucherTrigger() {
		
		String line=null;
		SchemaInterpreter si = new SchemaInterpreter();
		try { 
			
			Statement statement = null;
			
			line = si.processLargeStatement("voucherTrigger.sql");
			System.out.println (line);
			statement = conn.createStatement();
			statement.execute(line);
			statement.close();
		} catch (NullPointerException | SQLException e) {
			System.out.println("NPE: "+e+" ON THIS SQL: " +line);
		}

	}
	
	
	public void loadInserts() {

		try { 
	
			SchemaInterpreter si_schema = new SchemaInterpreter("inserts.sql");
			Statement statement = null;
			
			for(String line : si_schema) {
				//PreparedStatement pStmt = conn.prepareStatement(line);
				try {
					System.out.println(line);
					statement = conn.createStatement(); // Create a new statement
					statement.addBatch(line);
					statement.executeBatch();
					statement.close();
				} catch (NullPointerException npe) {
					System.out.println("NPE: "+npe+" ON THIS SQL: " +line);
				}
				
				catch (Exception e) {
					System.out.println("Most likely a DDL error, not a problem."+e);
				}
			}

		} catch (Exception e) {
			System.out.println("Insert error: "+e);
		} // if any error occurred in the try..catch block, call the SQLError
			// function
	}
	public void loadSchema() {
		
		try { 
	
			SchemaInterpreter si_schema = new SchemaInterpreter(SCHEMA_FILE);
			Statement statement = null;
			
			for(String line : si_schema) {
				//PreparedStatement pStmt = conn.prepareStatement(line);
				try {
					System.out.println(line);
					statement = conn.createStatement(); // Create a new statement
					statement.addBatch(line);
					statement.executeBatch();
					if(line.contains("CREATE TABLE Deal")) {
						line = si_schema.processLargeStatement("voucherTrigger.sql");
						statement.close();
						statement = conn.createStatement();
						statement.execute(line);
					}
					statement.close();
				} catch (NullPointerException npe) {
					System.out.println("NPE: "+npe+" ON THIS SQL: " +line);
				}
				
				catch (Exception e) {
					System.out.println("Most likely a DDL error, not a problem."+e);
				}
			}

		} catch (Exception e) {
			System.out.println("Schema error: "+e);
		} // if any error occurred in the try..catch block, call the SQLError
			// function

	}
	
/*
	DROP TABLE Customer CASCADE CONSTRAINTS;
	DROP TABLE Payment_Method CASCADE CONSTRAINTS;
	DROP TABLE Transaction CASCADE CONSTRAINTS;
	DROP TABLE Voucher CASCADE CONSTRAINTS;
	DROP TABLE Review CASCADE CONSTRAINTS;
	DROP TABLE Merchant CASCADE CONSTRAINTS;
	DROP TABLE Deal CASCADE CONSTRAINTS;
	DROP TABLE Location CASCADE CONSTRAINTS;
	DROP TABLE Category CASCADE CONSTRAINTS;	
	DROP TABLE Purchase	 CASCADE CONSTRAINTS;
	DROP TABLE Purchase_Deal CASCADE CONSTRAINTS;
	DROP TABLE Purchase_With CASCADE CONSTRAINTS;
	DROP TABLE Shopping_Cart CASCADE CONSTRAINTS;
	*/
	
	
	public boolean isSchemaLoaded() {
		try {
			  
			HashMap<String,ResultSet> map = new HashMap<String,ResultSet>();
			DatabaseMetaData dbm = conn.getMetaData();
			map.put("CUSTOMER", dbm.getTables(null, null, "CUSTOMER", null));
			map.put("PAYMENT_METHOD", dbm.getTables(null, null, "PAYMENT_METHOD", null));
			map.put("TRANSACTION", dbm.getTables(null, null, "TRANSACTION", null));
			map.put("VOUCHER", dbm.getTables(null, null, "VOUCHER", null));
			map.put("REVIEW", dbm.getTables(null, null, "REVIEW", null));
			map.put("MERCHANT", dbm.getTables(null, null, "MERCHANT", null));
			map.put("DEAL", dbm.getTables(null, null, "DEAL", null));
			map.put("LOCATION", dbm.getTables(null, null, "LOCATION", null));
			map.put("CATEGORY", dbm.getTables(null, null, "CATEGORY", null));
			map.put("PURCHASE", dbm.getTables(null, null, "PURCHASE", null));
			map.put("PURCHASE_DEAL", dbm.getTables(null, null, "PURCHASE_DEAL", null));
			map.put("PURCHASE_WITH", dbm.getTables(null, null, "PURCHASE_WITH", null));
			map.put("SHOPPING_CART", dbm.getTables(null, null, "SHOPPING_CART", null));
			
			if (map.get("CUSTOMER").next() &&
				map.get("PAYMENT_METHOD").next() &&
				map.get("TRANSACTION").next() &&
				map.get("VOUCHER").next() &&
				map.get("REVIEW").next() &&
				map.get("MERCHANT").next() &&
				map.get("DEAL").next() &&
				map.get("LOCATION").next() &&
				map.get("CATEGORY").next() &&
				map.get("PURCHASE").next() &&
				map.get("PURCHASE_DEAL").next() &&
				map.get("PURCHASE_WITH").next() &&
				map.get("SHOPPING_CART").next()) {
			  // All tables exists
				System.out.println("Schema already loaded.");
				return true;
			}
			else {
			  // Tables do not exist
				return false;
			}
		} catch (Exception e) {
			System.out.println("Schema not loaded properly."+e);
			return false;
		}
	}
	
}
