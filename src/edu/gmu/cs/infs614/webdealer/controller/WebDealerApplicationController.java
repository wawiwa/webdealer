package edu.gmu.cs.infs614.webdealer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.WebDealerApplication;
import edu.gmu.cs.infs614.webdealer.controller.access.UserCreds;
import edu.gmu.cs.infs614.webdealer.model.Customer;
import edu.gmu.cs.infs614.webdealer.model.Schema;
import edu.gmu.cs.infs614.webdealer.model.connector.MainConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.SchemaConnection;

public class WebDealerApplicationController implements Initializable {


	// COMPLEX APPLICATION SEGMENTS
	@FXML
	public static ScrollPane fxScrollPane;

	@FXML
	AnchorPane fxAnchorPane;
	
	@FXML
	public static
	AnchorPane merchant;

	@FXML
	public
	static
	SplitPane fxSplitPane;

	@FXML
	static
	AnchorPane fxPreviousView;


	// MAIN VIEWS
	@FXML
	Button fxCustomerButton;

	@FXML
	Button fxMerchantButton;

	@FXML
	Button fxDealButton;

	@FXML
	Button fxPurchaseButton;

	@FXML
	Button fxClearConsole;

	@FXML
	Button fxConnectButton;

	@FXML
	Button fxReviewButton;
	
	@FXML
	Button fxbShowTableNames;
	
	@FXML
	Button fxLocationButton;

	// Open Query
	@FXML
	Button fxbOpenQuery;
	@FXML
	Button fxbOpenQuerySubmit;
	@FXML
	Button fxbOpenQueryClear;
	@FXML
	Button fxbOpenQueryClose;

	@FXML
	static
	TextField fxtfOpenQuery;




	@FXML
	public static TextArea fxConsoleTextArea;

	// USER INFO

	@FXML 
	public static TextField fxCustomerUsernameTextField;

	@FXML
	public static Button fxCustomerUsernameLoginButton;


	@FXML
	public static TextField fxUsernameTextField;

	@FXML
	public static PasswordField fxPasswordField;


	// vars

	public static UserCreds customerUsername = null;

	public static MainConnection oracleConn = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO Auto-generated method stub

		fxCustomerUsernameTextField.setText("portiz@gmu.edu");
		fxUsernameTextField.setText("wward5");
		fxPasswordField.setText("sqlplus");

		startSplash();

