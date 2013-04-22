package edu.gmu.cs.infs614.webdealer.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.model.connector.CustomerConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

/* Customer model
CREATE TABLE Customer( 
customer_ID INTEGER, 
first_name VARCHAR2(30), 
last_name VARCHAR2(30), 
age INTEGER, 
email_address VARCHAR2(100), 
gender VARCHAR2(1), 
PRIMARY KEY (customer_ID)
);
INSERT INTO Customer VALUES (seq_customer.nextval,'John','Doe','30','jdoe@gmu.edu','m');
*/

// Immutable tuple
public class Customer {

	public boolean isInDatabase = false;
	private SimpleIntegerProperty cID; 
	private SimpleStringProperty cFirstName;
	private SimpleStringProperty cLastName;
	private SimpleIntegerProperty cAge;
	private SimpleStringProperty cEmailAddress;
	private SimpleStringProperty cGender;
	private static Connection conn;

	
	public Customer(Connection conn, Integer customer_ID, String first_name, String last_name,
			Integer age, String email_address, String gender) {
		this.cID = null;
		this.cFirstName = null;
		this.cLastName = null;
		this.cAge = null;
		this.cEmailAddress = null;
		this.cGender = null;
		// create a new customer in the database
		if(customer_ID == null) {
			int result = create(first_name,last_name,
					age,email_address,gender);
			if(result == -1) {
				isInDatabase = false;
				return;
			}
			else {
				isInDatabase = true;
			}
			
			this.cID = new SimpleIntegerProperty(result);
					
			}
		// create just a customer view
		else {
			this.cID = new SimpleIntegerProperty(customer_ID);
		}
		
		this.cFirstName = new SimpleStringProperty(first_name);
		this.cLastName = new SimpleStringProperty(last_name);
		this.cAge = new SimpleIntegerProperty(age);
		this.cEmailAddress = new SimpleStringProperty(email_address);
		this.cGender = new SimpleStringProperty(gender);
		AppUtil.console("Customer constructed");
		
		
	}
	
	// Think about how we can model the key that's generated by Oracle	
	public Customer(
			Connection conn,
			TextField tfCustomer_ID,
			TextField tfFirstName, 
			TextField tfLastName, 
			TextField tfAge, 
			TextField tfEmailAddress, 
			TextField tfGender ) {
		// had to check for null before calling tfCustomer_ID.getText(), so we just copied the constructor code
		this.cID = null;
		this.cFirstName = null;
		this.cLastName = null;
		this.cAge = null;
		this.cEmailAddress = null;
		this.cGender = null;
		
		
		if(tfCustomer_ID == null) {
			int result = create(tfFirstName.getText(),tfLastName.getText(),
					Integer.parseInt(tfAge.getText()),
					tfEmailAddress.getText(),tfGender.getText());
			if(result == -1) {
				isInDatabase = false;
				return;
			}
			else {
				isInDatabase = true;
			}
			this.cID = new SimpleIntegerProperty(result);
			}
		else {
			this.cID = new SimpleIntegerProperty(Integer.parseInt(tfCustomer_ID.getText()));
		}
		
		this.cFirstName = new SimpleStringProperty(tfFirstName.getText());
		this.cLastName = new SimpleStringProperty(tfLastName.getText());
		this.cAge = new SimpleIntegerProperty(Integer.parseInt(tfAge.getText()));
		this.cEmailAddress = new SimpleStringProperty(tfEmailAddress.getText());
		this.cGender = new SimpleStringProperty(tfGender.getText());
		AppUtil.console("Customer constructed");
	}
	

	


