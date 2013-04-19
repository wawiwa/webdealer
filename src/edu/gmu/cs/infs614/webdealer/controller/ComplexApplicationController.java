package edu.gmu.cs.infs614.webdealer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.SynchronousQueue;

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
import edu.gmu.cs.infs614.webdealer.model.Schema;
import edu.gmu.cs.infs614.webdealer.model.Table;
import edu.gmu.cs.infs614.webdealer.model.connector.SchemaConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;

public class ComplexApplicationController implements Initializable {

	
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
	
	@FXML
	Button fxMerchantButton;
	
	@FXML
	Button fxDealButton;
	
	@FXML
	Button fxShoppingCartButton;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO Auto-generated method stub
		customerDialogue();
		
		Schema schema = new Schema(new SchemaConnection("wward5","password").getConnection());
		if(!schema.isSchemaLoaded()) {
			schema.loadSchema();
		}
        
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
	
	    try {
			new Thread() { 
				public void run() {
					consolePrinter(fxConsoleTextArea);
				}
			}.start();
	    }catch (Exception e) {
	    	new Thread() { 
				public void run() {
					consolePrinter(fxConsoleTextArea);
				}
			}.start();
	    }
		

	}

	@SuppressWarnings("resource")
	public void consolePrinter (TextArea ta) {
		synchronized (this) {
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
			    	
			    	
			        String line = reader.readLine()+"\n";

			        ta.appendText(line);
			        
			        

			    } catch (IOException ex) {
			        // Handle ex
			    }
			}
		}
	}

	
	// DISPLAY CUSTOMER TABLE
	
	public void displayCustomerDialogue(ActionEvent event) {
	   System.out.println("Showing Customer dialogue.");
	   customerDialogue();
	}
	
	public void customerDialogue() {
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
