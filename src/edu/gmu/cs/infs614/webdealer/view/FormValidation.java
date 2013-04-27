package edu.gmu.cs.infs614.webdealer.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FormValidation {

	public static boolean textFieldNotEmpty(TextField i) {
		boolean r = false;
		
		if(i==null) return r;
		if(i.getText() != null && !i.getText().isEmpty()) {
			r = true;
		}
		
		return r;
	}
	
	public static boolean textFieldNotEmpty(TextField i, Label l, String sValidationText) {
		boolean r = true;
		String c = null;
		i.getStyleClass().remove("error");
		if(!textFieldNotEmpty(i)) {
			r = false;;
			c = sValidationText;
			i.getStyleClass().add("error");
		}
		l.setText(c);
		return r;
	}
	
	public static boolean textFieldTypeDate(TextField i) {
		boolean r = false;
		// dd/mm/yyyy
		if(i==null) return r;
		if(i.getText() == null) return r;
		try {
			getDate(i.getText());
			r=true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			r=false;
		}
		return r;	
		
	}
	
	public static boolean textFieldTypeDate(TextField i, Label l, String sValidationText) {
		boolean r = true;
		String c = null;
		i.getStyleClass().remove("error");
		if(!textFieldTypeDate(i)) {
			r = false;;
			c = sValidationText;
			i.getStyleClass().add("error");
		}
		l.setText(c);
		return r;
	}
	
	
	private static Date getDate(String text) throws java.text.ParseException {

	    try {
	        // try the day format first
	        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	        df.setLenient(false);

	        return df.parse(text);
	    } catch (ParseException e) {

	        // fall back on the month format
	        SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
	        df.setLenient(false);

	        return df.parse(text);
	    }
	}
	
	public static boolean textFieldTypeNumber(TextField i) {
		boolean r = false;
		
		String currency2 = "^[ ]*[\\d]+[.][\\d][\\d]";
		String currency1 = "^[ ]*[\\d]+[.][\\d]";
		String currency0 = "^[ ]*[\\d]+";
		
		if(i==null) return r;
		if(i.getText() != null && i.getText().matches(currency2)) {
			r=true;
		} else if(i.getText().matches(currency1)) {
			r=true;
		} else if(i.getText().matches(currency0)) {
			i.setText(i.getText()+".0");
			r=true;
		}
		return r;
		
	}
	
	public static boolean textFieldTypeNumber(TextField i, Label l, String sValidationText) {
		boolean r = true;
		String c = null;
		i.getStyleClass().remove("error");
		if(!textFieldTypeNumber(i)) {
			r = false;;
			c = sValidationText;
			i.getStyleClass().add("error");
		}
		l.setText(c);
		return r;
	}
	
	public static boolean textFieldTypeInteger(TextField i) {
		boolean r = false;

		String val = "^[ ]*[\\d]+";
		if(i != null && i.getText() != null && i.getText().matches(val)) {
			r=true;
		}
		return r;
		
	}
	
	public static boolean textFieldTypeInteger(TextField i, Label l, String sValidationText) {
		boolean r = true;
		String c = null;
		i.getStyleClass().remove("error");
		if(!textFieldTypeInteger(i)) {
			r = false;
			c = sValidationText;
			i.getStyleClass().add("error");
		}
		l.setText(c);
		return r;
	}
}