	// CRUD
	private int create(
			String cFirstName, String cLastName, 
			Integer cAge, String cEmailAddress, String cGender) {
		
		if(Customer.conn==null) {
			Customer.conn=new CustomerConnection(CustomerConnection.user,CustomerConnection.pass).getConnection();
		}
		
		String sql = "BEGIN INSERT INTO " +
						"Customer (customer_ID,first_name,last_name,age,email_address,gender) " +
						"VALUES (seq_customer.nextval, ?, ?, ?, ?, ?) RETURNING customer_ID INTO ?; END;";
		
		java.sql.CallableStatement stmt = null;
		
		
		int generatedKey = 0;
		
		try {
			stmt = conn.prepareCall(sql);
			stmt.setString(1, cFirstName);
			stmt.setString(2, cLastName);
			stmt.setInt(3, cAge); 
			stmt.setString(4, cEmailAddress);
			stmt.setString(5, cGender);
			
			stmt.registerOutParameter(6, java.sql.Types.INTEGER);	
			stmt.execute();
			generatedKey = stmt.getInt(6);
			stmt.close();
			
		} catch (SQLException sqle) {
			AppUtil.console("Customer insert error: "+sqle);
			return -1;
		}
		
		AppUtil.console("Customer_ID: "+generatedKey);
		return generatedKey;
	}
	
	public static boolean exists(Connection conn, Integer customer_id, String email_address)
	{
		
		if(Customer.conn==null) {
			Customer.conn=new CustomerConnection(CustomerConnection.user,CustomerConnection.pass).getConnection();
		}
		
		String selectSql;
		if(customer_id == null || customer_id < 0) {
			selectSql = "SELECT * FROM CUSTOMER WHERE email_address=\'"+email_address+"\'";
		}
		else {
			selectSql = "SELECT * FROM CUSTOMER WHERE customer_id=\'"+customer_id+"\'";
		}
		
		AppUtil.console("Select String: "+selectSql);
		
		ResultSet rs = null;		
		PreparedStatement preparedStatement = null;
		
		try {

			preparedStatement = conn.prepareStatement(selectSql);
					
		 
			// execute select SQL
			rs = preparedStatement.executeQuery();
		 
			
			
			if(!rs.next()) {
				AppUtil.console(email_address+" not found.");
				return false;
			}
			AppUtil.console(email_address+" found");	
					
			preparedStatement.close();
			
		} catch (SQLException e) {
		 
			AppUtil.console(e.getMessage());
			return false;
		}
		
		return true;
	}
			
