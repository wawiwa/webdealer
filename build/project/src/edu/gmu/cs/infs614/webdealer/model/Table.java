package edu.gmu.cs.infs614.webdealer.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Table {

	private final SimpleIntegerProperty rID;
	private final SimpleStringProperty rName;
	private final SimpleStringProperty rDate;
	private final SimpleFloatProperty rPrice;
	
	public Table(Integer sID, String sName, String sDate, Float f) {
		this.rID = new SimpleIntegerProperty(sID);
		this.rName = new SimpleStringProperty(sName);
		this.rDate = new SimpleStringProperty(sDate);
		this.rPrice = new SimpleFloatProperty(f);
	}

	// getters and setters
	
	public void setRID(Integer v) {
		rID.set(v);
	}

	public void setRName(String v) {
		rName.set(v);
	}

	public void setRDate(String v) {
		rDate.set(v);
	}

	public void setRPrice(Float v) {
		rPrice.set(v);
	}
	
	public Integer getRID() {
		return rID.get();
	}

	public String getRName() {
		return rName.get();
	}

	public String getRDate() {
		return rDate.get();
	}

	public Float getRPrice() {
		return rPrice.get();
	}
	
	
	
	
}
