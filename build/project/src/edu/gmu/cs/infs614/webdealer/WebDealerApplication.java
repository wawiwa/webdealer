package edu.gmu.cs.infs614.webdealer;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WebDealerApplication extends Application {

	
	public static void main(String[] args) {
		Application.launch(WebDealerApplication.class, (java.lang.String[]) null);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try {
			System.out.println("Application starting...");
			//AnchorPane page = (AnchorPane) FXMLLoader.load(MainView.class.getResource("ApplicationController.fxml"));
			String ca = "/edu/gmu/cs/infs614/webdealer/view/ComplexApplication.fxml";
			VBox page = (VBox) FXMLLoader.load(WebDealerApplication.class.getResource(ca));
			System.out.println("Main FXML loaded...");
			Scene scene = new Scene(page);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Window Title");
			primaryStage.show();
		}
		catch (Exception e) {
			Logger.getLogger(WebDealerApplication.class.getName(), null).log(Level.SEVERE, null, e);
		};
		
		
	}

}
