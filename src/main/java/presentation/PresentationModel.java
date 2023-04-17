package main.java.presentation;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.model.FieldModel;

import java.awt.*;

public final class PresentationModel {

  private static final PresentationModel INSTANCE = new PresentationModel();

  private static final int    WINDOW_HEIGHT = 3 * 256;
  private static final int    WINDOW_WIDTH  = 3 * 256;
  private static final String WINDOW_TITLE  = "IoT - Brick Simulator";

  private ObjectProperty<Dimension>     windowSize;
  private SimpleStringProperty          windowTitle;
  private BooleanProperty               refresh;
  private FieldModel                    field;
  private ObservableList<DistanceBrick> distanceBricks;
  private ObservableList<ServoBrick>    servoBricks;
  private ObjectProperty<DistanceBrick> mostActiveSensor;

  private PresentationModel(){
    initializeProperties();
    startUpdateLoop();
  }

  private void startUpdateLoop() {
    new Thread(() -> {
      while(true){
        refresh.set(!refresh.get());
        DistanceBrick mostActive = field.getMostActive();
        mostActiveSensor.set(mostActive);
      }
    }).start();
  }

  public static PresentationModel getInstance () {
    return INSTANCE;
  }

  public ObservableList<ServoBrick> getActors() {
    return servoBricks;
  }

  public ObservableList<DistanceBrick> getSensors() {
    return distanceBricks;
  }
  public StringProperty windowTitleProperty() {
    return windowTitle;
  }

  public Dimension getWindowSize() {
    return windowSize.get();
  }

  public void addSensor() {
    DistanceBrick newBrick = field.addSensor();
    distanceBricks.add(newBrick);
  }

  public void addActor() {
    ServoBrick sb = field.addActor();
    servoBricks.add(sb);
  }

  public BooleanProperty getRefreshFlag() {
    return refresh;
  }

  public ObjectProperty<DistanceBrick> getMostActiveSensor() {
    return mostActiveSensor;
  }

  private void initializeProperties(){
    windowSize       = new SimpleObjectProperty<>(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    windowTitle      = new SimpleStringProperty(WINDOW_TITLE);
    field            = new FieldModel();
    refresh          = new SimpleBooleanProperty(false);
    mostActiveSensor = new SimpleObjectProperty<>();
    servoBricks      = FXCollections.observableArrayList(field.getActors());
    distanceBricks   = FXCollections.observableArrayList(field.getSensors());
  }
}