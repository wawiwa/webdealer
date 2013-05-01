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
import edu.gmu.cs.infs614.webdealer.model.connector.LocationConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

public class Location {

	
	/*CREATE TABLE Location(
			location_ID INTEGER, 
			city VARCHAR2(30),
			state VARCHAR2(2),
			country VARCHAR2(2),
			continent VARCHAR2(2),
			PRIMARY KEY (location_ID));
			ALTER TABLE Location
			add constraint Location_name UNIQUE (city, state);*/
	
	public boolean isInDatabase = false;
	private SimpleIntegerProperty lID; 
	private SimpleStringProperty lCity;
	private SimpleStringProperty lState;
	private SimpleStringProperty lCountry;
	private SimpleStringProperty lContinent;
	private static Connection conn;
	
	public Location(Connection conn, Integer location_ID, String city, String state, String country, String continent){
		this.lID=null;
		this.lCity=null;
		this.lState=null;
		this.lCountry=null;
		this.lContinent=null;
		Location.conn=conn;
	
		
		// create a new location in the database
				if(location_ID == null) {
					int result = create(city,state,country,continent);
					if(result == -1) {
						isInDatabase = false;
						return;
					}
					else {
						isInDatabase = true;
					}

					this.lID = new SimpleIntegerProperty(result);

				}
				// create just a location view
				else {
					this.lID = new SimpleIntegerProperty(location_ID);
				}


				
				this.lCity=new SimpleStringProperty(city);
				this.lState=new SimpleStringProperty(state);
				this.lCountry=new SimpleStringProperty(country);
				this.lContinent=new SimpleStringProperty(continent);
				
				AppUtil.console("Location constructed");	
			}
	
	// Think about how we can model the key that's generated by Oracle	
		public Location(
				Connection conn,
				TextField tflocation_ID,
				TextField tfcity,
				TextField tfstate,
				TextField tfcountry,
				TextField tfcontinent)
		{
			Location.conn = conn;

			if(tflocation_ID == null) {
				int result = create(tfcity.getText(),tfstate.getText(),tfcountry.getText(),tfcontinent.getText());
				if(result == -1) {
					isInDatabase = false;
					return;
				}
				else {
					isInDatabase = true;
				}
				this.lID = new SimpleIntegerProperty(result);
			}
			else {
				this.lID = new SimpleIntegerProperty(Integer.parseInt(tflocation_ID.getText()));
			}


			
			this.lCity=new SimpleStringProperty(tfcity.getText());
			this.lState=new SimpleStringProperty(tfstate.getText());
			this.lCountry=new SimpleStringProperty(tfcountry.getText());
			this.lContinent=new SimpleStringProperty(tfcontinent.getText());
			
			AppUtil.console("Location constructed");
		}
		// CRUD
				private int create(
						String city, 
						String state, 
						String country,
						String continent){
					
					if(!connect()) AppUtil.console("Not able to connect to database!");
					
					String sql = "BEGIN INSERT INTO " +
									"Location (location_ID,city,state,country,continent)" +
									"VALUES (seq_location.nextval, ?,?,?,?) RETURNING location_ID INTO ?; END;";
					
					java.sql.CallableStatement stmt = null;
					
					
					int generatedKey = 0;
	
					try {
						stmt = conn.prepareCall(sql);
						stmt.setString(1, city);
						stmt.setString(2, state);
						stmt.setString(3, country);
						stmt.setString(4, continent);
										
						stmt.registerOutParameter(5, java.sql.Types.INTEGER);	
						stmt.execute();
						generatedKey = stmt.getInt(5);
						stmt.close();
						
					} catch (SQLException sqle) {
						AppUtil.console("Location insert error: "+sqle);
						return -1;
					}
					
					AppUtil.console("Location_ID: "+generatedKey);
					return generatedKey;
				}
				public static ArrayList<Location> retrieve(Connection conn,
						TextField tfLocation_ID,
						TextField tfcity,
						TextField tfstate,
						TextField tfcountry,
						TextField tfcontinent) 
				{
					if(!connect()) AppUtil.console("Not able to connect to database!");
					
					int start = 0;
					String sqlWhere = " WHERE ";
					if(FormValidation.textFieldTypeInteger(tfLocation_ID)) {
						String location_ID = "location_ID = "+"\'"+tfLocation_ID.getText()+"\'";
						if(start>0) sqlWhere+=" AND "+ location_ID;
						sqlWhere += location_ID;
						start++;
					}
					
					if(FormValidation.textFieldNotEmpty(tfcity)) {
						String city = "regexp_LIKE(city, '*" + tfcity.getText() +"*','i')";
						if(start>0) sqlWhere+=" AND "+city;
						else sqlWhere += city;
						start++;
					}
					
					if(FormValidation.textFieldNotEmpty(tfstate)) {
						String state = "regexp_LIKE(state, '*" + tfstate.getText() +"*','i')";
						if(start>0) sqlWhere+=" AND "+state;
						else sqlWhere += state;
						start++;
					}
					
					if(FormValidation.textFieldNotEmpty(tfcountry)) {
						String country = "regexp_LIKE(country, '*" + tfcountry.getText() +"*','i')";
						if(start>0) sqlWhere+=" AND "+country;
						else sqlWhere += country;
						start++;
					}
					
					if(FormValidation.textFieldNotEmpty(tfcontinent)) {
						String continent = "regexp_LIKE(continent, '*" + tfcontinent.getText() +"*','i')";
						if(start>0) sqlWhere+=" AND "+continent;
						else sqlWhere += continent;
						start++;
					}
					
						
					PreparedStatement preparedStatement = null;
					
					String selectSQL;
					if(start>0) {
						selectSQL = "SELECT location_ID, city, state, country, continent FROM LOCATION"+sqlWhere;
					}else {
						selectSQL = "SELECT location_ID, city, state, country, continent FROM LOCATION";
					}
					
					AppUtil.console("Select String: "+selectSQL);
			 
					ResultSet rs = null;
					ArrayList<Location> ll = new ArrayList<Location>();
					
					
					
					TextField tflid = new TextField();
					TextField tfcit = new TextField();
					TextField tfsta = new TextField();
					TextField tfcoun = new TextField();
					TextField tfcont = new TextField();
					
					try {

						preparedStatement = conn.prepareStatement(selectSQL);
						
			 
						// execute select SQL
						rs = preparedStatement.executeQuery();
			 
						while (rs.next()) {
							
							tflid.setText(((Integer)rs.getInt("location_ID")).toString());
							tfcit.setText(rs.getString("city"));
							tfsta.setText(rs.getString("state"));
							tfcoun.setText(rs.getString("country"));
							tfcont.setText(rs.getString("continent"));
							
							ll.add(new Location(Location.conn,tflid,tfcit,tfsta,tfcoun,
									tfcont));

							AppUtil.console("location_ID : " + rs.getInt("location_ID"));
							AppUtil.console("city : " + rs.getString("city"));
							AppUtil.console("state : " + rs.getString("state"));
							AppUtil.console("country : " + rs.getString("country"));
							AppUtil.console("continent : " + rs.getString("continent"));
							
			 
						}
			 
						preparedStatement.close();
						//rs.first(); // It seems this non-scrollable result set resets to first automagically
						
						
					} catch (SQLException e) {
			 
						AppUtil.console(e.getMessage());
						
					}
					
					AppUtil.console("LL list size: " + ll.size());
					return ll;
			 
				}
					
