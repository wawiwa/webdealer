package edu.gmu.cs.infs614.webdealer.controller;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gmu.cs.infs614.webdealer.model.Table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController implements Initializable {

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
	TableColumn<Table, Integer> tPrice;
	
	// DEFINE FORM
	@FXML
	TextField nameInput;
	@FXML
	TextField dateInput;
	@FXML
	TextField priceInput;
	@FXML
	Button submit;
	
	// DEFINE VARIABLES
	
	private int tNumber = 1;
	
	// CREATE TABLE DATA
	
	final ObservableList<Table> data = FXCollections.observableArrayList(
			new Table(tNumber++,"Name 1","10/10/13",100),
			new Table(tNumber++,"Name 2","10/11/13",110),
			new Table(tNumber++,"Name 3","10/12/13",123)
			);
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		tID.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rID"));
		tName.setCellValueFactory(new PropertyValueFactory<Table, String>("rName"));
		tDate.setCellValueFactory(new PropertyValueFactory<Table, String>("rDate"));
		tPrice.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rPrice"));
		
		tableID.setItems(data);
	}

	public void onAddItem(ActionEvent event) {
		
		// add the data any time and the will be updated
		Table entry = new Table(tNumber,nameInput.getText(),dateInput.getText(),Integer.parseInt(priceInput.getText()));
		tNumber++;
		
		// insert data in table
		data.add(entry);
		
		// clear TextFields
		clearForm();
	}

	private void clearForm() {
		// TODO Auto-generated method stub
		nameInput.clear();
		dateInput.clear();
		priceInput.clear();
	}
}
