package edu.gmu.cs.infs614.webdealer.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.PaymentMethodConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

/*
 * 
 * 
 * 

CREATE TABLE Deal(
deal_ID INTEGER,
expiration_date DATE,
description VARCHAR2(500),
quantity_limit INTEGER,
original_price REAL,
deal_price REAL,
sale_start_time DATE,
sale_end_time DATE,
location_ID INTEGER,
category_ID INTEGER,
merchant_ID INTEGER,
PRIMARY KEY (deal_ID),
FOREIGN KEY (location_ID) REFERENCES Location (location_ID),
FOREIGN KEY (category_ID) REFERENCES Category (category_ID),
FOREIGN KEY (merchant_ID) REFERENCES Merchant (merchant_ID)
);


CREATE TABLE Voucher(
voucher_ID INTEGER, 
status VARCHAR2(30),
deal_ID INTEGER,
PRIMARY KEY (voucher_ID),
FOREIGN KEY (deal_ID) REFERENCES Deal (deal_ID) on delete cascade
);

CREATE TABLE Transaction(
transaction_ID INTEGER, 
trans_date DATE,
voucher_ID INTEGER,
payment_ID INTEGER,
customer_ID INTEGER,
PRIMARY KEY (transaction_ID),
FOREIGN KEY (voucher_ID) REFERENCES Voucher (voucher_ID),
FOREIGN KEY (payment_ID,customer_ID) REFERENCES Payment_Method (payment_ID,customer_ID) on delete cascade
);

CREATE TABLE Payment_Method(
payment_ID INTEGER, 
cc_number VARCHAR2(18),
cc_vendor VARCHAR2(30),
customer_ID INTEGER,
cc_default INTEGER CHECK (cc_default=0 OR cc_default=1),
PRIMARY KEY (payment_ID,customer_ID),
FOREIGN KEY (customer_ID) REFERENCES Customer (customer_ID) on delete cascade
);
*
*
*/

// Immutable tuple
public class Purchase {

	public boolean isInDatabase = false;
	
	private SimpleIntegerProperty pVoucher_ID = null;
	private SimpleStringProperty pStatus = null;
	private SimpleIntegerProperty pDeal_ID = null;
	private SimpleIntegerProperty pTransaction_ID = null;
	private SimpleStringProperty pTrans_date = null;
	private SimpleIntegerProperty pPayment_ID = null;
	private SimpleIntegerProperty pCustomer_ID = null;
	private SimpleStringProperty pEmailAddress = null;
	private SimpleIntegerProperty pQuantity = null;
	
	private ArrayList<SimpleIntegerProperty> vl = new ArrayList<SimpleIntegerProperty>();

	private static Connection conn = null;
	
	public Purchase(Connection conn, 
			TextField transaction_id,
			TextField email_address, 
			TextField deal_id,
			TextField quantity,
			TextField voucher_id) {
		Purchase.conn = conn;
		
		// if voucher id not included, set to 0
		if(voucher_id == null || voucher_id.getText() == null) {
			voucher_id.setText("0");
		}else {
			this.pVoucher_ID = new SimpleIntegerProperty(Integer.parseInt(voucher_id.getText()));
		}
		
		// create a new purchase in the database
		if( transaction_id == null) {
			int result = create(email_address.getText(),
					Integer.parseInt(deal_id.getText()),
					Integer.parseInt(quantity.getText()));
			//couldn't be created
			if(result == 0) {
				isInDatabase = false;
				return;
			}
			//created
			else {
				isInDatabase = true;
			}
					
		}
		// already in db, just create for view purposes
		else {
			setProperties(email_address.getText(),
					Integer.parseInt(deal_id.getText()),
					Integer.parseInt(quantity.getText()));
			isInDatabase = true;
		}
		
		AppUtil.console("Payment Method constructed");
		
		
	}
	
	public ArrayList<Purchase> getPurchaseViews() {

		ArrayList<Purchase> pl = new ArrayList<Purchase>();
		
			TextField vid = new TextField();
			TextField sts = new TextField();
			TextField did = new TextField();
			TextField tid = new TextField();
			TextField tdt = new TextField();
			TextField pid = new TextField();
			TextField cid = new TextField();
			TextField eml = new TextField();

		for(SimpleIntegerProperty voucher : vl) {
			vid.setText( ((Integer) voucher.get()).toString());
			tid.setText( ((Integer) getPTransaction_ID()).toString());
			pl.addAll(Purchase.retrieve(conn,vid,sts,did,tid,tdt,pid,cid,eml));
		}
		return pl;
	}
	
	private boolean isValidDeal(Integer deal_id) {
		
		String selectSQL = "SELECT * FROM Deal WHERE deal_ID=\'"+deal_id+"\' AND sale_end_time>=\'"+this.getPTrans_date()+"\'";
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		boolean is = false;
		try {

			preparedStatement = conn.prepareStatement(selectSQL);
			
 
			// execute select SQL
			rs = preparedStatement.executeQuery();
 
			if(rs.next()) {
				is = true;
			}
 
			preparedStatement.close();
			//rs.first(); // It seems this non-scrollable result set resets to first automagically
			
			
		} catch (SQLException e) {
 
			AppUtil.console(e.getMessage());
			
		}

		return is;
		
	}
	