				public static boolean update(Location oldLocation, Location newLocation) {
					if(!connect()) AppUtil.console("Not able to connect to database!");
					
					
					AppUtil.console("Modifying location: "+oldLocation.getLID());
					
					String sql = "UPDATE Location SET "+
							"city = \'"+newLocation.getLCity()+"\',"+
							"state = \'"+newLocation.getLState()+"\',"+
							"country = \'"+newLocation.getLCountry()+"\',"+
							"continent = \'"+newLocation.getLContinent()+"\',"+
														
							" WHERE location_ID = \'"+newLocation.getLID()+"\'";
					
				
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
					
				public static boolean delete(Location oldLocation) {
					if(!connect()) AppUtil.console("Not able to connect to database!");
					
					
					AppUtil.console("Deleting location: "+oldLocation.getLID());
					
					String sql = "DELETE FROM Location "+ 
							"WHERE location_ID = \'"+oldLocation.getLID()+"\'";
					
					AppUtil.console("DELETE: "+sql);

					PreparedStatement preparedStatement = null;
					try {
						preparedStatement = conn.prepareStatement(sql);
						AppUtil.console("prep");
						preparedStatement.executeQuery();
						AppUtil.console("execute");
						preparedStatement.close();
						AppUtil.console("close");
					} catch (Exception e) {
						AppUtil.console("M: Most likely a DDL error, not a problem."+e);
					}
					return true;
				}

				
		// getters 

		public Integer getLID() {
			return lID.get();
		}


		public String getLCity() {
			return lCity.get();
		}

		public String getLState() {
			return lState.get();
		}

		public String getLCountry() {
			return lCountry.get();
		}

		public String getLContinent() {
			return lContinent.get();
		}

		private static boolean connect() {
			if(Location.conn==null) {
				Location.conn=new LocationConnection(LocationConnection.user,LocationConnection.pass).getConnection();
				return true;
			}
			return true;
		}
			
		}

