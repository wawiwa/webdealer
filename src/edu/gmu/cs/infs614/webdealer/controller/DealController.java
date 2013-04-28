package edu.gmu.cs.infs614.webdealer.controller;

//import java.io.IOException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import edu.gmu.cs.infs614.webdealer.AppUtil;
import edu.gmu.cs.infs614.webdealer.controller.access.UserCreds;
import edu.gmu.cs.infs614.webdealer.model.Deal;
import edu.gmu.cs.infs614.webdealer.model.Purchase;
import edu.gmu.cs.infs614.webdealer.model.connector.CustomerConnection;
import edu.gmu.cs.infs614.webdealer.model.connector.OracleConnection;
import edu.gmu.cs.infs614.webdealer.view.FormValidation;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.layout.AnchorPane;
//import edu.gmu.cs.infs614.webdealer.model.Customer;
//import edu.gmu.cs.infs614.webdealer.model.connector.DealConnection;

public class DealController implements Initializable {
	


		
		// DEFINE TABLE
		
		@FXML
		TableView<Deal> tvDeal;
		@FXML
		TableColumn<Deal, Integer> tcDealID;
		@FXML
		TableColumn<Deal, String> tcExpirationDate;
		@FXML
		TableColumn<Deal, String> tcDescription;
		@FXML
		TableColumn<Deal, Integer> tcQuantityLimit;
		@FXML
		TableColumn<Deal, Float> tcOriginalPrice;
		@FXML
		TableColumn<Deal, Float> tcDealPrice;
		@FXML
		TableColumn<Deal, String> tcSaleStartTime;
		@FXML
		TableColumn<Deal, String> tcSaleEndTime;
		@FXML
		TableColumn<Deal, Integer> tcLocationID;
		@FXML
		TableColumn<Deal, Integer> tcCategoryID;
		@FXML
		TableColumn<Deal, String> tcMerchant;
		@FXML
		TableColumn<Deal, Integer> tcMerchantID;
		@FXML
		TableColumn<Deal, String> tcCity;

		
		// DEFINE FORM
		@FXML
		static
		TextField tfDeal_ID;
		@FXML
		static
		TextField tfexpiration_date;
		@FXML
		static
		TextField tfdescription;
		@FXML
		static
		TextField tfquantity_limit;
		@FXML
		static
		TextField tforiginal_price;
		@FXML
		static
		TextField tfdeal_price;
		@FXML
		static
		TextField tfsale_start_time;
		@FXML
		static
		TextField tfsale_end_time;
		@FXML
		static
		TextField tflocation_ID;
		@FXML
		static
		TextField tfcategory_ID;
		@FXML
		static
		TextField tfmerchant_ID;
		@FXML
		static
		TextField tfmerchant;
		@FXML
		static
		TextField tfcity;
		@FXML
		static
		TextField tfstate;
		@FXML
		static
		TextField tfcountry;
		@FXML
		static
		TextField tfcontinent;
		@FXML
		static
		TextField tfquantity;
		
		
		@FXML
		Button submit;
		@FXML
		Button delete;
		@FXML
		Button clear;
		@FXML
		Button search;
		@FXML
		Button update;
		@FXML
		Button showOpenDeals;
		@FXML
		Button buy;
		
		
		@FXML
		Label expirationDateLabel;
		@FXML
		Label descriptionLabel;
		@FXML
		Label quantityLimitLabel;
		@FXML
		Label originalPriceLabel;
		@FXML
		Label dealPriceLabel;
		@FXML
		Label saleStarttimeLabel;
		@FXML
		Label saleEndtimeLabel;
		@FXML
		Label locationIDLabel;
		@FXML
		Label categoryIDLabel;
		@FXML
		Label merchantIDLabel;
		@FXML
		Label quantityLabel;
		
		// DEFINE VARIABLES
		
		// index for delete item
		private IntegerProperty index = new SimpleIntegerProperty();
		
		// DB connector
		public static Connection conn = new CustomerConnection(OracleConnection.user,OracleConnection.pass).getConnection();
		
		// CREATE TABLE DATA
		

		final ObservableList<Deal> data = FXCollections.observableArrayList();
		