	private boolean setProperties(String email_address, Integer deal_id, 
			Integer quantity) {
				
		for(int i=1;i<=quantity;i++) {
			vl.add(new SimpleIntegerProperty(Purchase.getUnsoldVoucherID(deal_id)));
		}
		if(vl.size() < quantity) return false;
		
		this.pEmailAddress = new SimpleStringProperty(email_address);
		this.pDeal_ID = new SimpleIntegerProperty(deal_id);
		this.pStatus = new SimpleStringProperty("current");
		this.pQuantity = new SimpleIntegerProperty(quantity);
		
		
		Integer customerID = Customer.getCustomerID(email_address);
		this.pCustomer_ID = new SimpleIntegerProperty(customerID);
		
		Integer paymentID = PaymentMethod.getPaymentID(customerID);
		this.pPayment_ID = new SimpleIntegerProperty(paymentID);
		
		
		
		// 1-Jun-2013
		//java.sql.Date sqlDate;
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY");
		//get current date time with Date()
		Date date = new Date();
		
		String currDate = dateFormat.format(date);
		this.pTrans_date = new SimpleStringProperty(currDate);
		return true;
	}

	// CRUD
	private int create( String email_address, Integer deal_id, Integer quantity) {
		
		
		if(!connect()) AppUtil.console("Not able to connect to database!");
		
		if(!isValidDeal(deal_id)) return 0;
		
		// not enough vouchers :(
		if(!setProperties(email_address,deal_id,quantity)) return 0;
		
		int transaction_id = 0;
		
		for(SimpleIntegerProperty voucher : vl) {
			
			//   INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','1','1','1');
			String setTransactionSql = "BEGIN INSERT INTO " +
					"Transaction (transaction_ID,trans_date,voucher_ID,payment_ID,customer_ID) " +
					"VALUES (seq_transaction.nextval, ?,?,?,?) RETURNING transaction_ID INTO ?; END;";
			
			java.sql.CallableStatement stmt = null;
			
			try {
				stmt = conn.prepareCall(setTransactionSql);
				stmt.setString(1, this.getPTrans_date());
				stmt.setInt(2, voucher.get());
				stmt.setInt(3, this.getPPayment_ID()); 
				stmt.setInt(4, this.getPCustomer_ID());
				
				stmt.registerOutParameter(5, java.sql.Types.INTEGER);	
				stmt.execute();
				transaction_id = stmt.getInt(5);
				this.pTransaction_ID = new SimpleIntegerProperty(transaction_id);
				stmt.close();
				
			} catch (SQLException sqle) {
				AppUtil.console("Purchase insert error: "+sqle);
				return -1;
			}
			
			AppUtil.console("Purchase_ID: "+transaction_id);
		}
		return transaction_id;
	}
	

	
	public static ArrayList<Purchase> retrieve(
			Connection conn,
			TextField fxtfVoucherID,
			TextField fxtfStatus, 
			TextField fxtfDealID, 
			TextField fxtfTransactionID, 
			TextField fxtfTransDate, 
			TextField fxtfPaymentID,
			TextField fxtfCustomerID,
			TextField fxtfEmailAddress) 
	{
		connect();
		
		int start = 0;
		String sqlWhere = " WHERE ";
		if(FormValidation.textFieldTypeInteger(fxtfVoucherID)) {
			String voucher_ID = "voucher_ID = "+"\'"+fxtfVoucherID.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+voucher_ID;
			sqlWhere += voucher_ID;
			start++;
		}
		if(FormValidation.textFieldNotEmpty(fxtfStatus)) {
			String status = "status = "+"\'"+fxtfStatus.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+status;
			else sqlWhere += status;
			start++;
		}
		if(FormValidation.textFieldTypeInteger(fxtfDealID)) {
			String deal_ID = "deal_ID = "+"\'"+fxtfDealID.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+deal_ID;
			else sqlWhere += deal_ID;
			start++;
		}
		if(FormValidation.textFieldTypeInteger(fxtfTransactionID)) {
			String transaction_ID = "transaction_ID = "+"\'"+fxtfTransactionID.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+transaction_ID;
			else sqlWhere += transaction_ID;
			start++;
		}
		if(FormValidation.textFieldNotEmpty(fxtfTransDate)) {
			String trans_date = "trans_date = "+"\'"+fxtfTransDate.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+trans_date;
			else sqlWhere += trans_date;
			start++;
		}
		if(FormValidation.textFieldTypeInteger(fxtfPaymentID)) {
			String payment_ID = "payment_ID = "+"\'"+fxtfPaymentID.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+payment_ID;
			else sqlWhere += payment_ID;
			start++;
		}
		if(FormValidation.textFieldTypeInteger(fxtfCustomerID)) {
			String customer_ID = "customer_ID = "+"\'"+fxtfCustomerID.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+customer_ID;
			else sqlWhere += customer_ID;
			start++;
		}
		if(FormValidation.textFieldNotEmpty(fxtfEmailAddress)) {
			String email_address = "email_address = "+"\'"+fxtfEmailAddress.getText()+"\'";
			if(start>0) sqlWhere+=" AND "+email_address;
			else sqlWhere += email_address;
			start++;
		}
		
		
		PreparedStatement preparedStatement = null;
 
		String selectSQL;
		if(start>0) {
			selectSQL = "SELECT * FROM Purchase "+sqlWhere;
		}else {
			selectSQL = "SELECT * FROM Purchase";
		}
		
		AppUtil.console("Select String: "+selectSQL);
 
		ResultSet rs = null;
		ArrayList<Purchase> pl = new ArrayList<Purchase>();
		
		try {

			preparedStatement = conn.prepareStatement(selectSQL);
			
 
			// execute select SQL
			rs = preparedStatement.executeQuery();

			TextField tfDealID = new TextField();
			TextField tfTransactionID = new TextField(); 
			TextField tfEmailAddress = new TextField();
			TextField tfQuantity = new TextField();
			TextField tfVoucherID = new TextField();
			
			while (rs.next()) {
				
				tfTransactionID.setText( ((Integer) rs.getInt("transaction_ID")).toString());
				tfEmailAddress.setText(rs.getString("email_address"));
				tfDealID.setText( ((Integer) rs.getInt("deal_ID")).toString());
				tfQuantity.setText("1");
				tfVoucherID.setText( ((Integer) rs.getInt("voucher_ID")).toString() );
				
				pl.add(new Purchase(Purchase.conn,
						tfTransactionID,
						tfEmailAddress,
						tfDealID,
						tfQuantity,
						tfVoucherID));			
 
			}
 
			preparedStatement.close();
			//rs.first(); // It seems this non-scrollable result set resets to first automagically
			
			
		} catch (SQLException e) {
 
			AppUtil.console(e.getMessage());
			
		}
		
		
		return pl;
 
	}
	
