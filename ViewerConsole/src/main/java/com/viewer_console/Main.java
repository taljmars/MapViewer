package com.viewer_console;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application  {
	
	@Override
    public void start(Stage primaryStage) {
        Parent root = (Parent) AppConfig.loader.load("com/views/view.fxml");
		root.setStyle("-fx-background-color: whitesmoke;");
		Scene scene = new Scene(root, 650, 550);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	public static void main(String[] args) {
        launch(args);
    }


}
