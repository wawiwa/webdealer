package edu.gmu.cs.infs614.webdealer.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import edu.gmu.cs.infs614.webdealer.model.Customer;
import edu.gmu.cs.infs614.webdealer.model.connector.CustomerConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

public class CustomerController implements Initializable {

	
	// COMPLEX APPLICATION SEGMENTS
	@FXML
	ScrollPane fxScrollPane;
	
	@FXML
	AnchorPane fxAnchorPane;
	
	@FXML
	TextArea fxConsoleTextArea;
	
	// MAIN VIEWS
	@FXML
	Button fxCustomerButton;
	
	// DEFINE TABLE
	
	@FXML
	TableView<Customer> tvCustomer;
	@FXML
	TableColumn<Customer, Integer> tcCustomerID;
	@FXML
	TableColumn<Customer, String> tcFirstName;
	@FXML
	TableColumn<Customer, String> tcLastName;
	@FXML
	TableColumn<Customer, Integer> tcAge;
	@FXML
	TableColumn<Customer, String> tcEmailAddress;
	@FXML
	TableColumn<Customer, String> tcGender;
	
	// DEFINE FORM
	@FXML
	TextField tfCustomer_ID;
	@FXML
	TextField tfFirstName;
	@FXML
	TextField tfLastName;
	@FXML
	TextField tfAge;
	@FXML
	TextField tfEmailAddress;
	@FXML
	TextField tfGender;
	
	
	@FXML
	Button submit;
	@FXML
	Button delete;
	@FXML
	Button clear;
	@FXML
	Button search;
	
	
	@FXML
	Label firstNameLabel;
	@FXML
	Label lastNameLabel;
	@FXML
	Label ageLabel;
	@FXML
	Label emailAddressLabel;
	@FXML
	Label genderLabel;
	
	// DEFINE VARIABLES
	
	// index for delete item
	private IntegerProperty index = new SimpleIntegerProperty();
	
	// DB connector
	public Connection conn = new CustomerConnection("wward5","password").getConnection();
	
	// CREATE TABLE DATA
	
//	final ObservableList<Customer> data = FXCollections.observableArrayList(
//			new Customer(conn,1,"John","Doe",20,"jdoe@gmu.edu","m"),
//			new Customer(conn,2,"Paul","Smith",30,"psmith@gmu.edu","m"),
//			new Customer(conn,3,"Mary","Contrary",40,"mcontrary@gmu.edu","f")
//			);
	final ObservableList<Customer> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		displayCustomers();
		
		// fix delete button working if not selecting once a table row
		index.set(-1);
		
		tcCustomerID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("cID"));
		tcFirstName.setCellValueFactory(new PropertyValueFactory<Customer, String>("cFirstName"));
		tcLastName.setCellValueFactory(new PropertyValueFactory<Customer, String>("cLastName"));
		tcAge.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("cAge"));
		tcEmailAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("cEmailAddress"));
		tcGender.setCellValueFactory(new PropertyValueFactory<Customer, String>("cGender"));
		
		System.out.println("DATA contents: " + data);
		tvCustomer.setItems(data);
		
		// get the index when clicking on table row
		tvCustomer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
				Customer c = (Customer) newValue;
				
				tfCustomer_ID.setText(c.getCID().toString()); 
				tfFirstName.setText(c.getCFirstName());
				tfLastName.setText(c.getCLastName());
				tfAge.setText(c.getCAge().toString());
				tfEmailAddress.setText(c.getCEmailAddress());
				tfGender.setText(c.getCGender());
				
				
				index.set(data.indexOf(newValue));
				System.out.println("OK index is: "+data.indexOf(newValue));
				System.out.println(c.getCID());
			}
		});
	}

	public void onAddItem(ActionEvent event) {
		
		// FORM VALIDATION
		boolean firstName = FormValidation.textFieldNotEmpty(tfFirstName,firstNameLabel,"First Name is required!");
		boolean lastName = FormValidation.textFieldNotEmpty(tfLastName,lastNameLabel,"Last Name is required!");
		boolean age = FormValidation.textFieldTypeInteger(tfAge,ageLabel,"Age must be a number.");
		boolean email = FormValidation.textFieldNotEmpty(tfEmailAddress,emailAddressLabel,"Must have a valid email address.");
		boolean gender = FormValidation.textFieldNotEmpty(tfGender,genderLabel,"Gender is required!");
		
		
		
		
		if(firstName && lastName && age && email && gender) {
			
			// add the data any time and the will be updated
			Customer entry = new Customer(conn,null,tfFirstName.getText(),tfLastName.getText(),
					Integer.parseInt(tfAge.getText()),tfEmailAddress.getText(),tfGender.getText());
			
			// insert data in table
			data.add(entry);
			
			// clear TextFields
			clearForm();
			
		}
		
		
		
	}
	
	public void displayCustomers() {
		ArrayList<Customer> cl =
				Customer.retrieve(conn, tfCustomer_ID, tfFirstName, tfLastName, tfAge, tfEmailAddress, tfGender);
		data.clear();
		for(Customer c : cl) {
			data.add(c);
		}
		// clear TextFields
		clearForm();
	}
	
	public void onSearchItem(ActionEvent event) {
		
		ArrayList<Customer> cl =
				Customer.retrieve(conn, tfCustomer_ID, tfFirstName, tfLastName, tfAge, tfEmailAddress, tfGender);
		data.clear();
		for(Customer c : cl) {
			data.add(c);
		}
		// clear TextFields
		clearForm();
	}
	
	
	
	public void onDeleteItem(ActionEvent event) {
		System.out.println("Deleted 1 item");
		int i = index.get();
		if(i > -1) {
			data.remove(i);
			tvCustomer.getSelectionModel().clearSelection();
		}
		
	}
	




	private void clearForm() {
		// TODO Auto-generated method stub
		tfCustomer_ID.clear();
		tfFirstName.clear();
		tfLastName.clear();
		tfAge.clear();
		tfEmailAddress.clear();
		tfGender.clear();
		
		tfCustomer_ID.setText(null);
		tfFirstName.setText(null);
		tfLastName.setText(null);
		tfAge.setText(null);
		tfEmailAddress.setText(null);
		tfGender.setText(null);
		
		firstNameLabel.setText(null);
		lastNameLabel.setText(null);
		ageLabel.setText(null);
		emailAddressLabel.setText(null);
		genderLabel.setText(null);
		
		tfFirstName.getStyleClass().remove("error");
		tfLastName.getStyleClass().remove("error");
		tfAge.getStyleClass().remove("error");
		tfEmailAddress.getStyleClass().remove("error");
		tfGender.getStyleClass().remove("error");
		
	}
	
	public void onClearAction (ActionEvent event) {
		
		clearForm();
	}
	
	
}
