package edu.gmu.cs.infs614.webdealer.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.model.connector.CustomerConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.PaymentMethodConnection;

/*
CREATE TABLE Payment_Method(
payment_ID INTEGER, 
cc_number VARCHAR2(18),
cc_vendor VARCHAR2(30),
customer_ID INTEGER,
cc_default INTEGER CHECK (cc_default=0 OR cc_default=1),
PRIMARY KEY (payment_ID,customer_ID),
FOREIGN KEY (customer_ID) REFERENCES Customer (customer_ID) on delete cascade
);
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'1111222233334444','VISA','1','1');
*/

// Immutable tuple
public class PaymentMethod {

	public boolean isInDatabase = false;
	private SimpleIntegerProperty pmPaymentID = null; 
	private SimpleStringProperty pmVendorName = null;
	private SimpleStringProperty pmCardNumber = null;
	private SimpleIntegerProperty pmPrimary = null;
	private SimpleIntegerProperty pmCustomerID = null;
	private static Connection conn = null;

	
	public PaymentMethod(Connection conn, 
			Integer payment_id, 
			String cc_vendor, 
			String cc_number, 
			Integer cc_default,
			Integer customer_id) {
		PaymentMethod.conn = conn;
		// create a new payment in the database
		if( payment_id == null) {
			int result = create(cc_vendor,cc_number,cc_default,customer_id);
			//couldn't be created
			if(result == -1) {
				isInDatabase = false;
				return;
			}
			//created
			else {
				isInDatabase = true;
			}
			//set the oracle index
			this.pmPaymentID = new SimpleIntegerProperty(result);
					
			}
		// already in db, just create for view purposes
		else {
			this.pmPaymentID = new SimpleIntegerProperty(payment_id);
		}
		
		this.pmVendorName = new SimpleStringProperty(cc_vendor);
		this.pmCardNumber = new SimpleStringProperty(cc_number);
		this.pmPrimary = new SimpleIntegerProperty(cc_default);
		this.pmCustomerID = new SimpleIntegerProperty(customer_id);
		
		AppUtil.console("Payment Method constructed");
		
		
	}
	
	public PaymentMethod(Connection conn, 
			TextField payment_id, 
			TextField cc_vendor, 
			TextField cc_number, 
			Integer cc_default,
			Integer customer_id) {
		PaymentMethod.conn = conn;
		// create a new payment in the database
		if( payment_id == null) {
			int result = create(cc_vendor.getText(),cc_number.getText(),
					cc_default,customer_id);
			//couldn't be created
			if(result == -1) {
				isInDatabase = false;
				return;
			}
			//created
			else {
				isInDatabase = true;
			}
			//set the oracle index
			this.pmPaymentID = new SimpleIntegerProperty(result);
					
			}
		// already in db, just create for view purposes
		else {
			this.pmPaymentID = new SimpleIntegerProperty(Integer.parseInt(payment_id.getText()));
		}
		
		this.pmVendorName = new SimpleStringProperty(cc_vendor.getText());
		this.pmCardNumber = new SimpleStringProperty(cc_number.getText());
		this.pmPrimary = new SimpleIntegerProperty(cc_default);
		this.pmCustomerID = new SimpleIntegerProperty(customer_id);
		
		AppUtil.console("Payment Method constructed");
		
		
	}
	

	

	


	// CRUD
	private int create( 
			String cc_vendor,
			String cc_number, 
			Integer cc_default,
			Integer customer_id) {
		
		if(!connect()) AppUtil.console("Not able to connect to database!");
		
		String sql = "BEGIN INSERT INTO " +
						"Payment_Method (payment_ID,cc_vendor,cc_number,cc_default,customer_ID) " +
						"VALUES (seq_payment_method.nextval, ?,?,?,?) RETURNING payment_ID INTO ?; END;";
		
		java.sql.CallableStatement stmt = null;
		
		
		int generatedKey = 0;
		
		try {
			stmt = conn.prepareCall(sql);
			stmt.setString(1, cc_vendor);
			stmt.setString(2, cc_number);
			stmt.setInt(3, cc_default); 
			stmt.setInt(4, customer_id);
			
			stmt.registerOutParameter(5, java.sql.Types.INTEGER);	
			stmt.execute();
			generatedKey = stmt.getInt(5);
			stmt.close();
			
		} catch (SQLException sqle) {
			AppUtil.console("Payment insert error: "+sqle);
			return -1;
		}
		
		AppUtil.console("Payment_ID: "+generatedKey);
		return generatedKey;
	}
	
