package main.java.presentation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.model.Brick;

import java.awt.*;
import java.util.List;

public class PresentationModel {

  private final int    WINDOW_HEIGHT = 800;
  private final int    WINDOW_WIDTH  = 800;
  private final String WINDOW_TITLE  = "IoT - Brick Simulator";

  private static final PresentationModel INSTANCE = new PresentationModel();

  private ObjectProperty<Dimension> windowSize;
  private SimpleStringProperty windowTitle;
  private ObservableList<Brick> bricks;

  private PresentationModel(){
    List<Brick> brickList = List.of(
        new Brick(100,  50,  30,  0),
        new Brick( 80, 610, 250, 30),
        new Brick(460, 180,  85, 20)
    );
    initializeProperties(brickList);
  }

  public void addBrick() {
    bricks.add(new Brick(
        getRandomNumber(10, 790),
        getRandomNumber(10, 790),
        getRandomNumber( 0, 360),
        getRandomNumber( 0, 255)
    ));
  }

  public static PresentationModel getInstance () {
    return INSTANCE;
  }

  public StringProperty windowTitleProperty() {
    return windowTitle;
  }

  public Dimension getWindowSize() {
    return windowSize.get();
  }

  public ObservableList<Brick> getBricks() {
    return bricks;
  }

  private void initializeProperties(List<Brick> brickList){
    windowSize  = new SimpleObjectProperty<>(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    windowTitle = new SimpleStringProperty(WINDOW_TITLE);
    bricks = FXCollections.observableArrayList(brickList);
  }

  public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }
}
