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
import edu.gmu.cs.infs614.webdealer.model.Merchant;
import edu.gmu.cs.infs614.webdealer.model.connector.MerchantConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

public class MerchantController implements Initializable {

	
	// COMPLEX APPLICATION SEGMENTS
	@FXML
	ScrollPane fxScrollPane;
	
	@FXML
	AnchorPane fxAnchorPane;
	
	@FXML
	TextArea fxConsoleTextArea;
	
	// MAIN VIEWS
	@FXML
	Button fxMerchantButton;
	
	// DEFINE TABLE
	
	@FXML
	TableView<Merchant> tvMerchant;
	@FXML
	TableColumn<Merchant, Integer> tcMerchant_ID;
	@FXML
	TableColumn<Merchant, String> tcMerchantName;

	
	// DEFINE FORM
	@FXML
	TextField tfMerchant_ID;
	@FXML
	TextField tfMerchantName;
	
	@FXML
	Button submit;
	@FXML
	Button delete;
	@FXML
	Button clear;
	@FXML
	Button search;
	
	
	@FXML
	Label merchantNameLabel;
	
//	@FXML
//	Label nameInput;
//	
//	@FXML
//	Label nameLabel;
	
	// DEFINE VARIABLES
	
	// index for delete item
	private IntegerProperty index = new SimpleIntegerProperty();
	
	// DB connector
	public Connection conn = new MerchantConnection("wward5","password").getConnection();
	
	
	final ObservableList<Merchant> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		
		displayMerchants();
		
		// fix delete button working if not selecting once a table row
		index.set(-1);
		
		tcMerchant_ID.setCellValueFactory(new PropertyValueFactory<Merchant, Integer>("mID"));
		tcMerchantName.setCellValueFactory(new PropertyValueFactory<Merchant, String>("mName"));
		

        AppUtil.console("DATA contents: "+data);

		tvMerchant.setItems(data);
		
		// get the index when clicking on table row
		tvMerchant.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
				if(newValue==null)return;
				Merchant m = (Merchant) newValue;
				
				tfMerchant_ID.setText(m.getMID().toString()); 
				tfMerchantName.setText(m.getMName());
				
				
				index.set(data.indexOf(newValue));
				AppUtil.console("OK index is: "+data.indexOf(newValue));
				AppUtil.console(m.getMID().toString());
			}
		});
	}

	public void onAddItem(ActionEvent event) {
		
		// FORM VALIDATION
		boolean merchantName = FormValidation.textFieldNotEmpty(tfMerchantName,merchantNameLabel,"Merchant Name is required!");
		
		
		
		
		if(merchantName) {
			
			// add the data any time and the will be updated
			Merchant entry = new Merchant(conn,null,tfMerchantName);
			if(!entry.isInDatabase) return;
			// insert data in table
			data.add(entry);
			
			// clear TextFields
			clearForm();
			
		}
		
		
		
	}
	
	public void displayMerchants() {
		AppUtil.console("Displaying merchants..");
		ArrayList<Merchant> ml =
				Merchant.retrieve(conn, tfMerchant_ID, tfMerchantName);
		try {
			data.clear();
		}
		catch(Exception e) {
			data.clear();
		}
		for(Merchant m : ml) {
			data.add(m);
		}
		// clear TextFields
		clearForm();
	}
	
	public void onSearchItem(ActionEvent event) {
		displayMerchants();
	}
	

	
	public void onUpdateItem(ActionEvent event) {
		AppUtil.console("Updating a merchant.");
		int i = index.get();
		Merchant oldMerchant = data.get(i);
		Merchant newMerchant = new Merchant(conn,tfMerchant_ID,tfMerchantName);
		if(Merchant.update(oldMerchant,newMerchant)) {
			data.set(i, newMerchant);
		}
		
		AppUtil.console("INDEX: "+i);
	}
	
	
	public void onDeleteItem(ActionEvent event) {
		AppUtil.console("Attempting to delete 1 item");
		int i = index.get();
		if(i <=-1 || i>=data.size()) return;
		Merchant oldMerchant = data.get(i);
		if(Merchant.delete(oldMerchant)) {
			data.remove(i);
			tvMerchant.getSelectionModel().select(i);
			tvMerchant.getSelectionModel().clearSelection();
		}
		
	}



	private void clearForm() {
		tfMerchant_ID.clear();
		tfMerchantName.clear();
		
		tfMerchant_ID.setText(null);
		tfMerchantName.setText(null);
		
		merchantNameLabel.setText(null);
		
		tfMerchantName.getStyleClass().remove("error");

	}
	
	public void onClearAction (ActionEvent event) {
		
		clearForm();
	}
	
	
}
