package com.viewer_console;

import com.gui.core.mapViewer.ViewMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application  {
	
	@Override
    public void start(Stage primaryStage) {
        Parent root = (Parent) AppConfig.loader.load("com/views/view.fxml");
        root.setStyle("-fx-background-color: whitesmoke;");
//      Scene scene = new Scene(root, 1920, 1080);
        Scene scene = new Scene(root);
//      primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getMaxX());
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds().getMaxY());
        MapView._WIDTH.set((int) primaryStage.getWidth());
        MapView._HEIGHT.set((int) primaryStage.getHeight());
    }
	
	public static void main(String[] args) {
        launch(args);
    }


}
