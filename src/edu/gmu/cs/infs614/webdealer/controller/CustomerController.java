package edu.gmu.cs.infs614.webdealer.controller;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.model.Customer;
import edu.gmu.cs.infs614.webdealer.model.connector.CustomerConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

public class CustomerController implements Initializable {

	
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
	static
	TextField tfCustomer_ID;
	@FXML
	static
	TextField tfFirstName;
	@FXML
	static
	TextField tfLastName;
	@FXML
	static
	TextField tfAge;
	@FXML
	static
	TextField tfEmailAddress;
	@FXML
	static
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
	public static Connection conn = new CustomerConnection("wward5","password").getConnection();
	
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
		

        AppUtil.console("DATA contents: "+data);

		tvCustomer.setItems(data);
		
		// get the index when clicking on table row
		tvCustomer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
				if(newValue==null)return;
				Customer c = (Customer) newValue;
				
				tfCustomer_ID.setText(c.getCID().toString()); 
				tfFirstName.setText(c.getCFirstName());
				tfLastName.setText(c.getCLastName());
				tfAge.setText(c.getCAge().toString());
				tfEmailAddress.setText(c.getCEmailAddress());
				tfGender.setText(c.getCGender());
				
				
				index.set(data.indexOf(newValue));
				AppUtil.console("OK index is: "+data.indexOf(newValue));
				AppUtil.console(c.getCID().toString());
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
			Customer entry = new Customer(conn,null,tfFirstName,tfLastName,tfAge,
					tfEmailAddress,tfGender);
			if(!entry.isInDatabase) return;
			// insert data in table
			data.add(entry);
			
			// clear TextFields
			clearForm();
			
		}
		
		
		
	}
	
	public void displayCustomers() {
		AppUtil.console("Displaying customers..");
		ArrayList<Customer> cl =
				Customer.retrieve(conn, tfCustomer_ID, tfFirstName, tfLastName, tfAge, tfEmailAddress, tfGender);
		try {
			data.clear();
		}
		catch(Exception e) {
			data.clear();
		}
		for(Customer c : cl) {
			data.add(c);
		}
		// clear TextFields
		clearForm();
	}
	
	public void onSearchItem(ActionEvent event) {
		displayCustomers();
	}
	

	
	public void onUpdateItem(ActionEvent event) {
		AppUtil.console("Updating a customer.");
		int i = index.get();
		Customer oldCust = data.get(i);
		Customer newCust = new Customer(conn,tfCustomer_ID,tfFirstName,tfLastName,
				tfAge,tfEmailAddress,tfGender);
		if(Customer.update(oldCust,newCust)) {
			data.set(i, newCust);
		}
		
		AppUtil.console("INDEX: "+i);
	}
	
	
	public void onDeleteItem(ActionEvent event) {
		AppUtil.console("Attempting to delete 1 item");
		int i = index.get();
		if(i <=-1 || i>=data.size()) return;
		Customer oldCust = data.get(i);
		if(Customer.delete(oldCust)) {
			data.remove(i);
			tvCustomer.getSelectionModel().select(i);
			tvCustomer.getSelectionModel().clearSelection();
		}
		
	}
	
	public void onEditPaymentMethod(ActionEvent event) {
		AppUtil.console("Loading payment method..");
		displayPaymentMethod();
		
	}

	public void displayPaymentMethod() {
		try {
			String mv = "/edu/gmu/cs/infs614/webdealer/view/PaymentMethodView.fxml";
			WebDealerApplicationController.fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(mv)));
		} catch (IOException e) {
			AppUtil.console(e.toString());
		}
	}
		
	public static Integer getCurrentCustomerID() {
		AppUtil.console("Getting currently selected customer..");
		ArrayList<Customer> cl =
				Customer.retrieve(conn, tfCustomer_ID, tfFirstName, tfLastName, tfAge, tfEmailAddress, tfGender);
		
		// get first customer
		for(Customer c : cl) {
			if(c.getCID() == null) return -1;
			return c.getCID();
		}
		return -1;
	}


	private void clearForm() {
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
