package edu.gmu.cs.infs614.webdealer.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.model.connector.DealConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.sql.Date;
//import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;


//import edu.gmu.cs.infs614.webdealer.view.FormValidation;





//Immutable tuple (aka primary key)

/*deal_ID INTEGER,
expiration_date DATE,
description VARCHAR2(500),
quantity_limit INTEGER,
original_price REAL,
deal_price REAL,
sale_start_time DATE,
sale_end_time DATE,
location_ID INTEGER,
category_ID INTEGER,
merchant_ID INTEGER
*/

public class Deal {
	
	java.sql.Date sqlDate;
	DateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY");
	
	public boolean isInDatabase = false;
	private SimpleIntegerProperty dID = null; 
	private SimpleStringProperty dExpDate = null;
	private SimpleStringProperty dDescription = null;
	private SimpleIntegerProperty dQuantity = null;
	private SimpleFloatProperty dOrigPrice = null;
	private SimpleFloatProperty dDealPrice = null;
	private SimpleStringProperty dSaleStart = null;
	private SimpleStringProperty dSaleEnd = null;
	private SimpleIntegerProperty lID = null;
	private SimpleIntegerProperty catID = null;
	private SimpleIntegerProperty mID = null;
	
	
	//more props
	private SimpleStringProperty dCityName;
	private SimpleStringProperty dMerchant;
	
	
	private static Connection conn;

	public Deal(Connection conn, 
			Integer deal_ID, 
			String expiration_date, 
			String description, 
			Integer quantity_limit,
			Float original_price, 
			Float deal_price, 
			String sale_start_time, 
			String sale_end_time, 
			Integer location_ID, 
			Integer category_ID, 
			Integer merchant_ID){

		Deal.conn=conn;
		
		// create a new deal in the database
		if(deal_ID == null) {
			int result = create(expiration_date,description,quantity_limit,original_price,deal_price,sale_start_time,sale_end_time,location_ID,category_ID,merchant_ID);
			if(result == -1) {
				isInDatabase = false;
				return;
			}
			else {
				isInDatabase = true;
			}
			
			this.dID = new SimpleIntegerProperty(result);
					
			}
		// create just a deal view
				else {
					this.dID = new SimpleIntegerProperty(deal_ID);
				}
				
				
				this.dExpDate=new SimpleStringProperty(expiration_date);
				this.dDescription=new SimpleStringProperty(description);
				this.dQuantity=new SimpleIntegerProperty(quantity_limit);
				this.dOrigPrice=new SimpleFloatProperty(original_price);
				this.dDealPrice=new SimpleFloatProperty(deal_price);
				this.dSaleStart=new SimpleStringProperty(sale_start_time);
				this.dSaleEnd=new SimpleStringProperty(sale_end_time);
				this.lID=new SimpleIntegerProperty(location_ID);
				this.catID=new SimpleIntegerProperty(category_ID);
				this.mID=new SimpleIntegerProperty(merchant_ID);
				AppUtil.console("Deal constructed");
				
				
				
			}
	// Think about how we can model the key that's generated by Oracle	
		public Deal(
				Connection conn,
				TextField tfDeal_ID,
				TextField tfexpiration_date,
				TextField tfdescription,
				TextField tfquantity_limit,
				TextField tforiginal_price,
				TextField tfdeal_price,
				TextField tfsale_start_time,
				TextField tfsale_end_time,
				TextField tflocation_ID,
				TextField tfcategory_ID,
				TextField tfmerchant_ID) {
			Deal.conn = conn;
			// had to check for null before calling tfDealID.getText(), so we just copied the constructor code
			
			
			// if quantity not included, set to quantity
			if(tfquantity_limit == null || tfquantity_limit.getText().isEmpty()) {
				tfquantity_limit = new TextField();
				tfquantity_limit.setText("0");
				this.dQuantity = new SimpleIntegerProperty(Integer.parseInt(tfquantity_limit.getText()));
			}else if (!tfquantity_limit.getText().isEmpty()){ // voucher id included in textfield
						
				this.dQuantity = new SimpleIntegerProperty(Integer.parseInt(tfquantity_limit.getText()));
			}
			
			if(tfDeal_ID == null) {
				int result = create(tfexpiration_date.getText(),tfdescription.getText(),Integer.parseInt(tfquantity_limit.getText()),Float.parseFloat(tforiginal_price.getText()),Float.parseFloat(tfdeal_price.getText()),
						tfsale_start_time.getText(),tfsale_end_time.getText(),Integer.parseInt(tflocation_ID.getText()),Integer.parseInt(tfcategory_ID.getText()),Integer.parseInt(tfmerchant_ID.getText()));
				if(result == -1) {
					isInDatabase = false;
					return;
				}
				else {
					isInDatabase = true;
				}
				this.dID = new SimpleIntegerProperty(result);
				}
			else {
				this.dID = new SimpleIntegerProperty(Integer.parseInt(tfDeal_ID.getText()));
			}

			
			
			this.dExpDate=new SimpleStringProperty(tfexpiration_date.getText());
			this.dDescription=new SimpleStringProperty(tfdescription.getText());
			//this.dQuantity=new SimpleIntegerProperty(Integer.parseInt(tfquantity_limit.getText()));
			this.dOrigPrice=new SimpleFloatProperty(Float.parseFloat(tforiginal_price.getText()));
			this.dDealPrice=new SimpleFloatProperty(Float.parseFloat(tfdeal_price.getText()));
			this.dSaleStart=new SimpleStringProperty(tfsale_start_time.getText());
			this.dSaleEnd=new SimpleStringProperty(tfsale_end_time.getText());
			this.lID=new SimpleIntegerProperty(Integer.parseInt(tflocation_ID.getText()));
			this.catID=new SimpleIntegerProperty(Integer.parseInt(tfcategory_ID.getText()));
			this.mID=new SimpleIntegerProperty(Integer.parseInt(tfmerchant_ID.getText()));

			AppUtil.console("Deal constructed");
		}	