		fxCustomerUsernameTextField.setOnKeyPressed(new EventHandler<KeyEvent>()
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


		
		//initPasswordFieldEnterKey(fxPasswordField);
		//initTextFieldEnterKey(fxCustomerUsernameTextField);

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

	public void initTextFieldEnterKey(Object o) {
		TextField tf;
		if(o.getClass() == TextField.class.getClass()) {
			tf = (TextField) o;
			tf.setOnKeyPressed(new EventHandler<KeyEvent>()
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
		}
	}

	public void initPasswordFieldEnterKey(Object o) {
		PasswordField pf;
		if(o.getClass() == PasswordField.class.getClass()) {
			pf = (PasswordField) o;
			pf.setOnKeyPressed(new EventHandler<KeyEvent>()
					{
				//@Override
				public void handle(KeyEvent ke)
				{
					if (ke.getCode().equals(KeyCode.ENTER))
					{
						connectToDatabase();
					}
				}
					});
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

	// Customer login
	public void onCustomerUsernameLogin (ActionEvent event) {
		customerUsernameLogin();
	}

	public void customerUsernameLogin() {
		customerUsername = new UserCreds(fxCustomerUsernameTextField.getText());
		if((oracleConn != null && !oracleConn.isConnected) || oracleConn == null) {
			AppUtil.console("Need to login to the Oracle system.");
			startSplash();
			return;
		}

		boolean c = Customer.exists(oracleConn.getConnection(),new Integer(-1), UserCreds.getLogin());
		if (c) {
			dealDialogue();
			return;
		}
		else if (oracleConn.isConnected){
			customerUsernameLoginSplash();
			return;
		}
		//default for now
		startSplash();
	}

	public void customerUsernameLoginSplash() {
		try {
			AppUtil.console("Customer Username splash.");
			String ss = "/edu/gmu/cs/infs614/webdealer/view/CustomerUsernameView.fxml";
			fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(ss)));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
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
		oracleConn = new MainConnection(OracleConnection.user,OracleConnection.pass);

		if (!oracleConn.isConnected) {
			fxConnectButton.setText("Incorrect!");
			fxConnectButton.setTextFill(Color.rgb(210, 39, 30));
		} else {
			fxConnectButton.setText("Confirmed!");
			fxConnectButton.setTextFill(Color.rgb(21, 117, 84));
			Schema schema = new Schema(new SchemaConnection(OracleConnection.user,OracleConnection.pass).getConnection());

			if(!schema.isSchemaLoaded()) {

				schema.loadSchema();
				schema.loadVoucherTrigger();
				schema.loadInserts();

			}
			customerUsernameLoginSplash();
		}
		fxPasswordField.clear();

		customerUsernameLogin();

	}



	// DISPLAY SPLASH LOGIN

	public void startSplash() {
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
		//if()
		customerDialogue();
	}

	private void customerDialogue() {
		try {
			AppUtil.console(fxScrollPane.idProperty().toString());
			String cv = "/edu/gmu/cs/infs614/webdealer/view/CustomerView.fxml";
			fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(cv)));
			fxSplitPane.setDividerPosition(1, 0.7607);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AppUtil.console(e.toString());
		}
	}

	// DISPLAY MERCHANT TABLE

	public void displayMerchantDialogue(ActionEvent event) {
		AppUtil.console("Showing Merchant dialogue.");
		merchantDialogue();
	}

	private void merchantDialogue() {
		try {
			AppUtil.console(fxScrollPane.idProperty().toString());
			String mv = "/edu/gmu/cs/infs614/webdealer/view/MerchantView.fxml";
			merchant = (AnchorPane) FXMLLoader.load(getClass().getResource(mv));
			fxScrollPane.setContent(merchant);
			
			fxSplitPane.setDividerPosition(1, 0.7607);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AppUtil.console(e.toString());
		}
	}

	// DISPLAY PURCHASE TABLE

	public void displayPurchaseDialogue(ActionEvent event) {
		AppUtil.console("Showing Purchase dialogue.");
		//if()
		purchaseDialogue();
	}

	private void purchaseDialogue() {
		try {
			AppUtil.console(fxScrollPane.idProperty().toString());
			String pv = "/edu/gmu/cs/infs614/webdealer/view/PurchaseView.fxml";
			fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(pv)));
			fxSplitPane.setDividerPosition(1, 0.7607);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AppUtil.console(e.toString());
		}
	}

	// DISPLAY DEAL TABLE

	public void displayDealDialogue(ActionEvent event) {
		AppUtil.console("Showing Deal dialogue.");
		dealDialogue();
	}

	private void dealDialogue() {
		try {
			AppUtil.console(fxScrollPane.idProperty().toString());
			String dv = "/edu/gmu/cs/infs614/webdealer/view/DealView.fxml";
			fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(dv)));
			fxSplitPane.setDividerPosition(1, 0.7607);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AppUtil.console(e.toString());
		}
	}
	// DISPLAY REVIEW TABLE

	public void displayReviewDialogue(ActionEvent event) {
		AppUtil.console("Showing Review dialogue.");
		reviewDialogue();
	}

	private void reviewDialogue() {
		try {
			AppUtil.console(fxScrollPane.idProperty().toString());
			String rv = "/edu/gmu/cs/infs614/webdealer/view/ReviewView.fxml";
			fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(rv)));
			fxSplitPane.setDividerPosition(1, 0.7607);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			AppUtil.console(e.toString());
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
				fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(lv)));
				fxSplitPane.setDividerPosition(1, 0.7607);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				AppUtil.console(e.toString());
			}
		}


	// Query View Controls

	public void displayOpenQueryDialogue(ActionEvent event) {
		AppUtil.console("Showing Purchase dialogue.");
		//if()
		openQueryDialogue();

	}

	private void openQueryDialogue() {
		try {
			//AppUtil.console(fxScrollPane.idProperty().toString());
			fxPreviousView = (AnchorPane) fxScrollPane.getContent();
			String ov = "/edu/gmu/cs/infs614/webdealer/view/OpenQueryView.fxml";
			GridPane pane = (GridPane) FXMLLoader.load(WebDealerApplication.class.getResource(ov));
			fxScrollPane.setContent(pane);
			fxSplitPane.setDividerPosition(1, 0.57);
			fxConsoleTextArea.setText("");

			fxtfOpenQuery.setOnKeyPressed(new EventHandler<KeyEvent>()
					{
				@Override
				public void handle(KeyEvent ke)
				{
					if (ke.getCode().equals(KeyCode.ENTER))
					{
						openQuery();
					}
				}
					});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			AppUtil.console(e.toString());
		}
	}

	public void displayOpenQuery(ActionEvent event) {
		openQuery();
	}

	public void openQuery() {
		fxConsoleTextArea.setText("");

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			if(fxtfOpenQuery == null || 
					fxtfOpenQuery.getText() == null || 
					fxtfOpenQuery.getText().trim().isEmpty()) {
				AppUtil.console("Please enter query.");
				return;
			}

			String query = fxtfOpenQuery.getText().replace(';', ' ');

			preparedStatement = oracleConn.getConnection().prepareStatement(query);


			// execute select SQL
			rs = preparedStatement.executeQuery();

			ResultSetMetaData meta = rs.getMetaData();
			final int columnCount = meta.getColumnCount();
			AppUtil.console("Column count is: "+columnCount);

			int counter = 1;
			while(rs.next()) {
				AppUtil.console("Row "+(counter++)+":\n");
				for(int i=1;i<=columnCount;i++) {
					AppUtil.console(meta.getColumnLabel(i)+":\t"+rs.getObject(i).toString());
				}
			}

			preparedStatement.close();
			//rs.first(); // It seems this non-scrollable result set resets to first automagically


		} catch (SQLException e) {


			AppUtil.console(e.getMessage());

		}
	}

	public void openQueryClearText(ActionEvent event) {
		fxtfOpenQuery.setText("");
	}

	public void displayOpenQueryExitDialogue (ActionEvent event) {
		openQueryExitDialogue();
	}

	public void openQueryExitDialogue() {
		fxScrollPane.setContent(fxPreviousView); 
		fxSplitPane.setDividerPosition(1, 0.7607);
	}
	
	public void showOpenTables() {
		fxConsoleTextArea.setText("");

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			String query = "SELECT table_name FROM user_tables";

			preparedStatement = oracleConn.getConnection().prepareStatement(query);


			// execute select SQL
			rs = preparedStatement.executeQuery();

			ResultSetMetaData meta = rs.getMetaData();
			final int columnCount = meta.getColumnCount();

			AppUtil.console(meta.getColumnLabel(1)+"\n");
			
			while(rs.next()) {
				
				for(int i=1;i<=columnCount;i++) {
					AppUtil.console(rs.getObject(i).toString());
				}
			}
			
			AppUtil.console("\nQUERY: "+query);

			preparedStatement.close();
			//rs.first(); // It seems this non-scrollable result set resets to first automagically


		} catch (SQLException e) {


			AppUtil.console(e.getMessage());

		}
	}
}