	// retrieves cards based on customer id
	public static ArrayList<PaymentMethod> retrieve(
			Connection conn,
			Integer customer_id) 
	{
		if(!connect()) AppUtil.console("Not able to connect to database!");
		
		PreparedStatement preparedStatement = null;
 
		String selectSQL = "SELECT * FROM PAYMENT_METHOD WHERE customer_id=\'"+customer_id+"\'";
		
		AppUtil.console("Select String: "+selectSQL);
 
		ResultSet rs = null;
		ArrayList<PaymentMethod> pml = new ArrayList<PaymentMethod>();
		
		try {

			preparedStatement = conn.prepareStatement(selectSQL);
			
 
			// execute select SQL
			rs = preparedStatement.executeQuery();
 
			while (rs.next()) {
				
				pml.add(new PaymentMethod(PaymentMethod.conn,
						rs.getInt("payment_ID"),
						rs.getString("cc_vendor"),
						rs.getString("cc_number"),
						rs.getInt("cc_default"),
						rs.getInt("customer_ID")));
 
				AppUtil.console("payment_ID : " + rs.getString("payment_ID"));
				AppUtil.console("cc_vendor : " + rs.getString("cc_vendor"));
				AppUtil.console("cc_number : " + rs.getString("cc_number"));
				AppUtil.console("cc_default : " + rs.getString("cc_default"));
				AppUtil.console("customer_ID : " + rs.getString("customer_ID"));
				
 
			}
 
			preparedStatement.close();
			//rs.first(); // It seems this non-scrollable result set resets to first automagically
			
			
		} catch (SQLException e) {
 
			AppUtil.console(e.getMessage());
			
		}
		
		
		return pml;
 
	}
	
	public static boolean update(PaymentMethod oldPayment, PaymentMethod newPayment) {
		if(!connect()) AppUtil.console("Not able to connect to database!");
		
		
		AppUtil.console("Modifying payment: "+oldPayment.getPmPaymentID());
		
		String sql = "UPDATE Payment_Method SET "+
				"cc_vendor= \'"+newPayment.getPmVendorName()+"\',"+
				"cc_number= \'"+newPayment.getPmCardNumber()+"\'"+
				" WHERE payment_ID = \'"+newPayment.getPmPaymentID()+"\'";
		
		AppUtil.console("UPDATE: "+sql);

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.executeQuery();
			preparedStatement.close();
		} catch (Exception e) {
			
			AppUtil.console("Update error: "+e);
			return false;
			
		}
		return true;
	}
	
		public static boolean delete(PaymentMethod oldPayment) {
			if(!connect()) AppUtil.console("Not able to connect to database!");
			
			
			AppUtil.console("Deleting payment: "+oldPayment.getPmPaymentID());
			
			String sql = "DELETE FROM Payment_Method "+ 
					"WHERE payment_ID = \'"+oldPayment.getPmPaymentID()+"\'";
			
			AppUtil.console("DELETE: "+sql);

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.executeQuery();
				preparedStatement.close();
			} catch (Exception e) {
				AppUtil.console("Deletion error: "+e);
			}
			return true;
		}
	
	
	// getters 


		public static Integer getPaymentID(Integer customer_id) {
			String getPaymentIDsql = "SELECT payment_ID FROM Payment_Method WHERE customer_ID=\'"+customer_id+"\'";
			Integer id = 0;
			connect();
			ResultSet rs = null;
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = conn.prepareStatement(getPaymentIDsql);
				rs = preparedStatement.executeQuery();
				
				// deal with getting default id
				while(rs.next()) {
					id = rs.getInt("payment_ID");
					if(rs.getInt("cc_default") == 1) {
						return id;
					}
					
				}
				
				preparedStatement.close();
			} catch (Exception e) {
				AppUtil.console("Most likely a DDL error, not a problem."+e);
			}
			return id;
		}
		
	private static boolean connect() {
		if(PaymentMethod.conn==null) {
			PaymentMethod.conn=new PaymentMethodConnection(OracleConnection.user,OracleConnection.pass).getConnection();
			return true;
		}
		return true;
	}

	public Integer getPmPaymentID() {
		return pmPaymentID.get();
	}

	public String getPmVendorName() {
		return pmVendorName.get();
	}

	public String getPmCardNumber() {
		return pmCardNumber.get();
	}

	public Integer getPmPrimary() {
		return pmPrimary.get();
	}

	public Integer getPmCustomerID() {
		return pmCustomerID.get();
	}

}