		// CRUD
		private int create(
				String expiration_date, 
				String description, 
				Integer quantity_limit,
				Float original_price, 
				Float deal_price, 
				String sale_start_time, 
				String sale_end_time, 
				Integer location_ID,
				Integer category_ID, 
				Integer merchant_ID) {
			
			if(!connect()) AppUtil.console("Not able to connect to database!");
			
			String sql = "BEGIN INSERT INTO " +
							"Deal (deal_ID,expiration_date,description,quantity_limit,original_price,deal_price,sale_start_time," +
							"sale_end_time,location_ID,category_ID,merchant_ID) " +
							"VALUES (seq_deal.nextval, ?,?,?,?,?,?,?,?,?,?) RETURNING deal_ID INTO ?; END;";
			
			java.sql.CallableStatement stmt = null;
			
			
			int generatedKey = 0;
			
			try {
				stmt = conn.prepareCall(sql);
				stmt.setString(1, expiration_date);
				stmt.setString(2, description);
				stmt.setInt(3, quantity_limit);
				stmt.setFloat(4, original_price);
				stmt.setFloat(5, deal_price);
				stmt.setString(6, sale_start_time);
				stmt.setString(7, sale_end_time);
				stmt.setInt(8, location_ID);
				stmt.setInt(9, category_ID);
				stmt.setInt(10, merchant_ID);
								
				stmt.registerOutParameter(11, java.sql.Types.INTEGER);	
				stmt.execute();
				generatedKey = stmt.getInt(11);
				stmt.close();
				
			} catch (SQLException sqle) {
				AppUtil.console("Deal insert error: "+sqle);
				return -1;
			}
			
			AppUtil.console("Deal_ID: "+generatedKey);
			return generatedKey;
		}

