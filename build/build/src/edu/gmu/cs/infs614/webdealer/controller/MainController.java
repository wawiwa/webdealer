package edu.gmu.cs.infs614.webdealer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.URL;
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
import edu.gmu.cs.infs614.webdealer.model.FormValidation;
import edu.gmu.cs.infs614.webdealer.model.Schema;
import edu.gmu.cs.infs614.webdealer.model.Table;
import edu.gmu.cs.infs614.webdealer.model.connector.SchemaConnection;

public class MainController implements Initializable {

	
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
	TableView<Table> tableID;
	@FXML
	TableColumn<Table, Integer> tID;
	@FXML
	TableColumn<Table, String> tName;
	@FXML
	TableColumn<Table, String> tDate;
	@FXML
	TableColumn<Table, Float> tPrice;
	
	// DEFINE FORM
	@FXML
	TextField nameInput;
	@FXML
	TextField dateInput;
	@FXML
	TextField priceInput;
	@FXML
	Button submit;
	@FXML
	Button delete;
	@FXML
	Button clear;
	@FXML
	Label nameLabel;
	@FXML
	Label dateLabel;
	@FXML
	Label priceLabel;
	
	// DEFINE VARIABLES
	
	private int tNumber = 1;
	// index for delete item
	private IntegerProperty index = new SimpleIntegerProperty();
	

	
	// CREATE TABLE DATA
	
	final ObservableList<Table> data = FXCollections.observableArrayList(
			new Table(tNumber++,"Name 1","10/10/13",(float)100),
			new Table(tNumber++,"Name 2","10/11/13",(float)110),
			new Table(tNumber++,"Name 3","10/12/13",(float)123)
			);
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO Auto-generated method stub
		

		
		Schema schema = new Schema(new SchemaConnection("wward5","password").getConnection());
		schema.loadSchema();
        
		new Thread() { 
			@SuppressWarnings("resource")
			public void run() {
				BufferedReader reader = null;
				try {
					PipedOutputStream pOut = new PipedOutputStream();
					System.setOut(new PrintStream(pOut));
					PipedInputStream pIn;
					pIn = new PipedInputStream(pOut);
					reader = new BufferedReader(new InputStreamReader(pIn));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while(true) {
				    try {
				        String line = reader.readLine();
				        fxConsoleTextArea.appendText(line+"\n");
				        if(line != null) {
				            // Write line to component
				        }
				    } catch (IOException ex) {
				        // Handle ex
				    }
				}
			}
		}.start();

		
		// fix delete button working if not selecting once a table row
		index.set(-1);
		
		tID.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rID"));
		tName.setCellValueFactory(new PropertyValueFactory<Table, String>("rName"));
		tDate.setCellValueFactory(new PropertyValueFactory<Table, String>("rDate"));
		tPrice.setCellValueFactory(new PropertyValueFactory<Table, Float>("rPrice"));
		
		tableID.setItems(data);
		
		// get the index when clicking on table row
		tableID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
				index.set(data.indexOf(newValue));
				System.out.println("OK index is: "+data.indexOf(newValue));
			}
		});
	}

	public void onAddItem(ActionEvent event) {
		
		// FORM VALIDATION
		boolean name = FormValidation.textFieldNotEmpty(nameInput,nameLabel,"Name is required!");
		boolean date = FormValidation.textFieldTypeDate(dateInput,dateLabel,"Date has to be of type mm/yy or dd/mm/yyyy.");
		boolean price = FormValidation.textFieldTypeNumber(priceInput,priceLabel,"Price has to be a numerical value.");
		
		
		if(name && date && price) {
			
			// add the data any time and the will be updated
			Table entry = new Table(tNumber,nameInput.getText(),dateInput.getText(),Float.parseFloat(priceInput.getText()));
			tNumber++;
			
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
			tableID.getSelectionModel().clearSelection();
		}
		
	}

	private void clearForm() {
		// TODO Auto-generated method stub
		nameInput.clear();
		dateInput.clear();
		priceInput.clear();
		
		nameLabel.setText(null);
		dateLabel.setText(null);
		priceLabel.setText(null);
		
		nameInput.getStyleClass().remove("error");
		dateInput.getStyleClass().remove("error");
		priceInput.getStyleClass().remove("error");
		
	}
	
	public void onClearAction (ActionEvent event) {
		
		clearForm();
	}
	
	// DISPLAY CUSTOMER TABLE
	
	public void displayCustomerDialogue(ActionEvent event) {
	   System.out.println("Showing Customer dialogue.");
	   try {
		System.out.println(fxScrollPane.idProperty());
		String cv = "/edu/gmu/cs/infs614/webdealer/view/CustomerView.fxml";
		fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(cv)));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
}