	public static ArrayList<Customer> retrieve(
			Connection conn,
			TextField tfCustomer_ID,
			TextField tfFirstName, 
			TextField tfLastName, 
			TextField tfAge, 
			TextField tfEmailAddress, 
			TextField tfGender ) 
	{
		if(Customer.conn==null) {
			Customer.conn=new CustomerConnection(CustomerConnection.user,CustomerConnection.pass).getConnection();
		}
		
		HashMap<String,TextField> map = new HashMap<String,TextField>();
		
		int start = 0;
		String sqlWhere = " WHERE ";
		if(FormValidation.textFieldTypeInteger(tfCustomer_ID)) {
			map.put("customer_ID", tfCustomer_ID);
			String customer_ID = "customer_ID = "+"\'"+tfCustomer_ID.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+customer_ID;
			sqlWhere += customer_ID;
			start++;
		}
		if(FormValidation.textFieldNotEmpty(tfFirstName)) {
			map.put("first_name", tfFirstName);
			String first_name = "first_name = "+"\'"+tfFirstName.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+first_name;
			else sqlWhere += first_name;
			start++;
		}
		if(FormValidation.textFieldNotEmpty(tfLastName)) {
			map.put("last_name", tfLastName);
			String last_name = "last_name = "+"\'"+tfLastName.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+last_name;
			else sqlWhere += last_name;
			start++;
		}
		if(FormValidation.textFieldTypeInteger(tfAge)) {
			map.put("age", tfAge);
			String age = "age = "+"\'"+tfAge.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+age;
			else sqlWhere += age;
			start++;
		}
		if(FormValidation.textFieldNotEmpty(tfEmailAddress)) {
			map.put("age", tfEmailAddress);
			String email_address = "email_address = "+"\'"+tfEmailAddress.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+email_address;
			else sqlWhere += email_address;
			start++;
		}
		if(FormValidation.textFieldNotEmpty(tfGender)) {
			map.put("gender", tfGender);
			String gender = "gender = "+"\'"+tfGender.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+gender;
			else sqlWhere += gender;
			start++;
		}
		
		
		PreparedStatement preparedStatement = null;
 
		String selectSQL;
		if(start>0) {
			selectSQL = "SELECT * FROM CUSTOMER "+sqlWhere;
		}else {
			selectSQL = "SELECT * FROM CUSTOMER";
		}
		
		AppUtil.console("Select String: "+selectSQL);
 
		ResultSet rs = null;
		ArrayList<Customer> cl = new ArrayList<Customer>();
		
		try {

			preparedStatement = conn.prepareStatement(selectSQL);
			
 
			// execute select SQL
			rs = preparedStatement.executeQuery();
 
			while (rs.next()) {
				
				cl.add(new Customer(Customer.conn,
						rs.getInt("customer_ID"),
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getInt("age"),
						rs.getString("email_address"),
						rs.getString("gender")));
 
				AppUtil.console("customer_ID : " + rs.getString("customer_ID"));
				AppUtil.console("first_name : " + rs.getString("first_name"));
				AppUtil.console("last_name : " + rs.getString("last_name"));
				AppUtil.console("age : " + rs.getString("age"));
				AppUtil.console("email_address : " + rs.getString("email_address"));
				AppUtil.console("gender : " + rs.getString("gender"));
				
 
			}
 
			preparedStatement.close();
			//rs.first(); // It seems this non-scrollable result set resets to first automagically
			
			
		} catch (SQLException e) {
 
			AppUtil.console(e.getMessage());
			
		}
		
		
		return cl;
 
	}
	
	// TODO
	public static boolean update(Customer oldCust, Customer newCust) {
		if(Customer.conn==null) {
			Customer.conn=new CustomerConnection(CustomerConnection.user,CustomerConnection.pass).getConnection();
		}
		
		
		AppUtil.console("Modifying customer: "+oldCust.getCID());
		
		String sql = "UPDATE Customer SET "+
				"first_name = \'"+newCust.getCFirstName()+"\',"+
				"last_name = \'"+newCust.getCLastName()+"\',"+
				"age = \'"+newCust.getCAge()+"\',"+
				"email_address = \'"+newCust.getCEmailAddress()+"\',"+
				"gender = \'"+newCust.getCGender()+"\'"+
				" WHERE customer_ID = \'"+newCust.getCID()+"\'";
		
		AppUtil.console("UPDATE: "+sql);

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.executeQuery();
			preparedStatement.close();
		} catch (Exception e) {
			
			AppUtil.console("Most likely a DDL error, not a problem."+e);
			return false;
			
		}
		return true;
	}
	
	// TODO
		public static boolean delete(Customer oldCust) {
			if(Customer.conn==null) {
				Customer.conn=new CustomerConnection(CustomerConnection.user,CustomerConnection.pass).getConnection();
			}
			
			
			AppUtil.console("Deleting customer: "+oldCust.getCID());
			
			String sql = "DELETE FROM Customer "+ 
					"WHERE customer_ID = \'"+oldCust.getCID()+"\'";
			
			AppUtil.console("DELETE: "+sql);

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.executeQuery();
				preparedStatement.close();
			} catch (Exception e) {
				AppUtil.console("Most likely a DDL error, not a problem."+e);
			}
			return true;
		}
	
	
	// getters 
	
	public Integer getCID() {
		return cID.get();
	}
	
	public String getCFirstName() {
		return cFirstName.get();
	}

	public String getCLastName() {
		return cLastName.get();
	}

	public Integer getCAge() {
		return cAge.get();
	}

	public String getCEmailAddress() {
		return cEmailAddress.get();
	}

	public String getCGender() {
		return cGender.get();
	}

	
	
	
}
