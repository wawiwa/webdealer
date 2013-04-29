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
import edu.gmu.cs.infs614.webdealer.model.connector.ReviewConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.sql.Date;
//import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;


//import edu.gmu.cs.infs614.webdealer.view.FormValidation;

//Immutable tuple (aka primary key)

/*review_ID INTEGER, 
rating INTEGER,
comments VARCHAR2(500),
deal_ID INTEGER,
customer_ID INTEGER,
*/

public class Review {
	
	public boolean isInDatabase = false;
	private SimpleIntegerProperty rID; 
	private SimpleIntegerProperty rRating;
	private SimpleStringProperty rComments;
	private SimpleIntegerProperty dID;
	private SimpleIntegerProperty cID;
	private static Connection conn;
	
	public Review(Connection conn, Integer review_ID, Integer rating, String comments, Integer deal_ID, Integer customer_ID){
		this.rID=null;
		this.rRating=null;
		this.rComments=null;
		this.dID=null;
		this.cID=null;
		Review.conn=conn;
		
		// create a new review in the database
				if(review_ID == null) {
					int result = create(rating,comments,deal_ID,customer_ID);
					if(result == -1) {
						isInDatabase = false;
						return;
					}
					else {
						isInDatabase = true;
					}
					
					this.rID = new SimpleIntegerProperty(result);
							
					}
				// create just a review view
				else {
					this.rID = new SimpleIntegerProperty(review_ID);
				}
				
				
				this.rRating=new SimpleIntegerProperty(rating);
				this.rComments=new SimpleStringProperty(comments);
				this.dID=new SimpleIntegerProperty(deal_ID);
				this.cID=new SimpleIntegerProperty(customer_ID);
				AppUtil.console("Review constructed");	
			}
	// Think about how we can model the key that's generated by Oracle	
			public Review(
					Connection conn,
					TextField tfreview_ID,
					TextField tfrating,
					TextField tfcomments,
					TextField tfdeal_ID,
					TextField tfcustomer_ID)
				 {
				Review.conn = conn;
				// had to check for null before calling tfID.getText(), so we just copied the constructor code
				this.rID=null;
				this.rRating=null;
				this.rComments=null;
				this.dID=null;
				this.cID=null;
				
				
				if(tfreview_ID == null) {
					int result = create(Integer.parseInt(tfrating.getText()),tfcomments.getText(),Integer.parseInt(tfdeal_ID.getText()),Integer.parseInt(tfcustomer_ID.getText()));
					if(result == -1) {
						isInDatabase = false;
						return;
					}
					else {
						isInDatabase = true;
					}
					this.rID = new SimpleIntegerProperty(result);
					}
				else {
					this.rID = new SimpleIntegerProperty(Integer.parseInt(tfreview_ID.getText()));
				}
				
				
				this.rRating=new SimpleIntegerProperty(Integer.parseInt(tfrating.getText()));
				this.rComments=new SimpleStringProperty(tfcomments.getText());
				this.dID=new SimpleIntegerProperty(Integer.parseInt(tfdeal_ID.getText()));
				this.cID=new SimpleIntegerProperty(Integer.parseInt(tfcustomer_ID.getText()));
				AppUtil.console("Review constructed");
			}	
			// CRUD
			private int create(Integer rating, String comments, Integer deal_ID,Integer customer_ID) {
				
				if(!connect()) AppUtil.console("Not able to connect to database!");
				
				String sql = "BEGIN INSERT INTO " +
								"Review (review_ID,rating,comments,deal_ID,customer_ID)" +
								"VALUES (seq_review.nextval, ?,?,?,?) RETURNING review_ID INTO ?; END;";
				
				java.sql.CallableStatement stmt = null;
				
				
				int generatedKey = 0;
				
				try {
					stmt = conn.prepareCall(sql);
					stmt.setInt(1, rating);
					stmt.setString(2, comments);
					stmt.setInt(3, deal_ID);
					stmt.setInt(4, customer_ID);
					
									
					stmt.registerOutParameter(5, java.sql.Types.INTEGER);	
					stmt.execute();
					generatedKey = stmt.getInt(5);
					stmt.close();
					
				} catch (SQLException sqle) {
					AppUtil.console("Review insert error: "+sqle);
					return -1;
				}
				
				AppUtil.console("review_ID: "+generatedKey);
				return generatedKey;
			}

