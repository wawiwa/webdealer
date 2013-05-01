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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.model.Location;
import edu.gmu.cs.infs614.webdealer.model.connector.LocationConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;


public class LocationController implements Initializable {
	// COMPLEX APPLICATION SEGMENTS
		@FXML
		ScrollPane fxScrollPane;
		
		@FXML
		AnchorPane fxAnchorPane;
		
		@FXML
		TextArea fxConsoleTextArea;
		
		// MAIN VIEWS
		@FXML
		Button fxLocationButton;
		
		// DEFINE TABLE
		
		@FXML
		TableView<Location> tvLocation;
		@FXML
		TableColumn<Location, String> tcLocationCity;
		@FXML
		TableColumn<Location, String> tcLocationState;
		@FXML
		TableColumn<Location, String> tcLocationCountry;
		@FXML
		TableColumn<Location, String> tcLocationContinent;
		@FXML
		TableColumn<Location, Integer> tcLocation_ID;

		
		// DEFINE FORM
		@FXML
		TextField tflocation_ID;
		@FXML
		TextField tfcity;
		@FXML
		TextField tfstate;
		@FXML
		TextField tfcountry;
		@FXML
		TextField tfcontinent;
		
		@FXML
		Button submit;
		@FXML
		Button delete;
		@FXML
		Button clear;
		@FXML
		Button search;
		
		
		
		@FXML
		Label locationCityLabel;
		@FXML
		Label locationStateLabel;
		@FXML
		Label locationCountryLabel;
		@FXML
		Label locationContinentLabel;
		

		

		
		// index for delete item
		private IntegerProperty index = new SimpleIntegerProperty();
		
		// DB connector
		public Connection conn = new LocationConnection(OracleConnection.user,OracleConnection.pass).getConnection();
		
		
		final ObservableList<Location> data = FXCollections.observableArrayList();
		
		@Override
		public void initialize(URL location, ResourceBundle resources) {

			
			
			displayLocation();
			
			// fix delete button working if not selecting once a table row
			index.set(-1);
			
			
			
			tcLocation_ID.setCellValueFactory(new PropertyValueFactory<Location, Integer>("lID"));
			tcLocationCity.setCellValueFactory(new PropertyValueFactory<Location, String>("lCity"));
			tcLocationState.setCellValueFactory(new PropertyValueFactory<Location, String>("lState"));
			tcLocationCountry.setCellValueFactory(new PropertyValueFactory<Location, String>("lCountry"));
			tcLocationContinent.setCellValueFactory(new PropertyValueFactory<Location, String>("lContinent"));


	        AppUtil.console("DATA contents: "+data);

			tvLocation.setItems(data);
			
			// get the index when clicking on table row
			tvLocation.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
				public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
					if(newValue==null)return;
					Location l = (Location) newValue;
					
					tflocation_ID.setText(l.getLID().toString()); 
					tfcity.setText(l.getLCity());
					tfstate.setText(l.getLState());
					tfcountry.setText(l.getLCountry());
					tfcontinent.setText(l.getLContinent());
					
					
					
				
					AppUtil.console(l.getLID().toString());
				}
			});
		}

		public void onAddItem(ActionEvent event) {
			
			// FORM VALIDATION
			boolean city = FormValidation.textFieldNotEmpty(tfcity,locationCityLabel,"City is required!");
			boolean state = FormValidation.textFieldNotEmpty(tfstate,locationStateLabel,"State is required!");
			boolean country = FormValidation.textFieldNotEmpty(tfcountry,locationCountryLabel,"Country is required!");
			boolean continent = FormValidation.textFieldNotEmpty(tfcontinent,locationContinentLabel,"Continent is required!");
			
			if(city && state && country && continent) {
				
				// add the data any time and the will be updated
				Location entry = new Location(conn,null,tfcity,tfstate,tfcountry,tfcontinent);
				if(!entry.isInDatabase) return;
				// insert data in table
				data.add(entry);
				
				// clear TextFields
				clearForm();
				
			}
			
			
				
			
			
		}
		
		public void displayLocation() {
			AppUtil.console("Displaying locations..");
			ArrayList<Location> ll =
					Location.retrieve(conn, tflocation_ID, tfcity, tfstate, tfcountry, tfcontinent);
			try {
				data.clear();
			}
			catch(Exception e) {
				data.clear();
			}
			for(Location l : ll) {
				data.add(l);
			}
			// clear TextFields
			clearForm();
		}
		
		public void onSearchItem(ActionEvent event) {
			displayLocation();
		}
		

		
		public void onUpdateItem(ActionEvent event) {
			AppUtil.console("Updating a location.");
			int i = index.get();
			Location oldLocation = data.get(i);
			Location newLocation = new Location(conn,tflocation_ID,tfcity,tfstate,tfcountry,tfcontinent);
			if(Location.update(oldLocation,newLocation)) {
				data.set(i, newLocation);
			}
			
			AppUtil.console("INDEX: "+i);
		}
		
		
		public void onDeleteItem(ActionEvent event) {
			AppUtil.console("Attempting to delete 1 item");
			int i = index.get();
			if(i <=-1 || i>=data.size()) return;
			Location oldLocation = data.get(i);
			if(Location.delete(oldLocation)) {
				data.remove(i);
				tvLocation.getSelectionModel().select(i);
				tvLocation.getSelectionModel().clearSelection();
			}
			
		}

		// DISPLAY LOCATION TABLE

		public void displayLocationDialogue(ActionEvent event) {
			AppUtil.console("Showing Location dialogue.");
			locationDialogue();
		}

		private void locationDialogue() {
			try {
				AppUtil.console(fxScrollPane.idProperty().toString());
				String lv = "/edu/gmu/cs/infs614/webdealer/view/LocationView.fxml";
				WebDealerApplicationController.fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(lv)));
				WebDealerApplicationController.fxSplitPane.setDividerPosition(1, 0.7607);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				AppUtil.console(e.toString());
			}
		}

		private void clearForm() {
			tflocation_ID.clear();
			tfcity.clear();
			tfstate.clear();
			tfcountry.clear();
			tfcontinent.clear();
			
			tflocation_ID.setText(null);
			tfcity.setText(null);
			tfstate.setText(null);
			tfcountry.setText(null);
			tfcontinent.setText(null);
			
			
			
			tflocation_ID.getStyleClass().remove("error");
			tfcity.getStyleClass().remove("error");
			tfstate.getStyleClass().remove("error");
			tfcountry.getStyleClass().remove("error");
			tfcontinent.getStyleClass().remove("error");
		}
		
		public void onClearAction (ActionEvent event) {
			
			clearForm();
		}
		
		
	}
