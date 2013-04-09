package edu.gmu.cs.infs614.webdealer.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Table {

	private final SimpleIntegerProperty rID;
	private final SimpleStringProperty rName;
	private final SimpleStringProperty rDate;
	private final SimpleIntegerProperty rPrice;
	
	public Table(int sID, String sName, String sDate, Integer sPrice) {
		this.rID = new SimpleIntegerProperty(sID);
		this.rName = new SimpleStringProperty(sName);
		this.rDate = new SimpleStringProperty(sDate);
		this.rPrice = new SimpleIntegerProperty(sPrice);
	}

	// getters and setters
	
	public void setrID(Integer v) {
		rID.set(v);
	}

	public void setrName(String v) {
		rName.set(v);
	}

	public void setrDate(String v) {
		rDate.set(v);
	}

	public void setrPrice(Integer v) {
		rPrice.set(v);
	}
	
	public SimpleIntegerProperty getrID() {
		return rID;
	}

	public SimpleStringProperty getrName() {
		return rName;
	}

	public SimpleStringProperty getrDate() {
		return rDate;
	}

	public SimpleIntegerProperty getrPrice() {
		return rPrice;
	}
	
	
	
	
}