		@Override
		public void initialize(URL location, ResourceBundle resources) {

			
			
			displayDeal();
			
			
			// fix delete button working if not selecting once a table row
			index.set(-1);
			
			tcDealID.setCellValueFactory(new PropertyValueFactory<Deal, Integer>("dID"));
			tcExpirationDate.setCellValueFactory(new PropertyValueFactory<Deal, String>("dExpDate"));
			tcDescription.setCellValueFactory(new PropertyValueFactory<Deal, String>("dDescription"));
			//tcQuantityLimit.setCellValueFactory(new PropertyValueFactory<Deal, Integer>("dQuantity"));
			//tcOriginalPrice.setCellValueFactory(new PropertyValueFactory<Deal, Float>("dOrigPrice"));
			tcDealPrice.setCellValueFactory(new PropertyValueFactory<Deal, Float>("dDealPrice"));
			
			tcSaleEndTime.setCellValueFactory(new PropertyValueFactory<Deal, String>("dSaleEnd"));
			
			tcCity.setCellValueFactory(new PropertyValueFactory<Deal,String>("dCityName"));
			
			//tcMerchant.setCellValueFactory(new PropertyValueFactory<Deal, Integer>("dMerchant"));
			tcMerchantID.setCellValueFactory(new PropertyValueFactory<Deal, Integer>("mID"));
			
			   AppUtil.console("DATA contents: "+data);
			   tvDeal.setItems(data);
			   
			   
			   
			   
			// get the index when clicking on table row
				tvDeal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
					public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
						if(newValue==null)return;
						Deal d = (Deal) newValue;
						
						tfDeal_ID.setText(d.getDID().toString()); 
						tfexpiration_date.setText(d.getDExpDate().toString());
						tfdescription.setText(d.getDDescription().toString());
						tfquantity_limit.setText(d.getDQuantity().toString());
						tforiginal_price.setText(d.getDOrigPrice().toString());
						tfdeal_price.setText(d.getDDealPrice().toString());
						tfsale_start_time.setText(d.getDSaleStart().toString());
						tfsale_end_time.setText(d.getDSaleEnd().toString());
						tflocation_ID.setText(d.getLID().toString());
						tfcategory_ID.setText(d.getCatID().toString());
						tfmerchant_ID.setText(d.getMID().toString());
			
						
						
						index.set(data.indexOf(newValue));
						AppUtil.console("OK index is: "+data.indexOf(newValue));
						AppUtil.console(d.getDID().toString());
					}
				});
		}
		
		public void onAddItem(ActionEvent event) {
			
			// FORM VALIDATION
			boolean expirationDate = FormValidation.textFieldNotEmpty(tfexpiration_date,expirationDateLabel,"Expiration date is required!");
			boolean description = FormValidation.textFieldNotEmpty(tfdescription,descriptionLabel,"Description is required!");
			boolean quantityLimit = FormValidation.textFieldTypeInteger(tfquantity_limit,quantityLimitLabel,"Quantity must be a number.");
			boolean originalPrice = FormValidation.textFieldNotEmpty(tforiginal_price,originalPriceLabel,"Original price must be a currency.");
			boolean dealPrice = FormValidation.textFieldNotEmpty(tfdeal_price,dealPriceLabel,"Deal price must be a currency.");
			boolean saleStarttime = FormValidation.textFieldNotEmpty(tfsale_start_time,dealPriceLabel,"Sale start time must be after current date.");
			boolean saleEndtime = FormValidation.textFieldNotEmpty(tfsale_end_time,dealPriceLabel,"Sale end time must be after start date.");
			boolean locationID = FormValidation.textFieldNotEmpty(tflocation_ID,dealPriceLabel,"Use your location ID.");
			boolean categoryID = FormValidation.textFieldNotEmpty(tfcategory_ID,dealPriceLabel,"Select the appropriate category.");
			boolean merchantID = FormValidation.textFieldNotEmpty(tfmerchant_ID,dealPriceLabel,"Use your merchant ID.");
			
			if(expirationDate && description && quantityLimit && originalPrice && dealPrice && saleStarttime && saleEndtime &&
					locationID && categoryID && merchantID) {
				
				// add the data any time and the will be updated
				Deal entry = new Deal(conn,null,tfexpiration_date,
						tfdescription,
						tfquantity_limit,
						tforiginal_price,
						tfdeal_price,
						tfsale_start_time,
						tfsale_end_time,
						tflocation_ID,
						tfcategory_ID,
						tfmerchant_ID);
				if(!entry.isInDatabase) return;
				// insert data in table
				data.add(entry);
				
				// clear TextFields
				clearForm();
				
			}
		}
		
		
		public void displayDeal() {
			AppUtil.console("Displaying deals..");
			ArrayList<Deal> dl = Deal.retrieve(conn,tfDeal_ID,tfexpiration_date,tfdescription,tfquantity_limit,tforiginal_price,tfdeal_price,tfsale_start_time,tfsale_end_time,tflocation_ID,
					tfcategory_ID,tfmerchant_ID);
			
			try {
				data.clear();
			}
			catch(Exception e) {
				data.clear();
			}
			for(Deal d : dl) {
				data.add(d);
			}
			// clear TextFields
			clearForm();
		}
		
		public void onSearchItem(ActionEvent event) {
			displayDeal();
		}
		

		public void onUpdateItem(ActionEvent event) {
			AppUtil.console("Updating a deal.");
			int i = index.get();
			Deal oldDeal = data.get(i);
			Deal newDeal = new Deal(conn,tfDeal_ID,tfexpiration_date,tfdescription,tfquantity_limit,tforiginal_price,tfdeal_price,
					tfsale_start_time,tfsale_end_time,tflocation_ID,tfcategory_ID,tfmerchant_ID);
			if(Deal.update(oldDeal,newDeal)) {
				data.set(i, newDeal);
			}
			
			AppUtil.console("INDEX: "+i);
		}

		public void onDeleteItem(ActionEvent event) {
			AppUtil.console("Attempting to delete 1 item");
			int i = index.get();
			if(i <=-1 || i>=data.size()) return;
			Deal oldDeal = data.get(i);
			if(Deal.delete(oldDeal)) {
				data.remove(i);
				tvDeal.getSelectionModel().select(i);
				tvDeal.getSelectionModel().clearSelection();
			}
			
		}

		public static Integer getCurrentDealID() {
			AppUtil.console("Getting currently selected deal..");
			ArrayList<Deal> dl =
					Deal.retrieve(conn, tfDeal_ID,tfexpiration_date,tfdescription,tfquantity_limit,tforiginal_price,tfdeal_price,tfsale_start_time,
							tfsale_end_time,tflocation_ID,tfcategory_ID,tfmerchant_ID);
			
			// get first deal
			for(Deal d : dl) {
				if(d.getDID() == null) return -1;
				return d.getDID();
			}
			return -1;
		}

		public void onBuyItem(ActionEvent event) {
			AppUtil.console("PC onAddItem..");
			
			displayPurchases();
			
			// need a trans id and voucher id hence two nulls
			Purchase entry = new Purchase(conn,null,new TextField(UserCreds.getLogin()),
					tfDeal_ID,tfquantity,null,null,null);
		
			if(!entry.isInDatabase) return;
			
			// insert multiple purchased vouchers into table
			ArrayList<Purchase> pl = entry.getPurchaseViews();
			AppUtil.console("PURCHASE LIST SIZE: "+pl.size());
			PurchaseController.data.clear();
			PurchaseController.data.addAll(pl);
				
			// clear TextFields
			clearForm();
			
			
				
		}
		

		public void displayPurchases() {
			try {
				String pv = "/edu/gmu/cs/infs614/webdealer/view/PurchaseView.fxml";
				WebDealerApplicationController.fxScrollPane.setContent((AnchorPane) FXMLLoader.load(getClass().getResource(pv)));
			} catch (IOException e) {
				AppUtil.console(e.toString());
			}
		}
		
		
		private void clearForm() {
			tfDeal_ID.clear();
			tfexpiration_date.clear();
			tfdescription.clear();
			tfquantity_limit.clear();
			tforiginal_price.clear();
			tfdeal_price.clear();
			tfsale_start_time.clear();
			tfsale_end_time.clear();
			tflocation_ID.clear();
			tfcategory_ID.clear();
			tfmerchant_ID.clear();
			
			tfDeal_ID.setText(null);
			tfexpiration_date.setText(null);
			tfdescription.setText(null);
			tfquantity_limit.setText(null);
			tforiginal_price.setText(null);
			tfdeal_price.setText(null);
			tfsale_start_time.setText(null);
			tfsale_end_time.setText(null);
			tflocation_ID.setText(null);
			tfcategory_ID.setText(null);
			tfmerchant_ID.setText(null);
			
		
			expirationDateLabel.setText(null);
			descriptionLabel.setText(null);
			quantityLimitLabel.setText(null);
			originalPriceLabel.setText(null);
			dealPriceLabel.setText(null);
			saleStarttimeLabel.setText(null);
			saleEndtimeLabel.setText(null);
			locationIDLabel.setText(null);
			categoryIDLabel.setText(null);
			merchantIDLabel.setText(null);
			
			
			tfDeal_ID.getStyleClass().remove("error");
			tfexpiration_date.getStyleClass().remove("error");
			tfdescription.getStyleClass().remove("error");
			tfquantity_limit.getStyleClass().remove("error");
			tforiginal_price.getStyleClass().remove("error");
			tfdeal_price.getStyleClass().remove("error");
			tfsale_start_time.getStyleClass().remove("error");
			tfsale_end_time.getStyleClass().remove("error");
			tflocation_ID.getStyleClass().remove("error");
			tfcategory_ID.getStyleClass().remove("error");
			tfmerchant_ID.getStyleClass().remove("error");
			

			
		}
		
		public void onClearAction (ActionEvent event) {
			
			clearForm();
		}






}
