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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.controller.access.UserCreds;
import edu.gmu.cs.infs614.webdealer.model.Purchase;
import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.PurchaseConnection;

public class PurchaseController implements Initializable {

	// MAIN VIEW SEGMENT

	
	// DEFINE TABLE
	
	@FXML
	TableView<Purchase> fxtvPurchase;
	@FXML
	TableColumn<Purchase, Integer> fxtcPurchaseID;
	@FXML
	TableColumn<Purchase, String> fxtcPurchaseDate;
	@FXML
	TableColumn<Purchase, String> fxtcCustomerEmail;
	@FXML
	TableColumn<Purchase, Integer> fxtcVoucherID;
	@FXML
	TableColumn<Purchase, String> fxtcStatus;
	@FXML
	TableColumn<Purchase, Integer> fxtcDealID;
	@FXML
	TableColumn<Purchase, Integer> fxtcPaymentID;

	
	// DEFINE FORM
	@FXML
	TextField fxtfPurchaseID;
	@FXML
	TextField fxtfPurchaseDate;
	@FXML
	public
	static
	TextField fxtfCustomerEmail;
	@FXML
	TextField fxtfVoucherID;
	@FXML
	TextField fxtfStatus;
	@FXML
	TextField fxtfDealID;
	@FXML
	TextField fxtfPaymentID;
	@FXML
	TextField fxtfCustomerID;
	@FXML
	TextField fxtfQuantity;
	
	@FXML
	Label fxlVoucherID;
	@FXML
	Label fxlStatus;

	@FXML
	Button fxbRetrieve;
	@FXML
	Button fxbClear;
	@FXML
	Button fxbStatus;
	@FXML
	Button fxbBuy;
	

	
	
	// DEFINE VARIABLES
	
	// index for delete item
	private IntegerProperty index = new SimpleIntegerProperty();
	
	// DB connector
	
	public Connection conn = new PurchaseConnection(OracleConnection.user,OracleConnection.pass).getConnection();
	
	public static final ObservableList<Purchase> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AppUtil.console("PC initialize..");
		
		
		displayPurchases();
		
		// fix delete button working if not selecting once a table row
		index.set(-1);
		
		fxtcPurchaseID.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("pTransaction_ID"));
		fxtcPurchaseDate.setCellValueFactory(new PropertyValueFactory<Purchase, String>("pTrans_date"));
		fxtcVoucherID.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("pVoucher_ID"));
		fxtcStatus.setCellValueFactory(new PropertyValueFactory<Purchase, String>("pStatus"));
		fxtcDealID.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("pDeal_ID"));
		fxtcCustomerEmail.setCellValueFactory(new PropertyValueFactory<Purchase, String>("pEmailAddress"));
		fxtcPaymentID.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("pPayment_ID"));
		

        AppUtil.console("DATA contents: "+data);

		fxtvPurchase.setItems(data);
		
		// get the index when clicking on table row
		fxtvPurchase.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
				if(newValue==null)return;
				Purchase p = (Purchase) newValue;
				
				fxtfPurchaseID.setText(p.getPTransaction_ID().toString());
				fxtfPurchaseDate.setText(p.getPTrans_date());
				fxtfCustomerEmail.setText(p.getPEmailAddress());
				fxtfVoucherID.setText(p.getPVoucher_ID().toString());
				fxtfStatus.setText(p.getPStatus());
				fxtfDealID.setText(p.getPDeal_ID().toString());
				fxtfPaymentID.setText(p.getPPayment_ID().toString());
				fxtfCustomerID.setText(p.getPCustomer_ID().toString());
				
				index.set(data.indexOf(newValue));
				AppUtil.console("OK index is: "+data.indexOf(newValue));
				AppUtil.console(p.getPTransaction_ID().toString());
			}
		});
	}

	public void onAddItem(ActionEvent event) {
		AppUtil.console("PC onAddItem..");
		// need a trans id and voucher id hence two nulls
		Purchase entry = new Purchase(conn,null,fxtfCustomerEmail,
				fxtfDealID,fxtfQuantity,null,null,null);
	
		if(!entry.isInDatabase) return;
		
		// insert multiple purchased vouchers into table
		ArrayList<Purchase> pl = entry.getPurchaseViews();
		AppUtil.console("PURCHASE LIST SIZE: "+pl.size());
		data.addAll(pl);
			
		// clear TextFields
		clearForm();
			
	}
	
	public void onSearchItem(ActionEvent event) {
		AppUtil.console("PC onSearchItem..");
		displayPurchases();
	}
		
	public void displayPurchases() {
		AppUtil.console("Display purchases..");

		ArrayList<Purchase> pl =
				Purchase.retrieve(conn, 
						fxtfVoucherID,
						fxtfStatus, 
						fxtfDealID, 
						fxtfPurchaseID, 
						fxtfPurchaseDate, 
						fxtfPaymentID,
						fxtfCustomerID,
						fxtfCustomerEmail);
		
		try {
			data.clear();
		}
		catch(Exception e) {
			data.clear();
		}
		for(Purchase p : pl) {
			AppUtil.console("PC: Adding purchases to view..");
			AppUtil.console("Purchase "+p.getPTransaction_ID()+" with deal "+p.getPDeal_ID());
			AppUtil.console("PC: Purchase voucher_id="+p.getPVoucher_ID());
			data.addAll(p.getPurchaseViews());
		}
		// clear TextFields
		clearForm();
	}

	
	public void onUpdateItem(ActionEvent event) {
		AppUtil.console("Updating purchase status.");
		int i = index.get();
		Purchase oldPurchase = data.get(i);

		Purchase newPurchase = new Purchase(conn,fxtfPurchaseID,fxtfCustomerEmail,
				fxtfDealID,fxtfQuantity,fxtfVoucherID,fxtfPurchaseDate,fxtfStatus);
		if(Purchase.update(oldPurchase,newPurchase)) {
			data.set(i, newPurchase);
		}
		
		AppUtil.console("INDEX: "+i);
	}

	private void clearForm() {

		fxtfVoucherID.clear();
		fxtfStatus.clear();
		fxtfDealID.clear();
		fxtfPurchaseID.clear(); 
		fxtfPurchaseDate.clear();
		fxtfPaymentID.clear();
		fxtfCustomerID.clear();
		fxtfCustomerEmail.clear();
		
		fxtfVoucherID.setText(null);
		fxtfStatus.setText(null);
		fxtfDealID.setText(null);
		fxtfPurchaseID.setText(null); 
		fxtfPurchaseDate.setText(null);
		fxtfPaymentID.setText(null);
		fxtfCustomerID.setText(null);
		fxtfCustomerEmail.setText(null);
		
		fxlVoucherID.setText(null);
		fxlStatus.setText(null);
		
		fxtfVoucherID.getStyleClass().remove("error");
		fxlStatus.getStyleClass().remove("error");
		
	}
	
	public void onClearAction (ActionEvent event) {
		
		clearForm();
	}
	
	
}
