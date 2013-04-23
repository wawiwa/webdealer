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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.model.Customer;
import edu.gmu.cs.infs614.webdealer.model.PaymentMethod;
import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.PaymentMethodConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

public class PaymentMethodController implements Initializable {

	// MAIN VIEW SEGMENT

	
	// DEFINE TABLE
	
	@FXML
	TableView<PaymentMethod> fxtvPaymentMethod;
	@FXML
	TableColumn<PaymentMethod, Integer> fxtcPaymentID;
	@FXML
	TableColumn<PaymentMethod, String> fxtcVendorName;
	@FXML
	TableColumn<PaymentMethod, String> fxtcCardNumber;
	@FXML
	TableColumn<PaymentMethod, Integer> fxtcPrimary;

	
	// DEFINE FORM
	@FXML
	TextField fxtfPaymentID;
	@FXML
	TextField fxtfVendorName;
	@FXML
	TextField fxtfCardNumber;
	
	@FXML
	Label fxlCustomerInfo;
	@FXML
	Label fxlVendorName;
	@FXML
	Label fxlCardNumber;
	
	
	@FXML
	RadioButton fxrbPrimary;
	@FXML
	Button submit;
	@FXML
	Button delete;
	@FXML
	Button clear;
	@FXML
	Button search;
	
	
	// DEFINE VARIABLES
	
	// index for delete item
	private IntegerProperty index = new SimpleIntegerProperty();
	
	// DB connector
	
	public Connection conn = new PaymentMethodConnection(OracleConnection.user,OracleConnection.pass).getConnection();
	
	final ObservableList<PaymentMethod> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		
		displayCards();
		
		// fix delete button working if not selecting once a table row
		index.set(-1);
		
		fxtcPaymentID.setCellValueFactory(new PropertyValueFactory<PaymentMethod, Integer>("pmPaymentID"));
		fxtcVendorName.setCellValueFactory(new PropertyValueFactory<PaymentMethod, String>("pmVendorName"));
		fxtcCardNumber.setCellValueFactory(new PropertyValueFactory<PaymentMethod, String>("pmCardNumber"));
		fxtcPrimary.setCellValueFactory(new PropertyValueFactory<PaymentMethod, Integer>("pmPrimary"));

		

        AppUtil.console("DATA contents: "+data);

		fxtvPaymentMethod.setItems(data);
		
		// get the index when clicking on table row
		fxtvPaymentMethod.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
				if(newValue==null)return;
				PaymentMethod pm = (PaymentMethod) newValue;
				
				
				fxtfPaymentID.setText(pm.getPmPaymentID().toString()); 
				fxtfVendorName.setText(pm.getPmVendorName());
				fxtfCardNumber.setText(pm.getPmCardNumber());
				
				
				index.set(data.indexOf(newValue));
				AppUtil.console("OK index is: "+data.indexOf(newValue));
				AppUtil.console(pm.getPmPaymentID().toString());
			}
		});
	}

	public void onAddItem(ActionEvent event) {
		
		// FORM VALIDATION
		boolean vendorName = FormValidation.textFieldNotEmpty(fxtfVendorName,fxlVendorName,"Vendor name is required!");
		boolean cardNumber = FormValidation.textFieldNotEmpty(fxtfCardNumber,fxlCardNumber,"Card number is required!");
		
		if(vendorName && cardNumber) {
			
			Integer selected = 0;
			if(fxrbPrimary.isSelected()) {
				selected = 1;
			}
			
			// add the data any time and the will be updated
			PaymentMethod entry = new PaymentMethod(conn,null,fxtfVendorName,
					fxtfCardNumber,selected,
					CustomerController.getCurrentCustomerID());
			if(!entry.isInDatabase) return;
			// insert data in table
			data.add(entry);
			
			// clear TextFields
			clearForm();
			
		}
		
		
		
	}
	
	public void displayCards() {
		AppUtil.console("Display cards..");
		fxlCustomerInfo.setText(Customer.retrieve(CustomerController.getCurrentCustomerID()));
		ArrayList<PaymentMethod> pml =
				PaymentMethod.retrieve(conn, CustomerController.getCurrentCustomerID());
		try {
			data.clear();
		}
		catch(Exception e) {
			data.clear();
		}
		for(PaymentMethod pm : pml) {
			AppUtil.console("Adding cards to view..");
			data.add(pm);
		}
		// clear TextFields
		clearForm();
	}

	
	public void onUpdateItem(ActionEvent event) {
		AppUtil.console("Updating card.");
		int i = index.get();
		PaymentMethod oldPayment = data.get(i);
		int selected = 0;
		if(fxrbPrimary.isSelected()) {
			selected = 1;
		}
		PaymentMethod newPayment = new PaymentMethod(conn,fxtfPaymentID,fxtfVendorName,fxtfCardNumber,
				selected,CustomerController.getCurrentCustomerID());
		if(PaymentMethod.update(oldPayment,newPayment)) {
			data.set(i, newPayment);
		}
		
		AppUtil.console("INDEX: "+i);
	}
	
	
	public void onDeleteItem(ActionEvent event) {
		AppUtil.console("Attempting to delete 1 item");
		int i = index.get();
		if(i <=-1 || i>=data.size()) return;
		PaymentMethod oldPayment = data.get(i);
		if(PaymentMethod.delete(oldPayment)) {
			data.remove(i);
			fxtvPaymentMethod.getSelectionModel().select(i);
			fxtvPaymentMethod.getSelectionModel().clearSelection();
		}
		
	}



	private void clearForm() {

		fxtfCardNumber.clear();
		fxtfVendorName.clear();
		fxtfPaymentID.clear();
		
		fxtfCardNumber.setText(null);
		fxtfVendorName.setText(null);
		fxtfPaymentID.setText(null);
		
		fxlCardNumber.setText(null);
		fxlVendorName.setText(null);
		
		fxtfCardNumber.getStyleClass().remove("error");
		fxtfVendorName.getStyleClass().remove("error");
		fxtfPaymentID.getStyleClass().remove("error");
		
	}
	
	public void onClearAction (ActionEvent event) {
		
		clearForm();
	}
	
	
}