			public static ArrayList<Review> retrieve(Connection conn,TextField tfreview_ID,TextField tfrating,TextField tfcomments,
					TextField tfdeal_ID,TextField tfcustomer_ID) 
			{
				if(!connect()) AppUtil.console("Not able to connect to database!");
				
				int start = 0;
				String sqlWhere = " WHERE ";
				if(FormValidation.textFieldTypeInteger(tfreview_ID)) {
					String review_ID = "review_ID = "+"\'"+tfreview_ID.getText()+"\'";
					if(start>0) sqlWhere+=" AND "+ review_ID;
					sqlWhere += review_ID;
					start++;
				}
				if(FormValidation.textFieldNotEmpty(tfrating)) {
					String rating = "rating = "+"\'"+tfrating.getText()+"\'";
					if(start>0) sqlWhere+=" AND "+rating;
					else sqlWhere += rating;
					start++;
				}
				
				if(FormValidation.textFieldNotEmpty(tfcomments)) {
					String comments = "comments = "+"\'"+tfcomments.getText()+"\'";
					if(start>0) sqlWhere+=" AND "+comments;
					else sqlWhere += comments;
					start++;
				}
				
				if(FormValidation.textFieldNotEmpty(tfdeal_ID)) {
					String deal_ID = "deal_ID = "+"\'"+tfdeal_ID.getText()+"\'";
					if(start>0) sqlWhere+=" AND "+deal_ID;
					else sqlWhere += deal_ID;
					start++;
				}
				
				
				if(FormValidation.textFieldNotEmpty(tfcustomer_ID)) {
					String customer_ID = "customer_ID = "+"\'"+tfcustomer_ID.getText()+"\'";
					if(start>0) sqlWhere+=" AND "+customer_ID;
					else sqlWhere += customer_ID;
					start++;
				}
				
				
					
				PreparedStatement preparedStatement = null;
				
				String selectSQL;
				if(start>0) {
					selectSQL = "SELECT * FROM REVIEW "+sqlWhere;
				}else {
					selectSQL = "SELECT * FROM REVIEW";
				}
				
				AppUtil.console("Select String: "+selectSQL);
		 
				ResultSet rs = null;
				ArrayList<Review> rl = new ArrayList<Review>();
				
				
				try {

					preparedStatement = conn.prepareStatement(selectSQL);
					
		 
					// execute select SQL
					rs = preparedStatement.executeQuery();
		 
					while (rs.next()) {
						
						rl.add(new Review(Review.conn,
								rs.getInt("review_ID"),
								rs.getInt("rating"),
								rs.getString("comments"),
								rs.getInt("deal_ID"),
								rs.getInt("customer_ID")));

						AppUtil.console("review_ID : " + rs.getInt("review_ID"));
						AppUtil.console("rating : " + rs.getString("rating"));
						AppUtil.console("comments : " + rs.getString("comments"));
						AppUtil.console("deal_ID : " + rs.getInt("deal_ID"));
						AppUtil.console("customer_ID : " + rs.getInt("customer_ID"));
				
		 
					}
		 
					preparedStatement.close();
					//rs.first(); // It seems this non-scrollable result set resets to first automagically
					
					
				} catch (SQLException e) {
		 
					AppUtil.console(e.getMessage());
					
				}
				
				
				return rl;
		 
			}

			public static boolean update(Review oldReview, Review newReview) {
				if(!connect()) AppUtil.console("Not able to connect to database!");
				
				
				AppUtil.console("Modifying review: "+oldReview.getRID());
				
				String sql = "UPDATE Review SET "+
						"rating = \'"+newReview.getRRating()+"\',"+
						"comments = \'"+newReview.getRComments()+"\',"+
						"deal_ID = \'"+newReview.getDID()+"\',"+
						"customer_ID = \'"+newReview.getCID()+"\'"+
				
						
						" WHERE review_ID = \'"+newReview.getRID()+"\'";
				
			
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
				
			public static boolean delete(Review oldReview) {
				if(!connect()) AppUtil.console("Not able to connect to database!");
				
				
				AppUtil.console("Deleting review: "+oldReview.getRID());
				
				String sql = "DELETE FROM Review "+ 
						"WHERE review_ID = \'"+oldReview.getRID()+"\'";
				
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

		
	// getters 

	public Integer getRID() {
		return rID.get();
	}


	public Integer getRRating() {
		return rRating.get();
	}

	public String getRComments() {
		return rComments.get();
	}

	public Integer getDID() {
		return dID.get();
	}

	public Integer getCID() {
		return cID.get();
	}

	

	private static boolean connect() {
		if(Review.conn==null) {
			Review.conn = new ReviewConnection(ReviewConnection.user, ReviewConnection.pass).getConnection();
			return true;
		}
		return true;
	}
		
	
}