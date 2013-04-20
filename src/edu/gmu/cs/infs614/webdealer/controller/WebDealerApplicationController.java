package edu.gmu.cs.infs614.webdealer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.model.Schema;
import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.SchemaConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.TestConnection;

public class WebDealerApplicationController implements Initializable {

	
	// COMPLEX APPLICATION SEGMENTS
	@FXML
	ScrollPane fxScrollPane;
	
	@FXML
	AnchorPane fxAnchorPane;
	

	
	// MAIN VIEWS
	@FXML
	Button fxCustomerButton;
	
	@FXML
	Button fxMerchantButton;
	
	@FXML
	Button fxDealButton;
	
	@FXML
	Button fxShoppingCartButton;
	
	@FXML
	Button fxClearConsole;
	
	@FXML
	Button fxConnectButton;
	
	@FXML
	public static TextArea fxConsoleTextArea;
	
	@FXML
	public static TextField fxUsernameTextField;
	
	@FXML
	public static PasswordField fxPasswordField;

	
	private TestConnection tc;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO Auto-generated method stub
		
		displayStartSplash();
		fxPasswordField.setOnKeyPressed(new EventHandler<KeyEvent>()
			    {
			        @Override
			        public void handle(KeyEvent ke)
			        {
			            if (ke.getCode().equals(KeyCode.ENTER))
			            {
			                connectToDatabase();
			            }
			        }
			    });
		
		//customerDialogue();
//	
//	    try {
//			new Thread() { 
//				public void run() {
//					consolePrinter(fxConsoleTextArea);
//				}
//			}.start();
//	    }catch (Exception e) {
//	    	System.out.println(e);
//	    }
		

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

	// CLEAR Console
	
	public void clearConsole (ActionEvent event) {
		// clear didn't work
		fxConsoleTextArea.setText("");
	}
	
	// ACCEPT PASSWORD
	
	
	public void onConnect(ActionEvent event) {
		connectToDatabase();
	}
	
	private void connectToDatabase() {
		
		OracleConnection.user = fxUsernameTextField.getText();
		OracleConnection.pass = fxPasswordField.getText();
		
		AppUtil.console("Attempting to connect...");
		TestConnection tc = new TestConnection(OracleConnection.user,OracleConnection.pass);
		
		if (!tc.isConnected) {
			fxConnectButton.setText("Incorrect!");
			fxConnectButton.setTextFill(Color.rgb(210, 39, 30));
        } else {
        	fxConnectButton.setText("Confirmed!");
        	fxConnectButton.setTextFill(Color.rgb(21, 117, 84));
        	customerDialogue();
        }
		fxPasswordField.clear();
        
		
	}
	
	
	
	// DISPLAY SPLASH LOGIN
	
	public void displayStartSplash() {
		   try {
			   System.out.println("Start splash.");
			   
			    //AppUtil.console(fxScrollPane.idProperty().toString());
				String ss = "/edu/gmu/cs/infs614/webdealer/view/Splash.fxml";
				fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(ss)));
		   } catch (IOException e) {
				   // TODO Auto-generated catch block
			   System.out.println(e.toString());
		}
	}
	
	// DISPLAY CUSTOMER TABLE
	
	public void displayCustomerDialogue(ActionEvent event) {
	   AppUtil.console("Showing Customer dialogue.");
	   customerDialogue();
	}
	
	private void customerDialogue() {
		   try {
			    AppUtil.console(fxScrollPane.idProperty().toString());
				String cv = "/edu/gmu/cs/infs614/webdealer/view/CustomerView.fxml";
				fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(cv)));
		   } catch (IOException e) {
				   // TODO Auto-generated catch block
			   AppUtil.console(e.toString());
		}
	}
	
}