	public static boolean update(Purchase oldPurchase, Purchase newPurchase) {
		if(!connect()) AppUtil.console("Not able to connect to database!");
		
		
		AppUtil.console("Modifying voucher status: "+oldPurchase.getPVoucher_ID());
		
		String sql = "UPDATE Voucher SET "+
				"status= \'"+newPurchase.getPStatus()+"\',"+
				" WHERE voucher_ID = \'"+newPurchase.getPVoucher_ID()+"\'";
		
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
	
		public static boolean delete(Purchase oldPurchase) {
			if(!connect()) AppUtil.console("Not able to connect to database!");
			
			
			AppUtil.console("Deleting purchase: "+oldPurchase.getPTransaction_ID());
			
			String sql = "DELETE FROM Transaction "+ 
					"WHERE transaction_ID = \'"+oldPurchase.getPTransaction_ID()+"\'";
			
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
		
	public static boolean sellVoucher(Integer voucher_id) {
		if(!connect()) AppUtil.console("Not able to connect to database!");
		
		
		AppUtil.console("Updating voucher status. ");
		
		String sql = "UPDATE Voucher SET "+
				"status= \'1\',"+
				" WHERE voucher_ID = \'"+voucher_id+"\'";
		
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

		public static Integer getUnsoldVoucherID(Integer deal_id) {
			String getVoucherIDsql = "SELECT voucher_ID FROM Voucher WHERE deal_ID=\'"+deal_id+"\'"+
					" AND sold=\'0\'";
			
			Integer id = 0;
			connect();
			ResultSet rs = null;
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = conn.prepareStatement(getVoucherIDsql);
				rs = preparedStatement.executeQuery();
				rs.next();
				id = rs.getInt("voucher_ID");
				preparedStatement.close();
			} catch (Exception e) {
				AppUtil.console("Most likely a DDL error, not a problem."+e);
			}
			return id;
		}

	private static boolean connect() {
		if(Purchase.conn==null) {
			Purchase.conn=new PaymentMethodConnection(OracleConnection.user,OracleConnection.pass).getConnection();
			return true;
		}
		return true;
	}
	
	
	public Integer getPVoucher_ID() {
		return pVoucher_ID.get();
	}

	public String getPStatus() {
		return pStatus.get();
	}

	public Integer getPDeal_ID() {
		return pDeal_ID.get();
	}

	public Integer getPTransaction_ID() {
		return pTransaction_ID.get();
	}

	public String getPTrans_date() {
		return pTrans_date.get();
	}

	public Integer getPPayment_ID() {
		return pPayment_ID.get();
	}

	public Integer getPCustomer_ID() {
		return pCustomer_ID.get();
	}

	public String getPEmailAddress() {
		return pEmailAddress.get();
	}
	
	public Integer getPQuantity() {
		return pQuantity.get();
	}
	

	
}