		public static ArrayList<Deal> retrieve(Connection conn,TextField tfDeal_ID,TextField tfexpiration_date,TextField tfdescription,
				TextField tfquantity_limit,TextField tforiginal_price,TextField tfdeal_price,TextField tfsale_start_time,TextField tfsale_end_time,TextField tflocation_ID,TextField tfcategory_ID,TextField tfmerchant_ID) 
		{
			if(!connect()) AppUtil.console("Not able to connect to database!");
			
			int start = 0;
			String sqlWhere = " WHERE ";
			if(FormValidation.textFieldTypeInteger(tfDeal_ID)) {
				String deal_ID = "deal_ID = "+"\'"+tfDeal_ID.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+ deal_ID;
				sqlWhere += deal_ID;
				start++;
			}
			if(FormValidation.textFieldNotEmpty(tfexpiration_date)) {
				String expiration_date = "expiration_date = "+"\'"+tfexpiration_date.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+expiration_date;
				else sqlWhere += expiration_date;
				start++;
			}
			
			if(FormValidation.textFieldNotEmpty(tfdescription)) {
				String description = "regexp_LIKE(description, '*" + tfdescription.getText() +"*','i')";
				if(start>0) sqlWhere+=" AND "+description;
				else sqlWhere += description;
				start++;
			}
			
			if(FormValidation.textFieldNotEmpty(tfquantity_limit)) {
				String quantity_limit = "quantity_limit = "+"\'"+tfquantity_limit.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+quantity_limit;
				else sqlWhere += quantity_limit;
				start++;
			}
			
			
			if(FormValidation.textFieldNotEmpty(tforiginal_price)) {
				String original_price = "original_price = "+"\'"+tforiginal_price.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+original_price;
				else sqlWhere += original_price;
				start++;
			}
			
			if(FormValidation.textFieldNotEmpty(tfdeal_price)) {
				String deal_price = "deal_price = "+"\'"+tfdeal_price.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+deal_price;
				else sqlWhere += deal_price;
				start++;
			}
			
			
			if(FormValidation.textFieldNotEmpty(tfsale_start_time)) {
				String sale_start_time = "sale_start_time = "+"\'"+tfsale_start_time.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+sale_start_time;
				else sqlWhere += sale_start_time;
				start++;
			}
			
			if(FormValidation.textFieldNotEmpty(tfsale_end_time)) {
				String sale_end_time = "sale_end_time = "+"\'"+tfsale_end_time.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+sale_end_time;
				else sqlWhere += sale_end_time;
				start++;
			}
			
			if(FormValidation.textFieldNotEmpty(tflocation_ID)) {
				String location_ID = "location_ID = "+"\'"+tflocation_ID.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+location_ID;
				else sqlWhere += location_ID;
				start++;
			}
			
			if(FormValidation.textFieldNotEmpty(tfcategory_ID)) {
				String category_ID = "category_ID = "+"\'"+tfcategory_ID.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+category_ID;
				else sqlWhere += category_ID;
				start++;
			}
			
			if(FormValidation.textFieldNotEmpty(tfmerchant_ID)) {
				String merchant_ID = "merchant_ID = "+"\'"+tfmerchant_ID.getText()+"\'";
				if(start>0) sqlWhere+=" AND "+merchant_ID;
				else sqlWhere += merchant_ID;
				start++;
			}
				
			PreparedStatement preparedStatement = null;
			
			String selectSQL;
			if(start>0) {
				selectSQL = "SELECT * FROM DEAL "+sqlWhere;
			}else {
				selectSQL = "SELECT * FROM DEAL";
			}
			
			AppUtil.console("Select String: "+selectSQL);
	 
			ResultSet rs = null;
			ArrayList<Deal> dl = new ArrayList<Deal>();
			
			
			
			TextField tfdid = new TextField();
			TextField tfexp = new TextField();
			TextField tfdes = new TextField();
			TextField tfqty = new TextField();
			TextField tforg = new TextField();
			TextField tfprc = new TextField();
			TextField tfstr = new TextField();
			TextField tfend = new TextField();
			TextField tfloc = new TextField();
			TextField tfcat = new TextField();
			TextField tfmid = new TextField();
			
			try {

				preparedStatement = conn.prepareStatement(selectSQL);
				
	 
				// execute select SQL
				rs = preparedStatement.executeQuery();
	 
				while (rs.next()) {
					
					tfdid.setText(((Integer)rs.getInt("deal_ID")).toString());
					tfexp.setText(rs.getString("expiration_date"));
					tfdes.setText(rs.getString("description"));
					tfqty.setText(((Integer)rs.getInt("quantity_limit")).toString());
					tforg.setText(((Float)rs.getFloat("original_price")).toString());
					tfprc.setText(((Float)rs.getFloat("deal_price")).toString());
					tfstr.setText(rs.getString("sale_start_time"));
					tfend.setText(rs.getString("sale_end_time"));
					tfloc.setText(((Integer)rs.getInt("location_ID")).toString());
					tfcat.setText(((Integer)rs.getInt("category_ID")).toString());
					tfmid.setText(((Integer)rs.getInt("merchant_ID")).toString());
					
					dl.add(new Deal(Deal.conn,tfdid,tfexp,tfdes,tfqty,
							tforg,tfprc,tfstr,tfend,tfloc,tfcat,tfmid));

					AppUtil.console("deal_ID : " + rs.getInt("deal_ID"));
					AppUtil.console("expiration_date : " + rs.getString("expiration_date"));
					AppUtil.console("description : " + rs.getString("description"));
					AppUtil.console("quantity_limit : " + rs.getInt("quantity_limit"));
					AppUtil.console("original_price : " + rs.getFloat("original_price"));
					AppUtil.console("deal_price : " + rs.getFloat("deal_price"));
					AppUtil.console("sale_start_time : " + rs.getString("sale_start_time"));
					AppUtil.console("sale_end_time : " + rs.getString("sale_end_time"));
					AppUtil.console("location_ID : " + rs.getInt("location_ID"));
					AppUtil.console("category_ID : " + rs.getInt("category_ID"));
					AppUtil.console("merchant_ID : " + rs.getInt("merchant_ID"));
	 
				}
	 
				preparedStatement.close();
				//rs.first(); // It seems this non-scrollable result set resets to first automagically
				
				
			} catch (SQLException e) {
	 
				AppUtil.console(e.getMessage());
				
			}
			
			AppUtil.console("DL list size: " + dl.size());
			return dl;
	 
		}
			
