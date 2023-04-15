package main.java.presentation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.awt.*;

public final class PresentationModel {


  private static final int WINDOW_HEIGHT   = 800;
  private static final int WINDOW_WIDTH    = 800;
  private static final String WINDOW_TITLE = "IoT - Brick Simulator";

  private static final PresentationModel INSTANCE = new PresentationModel();

  private ObjectProperty<Dimension> windowSize;
  private SimpleStringProperty      windowTitle;

  private PresentationModel(){
    initializeProperties();
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

  private void initializeProperties(){
    windowSize  = new SimpleObjectProperty<>(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    windowTitle = new SimpleStringProperty(WINDOW_TITLE);
  }

  public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }
}
