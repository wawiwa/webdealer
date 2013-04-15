package edu.gmu.cs.infs614.webdealer.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import edu.gmu.cs.infs614.webdealer.model.Customer;
import edu.gmu.cs.infs614.webdealer.model.CustomerConnection;
import edu.gmu.cs.infs614.webdealer.model.FormValidation;
import edu.gmu.cs.infs614.webdealer.model.Schema;

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
	
	private int tNumber = 1;
	// index for delete item
	private IntegerProperty index = new SimpleIntegerProperty();
	
	
	
	// CREATE TABLE DATA
	
	final ObservableList<Customer> data = FXCollections.observableArrayList(
			new Customer("John","Doe",20,"jdoe@gmu.edu","m"),
			new Customer("Paul","Smith",30,"psmith@gmu.edu","m"),
			new Customer("Mary","Contrary",40,"mcontrary@gmu.edu","f")
			);
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

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
				index.set(data.indexOf(newValue));
				System.out.println("OK index is: "+data.indexOf(newValue));
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
			Customer entry = new Customer(tfFirstName.getText(),tfLastName.getText(),
					Integer.parseInt(tfAge.getText()),tfEmailAddress.getText(),tfGender.getText());
			
			// insert data in table
			data.add(entry);
			
			// clear TextFields
			clearForm();
			
		}
		
		
		
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
		tfFirstName.clear();
		tfLastName.clear();
		tfAge.clear();
		tfEmailAddress.clear();
		tfGender.clear();
		
		tfFirstName.setText(null);
		tfLastName.setText(null);
		tfAge.setText(null);
		tfEmailAddress.setText(null);
		tfGender.setText(null);
		
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
