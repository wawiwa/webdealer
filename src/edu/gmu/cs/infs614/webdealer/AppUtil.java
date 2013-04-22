package edu.gmu.cs.infs614.webdealer;

import javafx.scene.control.TextArea;
import edu.gmu.cs.infs614.webdealer.controller.WebDealerApplicationController;

public class AppUtil {
	
	final private static TextArea ta = WebDealerApplicationController.fxConsoleTextArea;
	
	private static String nl = "\n";
	
	public static void console(String stuff) {
		if(ta==null)return;
		ta.appendText(stuff+nl);
	}

}
