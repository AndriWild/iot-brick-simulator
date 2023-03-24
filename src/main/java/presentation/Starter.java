package main.java.presentation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Starter extends Application {
  @Override
  public void start(Stage stage) {
    Pane pane = new ApplicationUi();
    PresentationModel pm = PresentationModel.getInstance();

    Scene scene = new Scene(pane, pm.getWindowSize().getWidth(), pm.getWindowSize().getHeight());
    stage.titleProperty().bind(pm.windowTitleProperty());
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    Application.launch();
  }
}