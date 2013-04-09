package edu.gmu.cs.infs614.webdealer.controller;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gmu.cs.infs614.webdealer.model.Table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController implements Initializable {

	// DEFINE TABLE
	
	@FXML
	TableView<Table> tableID;
	@FXML
	TableColumn<Table, Integer> iID;
	@FXML
	TableColumn<Table, String> iName;
	@FXML
	TableColumn<Table, String> iDate;
	@FXML
	TableColumn<Table, Integer> iPrice;
	
	// DEFINE VARIABLES
	
	private int iNumber = 1;
	
	// CREATE TABLE DATA
	
	final ObservableList<Table> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		iID.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rID"));
		iName.setCellValueFactory(new PropertyValueFactory<Table, String>("rName"));
		iDate.setCellValueFactory(new PropertyValueFactory<Table, String>("rDate"));
		iPrice.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rPrice"));
		
		tableID.setItems(data);
	}

}
