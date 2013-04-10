package edu.gmu.cs.infs614.webdealer.model;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FormValidation {

	public static boolean textFieldNotEmpty(TextField i) {
		boolean r = false;
		if(i.getText() != null && !i.getText().isEmpty()) {
			r = true;
		}
		
		return r;
	}
	
	public static boolean textFieldNotEmpty(TextField i, Label l, String sValidationText) {
		boolean r = true;
		String c = null;
		if(!textFieldNotEmpty(i)) {
			r = false;;
			c = sValidationText;
		}
		l.setText(c);
		return r;
	}
}
