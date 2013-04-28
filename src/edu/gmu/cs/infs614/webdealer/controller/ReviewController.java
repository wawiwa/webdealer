package edu.gmu.cs.infs614.webdealer.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.model.Review;
import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.ReviewConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

public class ReviewController implements Initializable {
	// COMPLEX APPLICATION SEGMENTS
	@FXML
	ScrollPane fxScrollPane;
	
	@FXML
	AnchorPane fxAnchorPane;
	
	@FXML
	TextArea fxConsoleTextArea;
	
	// MAIN VIEWS
	@FXML
	Button fxReviewButton;
	
	// DEFINE TABLE
	
	@FXML
	TableView<Review> tvReview;
	@FXML
	TableColumn<Review, Integer> tcReview_ID;
	@FXML
	TableColumn<Review, Integer> tcRating;
	@FXML
	TableColumn<Review, String> tcComments;
	@FXML
	TableColumn<Review, Integer> tcDeal_ID;
	@FXML
	TableColumn<Review, Integer> tcCustomer_ID;

	
	// DEFINE FORM
	@FXML
	TextField tfreview_ID;
	@FXML
	TextField tfrating;
	@FXML
	TextField tfcomments;
	@FXML
	TextField tfdeal_ID;
	@FXML
	TextField tfcustomer_ID;
	
	
	@FXML
	Button submit;
	@FXML
	Button delete;
	@FXML
	Button clear;
	@FXML
	Button search;
	
	
	@FXML
	Label ratingLabel;
	@FXML
	Label commentsLabel;
	@FXML
	Label deal_IDLabel;
	@FXML
	Label customer_IDLabel;
	

	
	// DEFINE VARIABLES
	
	// index for delete item
	private IntegerProperty index = new SimpleIntegerProperty();
	
	// DB connector
	public Connection conn = new ReviewConnection(OracleConnection.user,OracleConnection.pass).getConnection();
	
	
	final ObservableList<Review> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		
		displayReview();
		
		// fix delete button working if not selecting once a table row
		index.set(-1);
		
		tcReview_ID.setCellValueFactory(new PropertyValueFactory<Review, Integer>("rID"));
		tcRating.setCellValueFactory(new PropertyValueFactory<Review, Integer>("rRating"));
		tcComments.setCellValueFactory(new PropertyValueFactory<Review, String>("rComments"));
		tcDeal_ID.setCellValueFactory(new PropertyValueFactory<Review, Integer>("dID"));
		tcCustomer_ID.setCellValueFactory(new PropertyValueFactory<Review, Integer>("cID"));

        AppUtil.console("DATA contents: "+data);

		tvReview.setItems(data);
		
		// get the index when clicking on table row
		tvReview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
				if(newValue==null)return;
				Review r = (Review) newValue;
				
				tfreview_ID.setText(r.getRID().toString()); 
				tfrating.setText(r.getRRating().toString());
				tfcomments.setText(r.getRComments().toString());
				tfdeal_ID.setText(r.getDID().toString());
				tfcustomer_ID.setText(r.getCID().toString());
			
				
				
				index.set(data.indexOf(newValue));
				AppUtil.console("OK index is: "+data.indexOf(newValue));
				AppUtil.console(r.getRID().toString());
			}
		});
	}

	public void onAddItem(ActionEvent event) {
		
		// FORM VALIDATION
		boolean rating = FormValidation.textFieldNotEmpty(tfrating,ratingLabel,"Rating is required!");
		boolean comments = FormValidation.textFieldNotEmpty(tfcomments,commentsLabel,"Please provide your comments!");
		boolean deal_ID = FormValidation.textFieldNotEmpty(tfdeal_ID,deal_IDLabel,"Deal ID is required!");
		boolean customer_ID = FormValidation.textFieldNotEmpty(tfcustomer_ID,customer_IDLabel,"Please provide your customer ID!");
		
		
	if(rating && comments && deal_ID && customer_ID) {
			
			// add the data any time and the will be updated
			Review entry = new Review(conn,null,tfrating, tfcomments, tfdeal_ID, tfcustomer_ID);
			if(!entry.isInDatabase) return;
			// insert data in table
			data.add(entry);
			
			// clear TextFields
			clearForm();
			
		}
		
		
		
	}
	
	public void displayReview() {
		AppUtil.console("Displaying reviews..");
		ArrayList<Review> rl =
				Review.retrieve(conn, tfreview_ID, tfrating, tfcomments, tfdeal_ID, tfcustomer_ID);
		try {
			data.clear();
		}
		catch(Exception e) {
			data.clear();
		}
		for(Review r : rl) {
			data.add(r);
		}
		// clear TextFields
		clearForm();
	}
	
	public void onSearchItem(ActionEvent event) {
		displayReview();
	}
	

	
	public void onUpdateItem(ActionEvent event) {
		AppUtil.console("Updating a review.");
		int i = index.get();
		Review oldReview = data.get(i);
		Review newReview = new Review(conn,tfreview_ID,tfrating, tfcomments, tfdeal_ID, tfcustomer_ID);
		if(Review.update(oldReview,newReview)) {
			data.set(i, newReview);
		}
		
		AppUtil.console("INDEX: "+i);
	}
	
	
	public void onDeleteItem(ActionEvent event) {
		AppUtil.console("Attempting to delete 1 item");
		int i = index.get();
		if(i <=-1 || i>=data.size()) return;
		Review oldReview = data.get(i);
		if(Review.delete(oldReview)) {
			data.remove(i);
			tvReview.getSelectionModel().select(i);
			tvReview.getSelectionModel().clearSelection();
		}
		
	}



	private void clearForm() {
		tfreview_ID.clear();
		tfrating.clear();
		tfcomments.clear();
		tfdeal_ID.clear();
		tfcustomer_ID.clear();
		
		tfreview_ID.setText(null);
		tfrating.setText(null);
		tfcomments.setText(null);
		tfdeal_ID.setText(null);
		tfcustomer_ID.setText(null);
		
		ratingLabel.setText(null);
		commentsLabel.setText(null);
		deal_IDLabel.setText(null);
		customer_IDLabel.setText(null);
		
		tfrating.getStyleClass().remove("error");
		tfcomments.getStyleClass().remove("error");
		tfdeal_ID.getStyleClass().remove("error");
		tfcustomer_ID.getStyleClass().remove("error");
	}
	
	public void onClearAction (ActionEvent event) {
		
		clearForm();
	}
	
	
}



