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
import edu.gmu.cs.infs614.webdealer.model.Customer;
import edu.gmu.cs.infs614.webdealer.model.connector.CustomerConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

public class SplashController implements Initializable {

	
	// COMPLEX APPLICATION SEGMENTS
	@FXML
	AnchorPane fxSplashAnchor;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
	}

	
}
