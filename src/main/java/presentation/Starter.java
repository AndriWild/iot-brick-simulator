package main.java.presentation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Starter extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    PresentationModel pm = PresentationModel.getInstance();
    Pane pane = new ApplicationUi(stage);

    Scene scene = new Scene(pane, pm.getWindowSize().getWidth(), pm.getWindowSize().getHeight());
    stage.titleProperty().bind(pm.windowTitleProperty());
    stage.setScene(scene);
    stage.show();

  }

  public static void main(String[] args) {
    Application.launch();
  }
}