		public static boolean update(Deal oldDeal, Deal newDeal) {
			if(!connect()) AppUtil.console("Not able to connect to database!");
			
			
			AppUtil.console("Modifying deal: "+oldDeal.getDID());
			
			String sql = "UPDATE Deal SET "+
					"expiration_date = \'"+newDeal.getDExpDate()+"\',"+
					"description = \'"+newDeal.getDDescription()+"\',"+
					"quantity_limit = \'"+newDeal.getDQuantity()+"\',"+
					"original_price = \'"+newDeal.getDOrigPrice()+"\',"+
					"deal_price = \'"+newDeal.getDDealPrice()+"\',"+
					"sale_start_time = \'"+newDeal.getDSaleStart()+"\',"+
					"sale_end_time = \'"+newDeal.getDSaleEnd()+"\',"+
					"location_ID = \'"+newDeal.getLID()+"\',"+
					"category_ID = \'"+newDeal.getCatID()+"\',"+
					"merchant_ID = \'"+newDeal.getMID()+"\'"+
					
					" WHERE deal_ID = \'"+newDeal.getDID()+"\'";
			
		
			AppUtil.console("UPDATE: "+sql);

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.executeQuery();
				preparedStatement.close();
			} catch (Exception e) {
				
				AppUtil.console("M: Most likely a DDL error, not a problem."+e);
				return false;
				
			}
			return true;
		}		
			
		public static boolean delete(Deal oldDeal) {
			if(!connect()) AppUtil.console("Not able to connect to database!");
			
			
			AppUtil.console("Deleting deal: "+oldDeal.getDID());
			
			String sql = "DELETE FROM Deal "+ 
					"WHERE deal_ID = \'"+oldDeal.getDID()+"\'";
			
			AppUtil.console("DELETE: "+sql);

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.executeQuery();
				preparedStatement.close();
			} catch (Exception e) {
				AppUtil.console("M: Most likely a DDL error, not a problem."+e);
			}
			return true;
		}

	
	public static ArrayList<Deal> citySearchretrieve(Connection conn,TextField tfDeal_ID,TextField tfexpiration_date,TextField tfdescription,
				TextField tfquantity_limit,TextField tforiginal_price,TextField tfdeal_price,TextField tfsale_start_time,TextField tfsale_end_time,
				TextField tflocation_ID,TextField tfcategory_ID,TextField tfmerchant_ID, TextField tfcity_name) 
		{
			if(!connect()) AppUtil.console("Not able to connect to database!");
			
			int start = 0;
			String sqlWhere = "WHERE D.merchant_id=M.merchant_ID AND D.location_ID=L.location_id";
		
			if(FormValidation.textFieldNotEmpty(tfcity_name)) {
				String city_search = " AND regexp_LIKE(L.city, \'*" + tfcity_name.getText() +"*\',\'i\')";
				sqlWhere += city_search;
				start++;
			}
			
			
				
			PreparedStatement preparedStatement = null;
			
			String selectSQL;
			if(start>0) {
				selectSQL = "SELECT D.deal_id,D.expiration_date,D.description,D.quantity_limit,D.original_price,D.deal_price,D.sale_start_time,D.sale_end_time,D.location_ID,D.category_ID,D.merchant_ID,M.merchant_name,L.city,L.state,L.country,L.continent FROM DEAL D, MERCHANT M, LOCATION L "+sqlWhere;
			}else {
				selectSQL = "SELECT D.deal_id,D.expiration_date,D.description,D.quantity_limit,D.original_price,D.deal_price,D.sale_start_time,D.sale_end_time,D.location_ID,D.category_ID,D.merchant_ID,M.merchant_name,L.city,L.state,L.country,L.continent FROM DEAL D, MERCHANT M, LOCATION L";
			}
			
			AppUtil.console("Select String: "+selectSQL);
	 
			ResultSet rs = null;
			ArrayList<Deal> dl = new ArrayList<Deal>();
			
			
			try {

				preparedStatement = conn.prepareStatement(selectSQL);
				
	 
				// execute select SQL
				rs = preparedStatement.executeQuery();
	 
				/*
				 * public Deal(Connection conn, 
			Integer deal_ID, 
			String expiration_date, 
			String description, 
			Integer quantity_limit,
			Float original_price, 
			Float deal_price, 
			String sale_start_time, 
			String sale_end_time, 
			Integer location_ID, 
			Integer category_ID, 
			Integer merchant_ID
				 */
				while (rs.next()) {
					
					/*dl.add(new Deal(Deal.conn,
							rs.getInt("deal_ID"),
							rs.getString("expiration_date"),
							rs.getString("description"),
							rs.getInt("quantity_limit"),
							rs.getFloat("original_price"),
							rs.getFloat("deal_price"),
							rs.getString("sale_start_time"),
							rs.getString("sale_end_time"),
							rs.getInt("L.location_ID"),
							rs.getInt("category_ID"),
							rs.getInt("M.merchant_ID"),
							rs.getString("M.merchant_name"),
							rs.getString("L.city"),
							rs.getString("L.state"),
							rs.getString("L.country"),
							rs.getString("L.continent")));*/

					AppUtil.console("deal_ID : " + rs.getInt("deal_ID"));
					AppUtil.console("expiration_date : " + rs.getString("expiration_date"));
					AppUtil.console("description : " + rs.getString("description"));
					AppUtil.console("quantity_limit : " + rs.getInt("quantity_limit"));
					AppUtil.console("original_price : " + rs.getFloat("original_price"));
					AppUtil.console("deal_price : " + rs.getFloat("deal_price"));
					AppUtil.console("sale_start_time : " + rs.getString("sale_start_time"));
					AppUtil.console("sale_end_time : " + rs.getString("sale_end_time"));
					AppUtil.console("location_ID : " + rs.getInt("location_ID"));
					AppUtil.console("category_ID : " + rs.getInt("category_ID"));
					AppUtil.console("merchant_ID : " + rs.getInt("merchant_ID"));
	 
				}
	 
				preparedStatement.close();
				//rs.first(); // It seems this non-scrollable result set resets to first automagically
				
				
			} catch (SQLException e) {
	 
				AppUtil.console(e.getMessage());
				
			}
			
			
			return dl;
	 
		}	
		
		
		
		
	
		
		
// getters 

public Integer getDID() {
	return dID.get();
}


public String getDExpDate() {
	return dExpDate.get();
}

public String getDDescription() {
	return dDescription.get();
}

public Integer getDQuantity() {
	return dQuantity.get();
}

public Float getDOrigPrice() {
	return dOrigPrice.get();
}

public Float getDDealPrice() {
	return dDealPrice.get();
}

public String getDSaleStart() {
	return dSaleStart.get();
}

public String getDSaleEnd() {
	return dSaleEnd.get();
}


public Integer getLID() {
	return lID.get();
}

public Integer getCatID() {
	return catID.get();
}

public Integer getMID() {
	return mID.get();
}

private static boolean connect() {
	if(Deal.conn==null) {
		Deal.conn=new DealConnection(DealConnection.user,DealConnection.pass).getConnection();
		return true;
	}
	return true;
}
	
	
	
	
}