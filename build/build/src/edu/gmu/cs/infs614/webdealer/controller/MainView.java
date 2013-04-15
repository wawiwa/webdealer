package edu.gmu.cs.infs614.webdealer.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {

	public static void main(String[] args) {
		Application.launch(MainView.class, (java.lang.String[]) null);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try {
			//AnchorPane page = (AnchorPane) FXMLLoader.load(MainView.class.getResource("ApplicationController.fxml"));
			VBox page = (VBox) FXMLLoader.load(MainView.class.getResource("ComplexApplication.fxml"));
			Scene scene = new Scene(page);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Window Title");
			primaryStage.show();
		}
		catch (Exception e) {
			Logger.getLogger(MainView.class.getName(), null).log(Level.SEVERE, null, e);
		};
		
	}

}
