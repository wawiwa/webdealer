package edu.gmu.cs.infs614.webdealer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class first_one extends Application {

	@Override
	public void start(Stage primaryStage) {
		BorderPane bp = new BorderPane();
		bp.setCenter(new Button("Hello J1"));
		Scene s = new Scene(bp);
		primaryStage.setScene(s);
		primaryStage.setWidth(200);
		primaryStage.setHeight(200);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
