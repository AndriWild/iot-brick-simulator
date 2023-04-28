package main.java.presentation;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.model.FieldModel;

import java.awt.*;

import static main.java.model.Constants.WINDOW_HEIGHT;
import static main.java.model.Constants.WINDOW_WIDTH;

public final class PresentationModel {

  private static final PresentationModel INSTANCE = new PresentationModel();
  private static final String WINDOW_TITLE        = "IoT - Brick Simulator";

  private ObjectProperty<Dimension>     windowSize;
  private SimpleStringProperty          windowTitle;

  private BooleanProperty               refresh;
  private BooleanProperty               printSnapshotData;
  private FieldModel                    field;
  private ObservableList<DistanceBrick> distanceBricks;
  private ObservableList<ServoBrick>    servoBricks;
  private ObjectProperty<DistanceBrick> mostActiveSensor;
  private StringProperty                sensorId;
  private StringProperty                actorId;

  private PresentationModel(){
    initializeProperties();
    startUpdateLoop();
  }

  private void startUpdateLoop() {
    System.out.println("PresentationModel.startUpdateLoop");
    new Thread(() -> {
      while(true){
        refresh.set(!refresh.get());

        DistanceBrick mostActive = field.getMostActive();
        mostActiveSensor.set(mostActive);
        System.out.println("loop: "+ Thread.currentThread());
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
    DistanceBrick newBrick = field.addSimulatedSensor();
    distanceBricks.add(newBrick);
  }

  public void addMqttSensor() {
    DistanceBrick newBrick = field.addMqttSensor(sensorId.get());
    distanceBricks.add(newBrick);
  }

  public void addMqttActor() {
    DistanceBrick newBrick = field.addMqttSensor(actorId.get());
    distanceBricks.add(newBrick);
  }

  public void addSimulatedActor() {
    ServoBrick sb = field.addSimulatedActor();
    servoBricks.add(sb);
  }

  public StringProperty sensorIdProperty() {
    return sensorId;
  }

  public StringProperty actorIdProperty() {
    return actorId;
  }

  public BooleanProperty printSnapshotDataProperty() {
    return printSnapshotData;
  }

  public void printSnapshot() {
    printSnapshotData.set(!printSnapshotData.get());
  }

  public BooleanProperty getRefreshFlag() {
    return refresh;
  }

  public ObjectProperty<DistanceBrick> getMostActiveSensor() {
    return mostActiveSensor;
  }

  private void initializeProperties(){
    windowSize        = new SimpleObjectProperty<>(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    windowTitle       = new SimpleStringProperty(WINDOW_TITLE);
    field             = new FieldModel();
    refresh           = new SimpleBooleanProperty(false);
    printSnapshotData = new SimpleBooleanProperty(false);
    mostActiveSensor  = new SimpleObjectProperty<>();
    sensorId          = new SimpleStringProperty("0000-0003");
    actorId           = new SimpleStringProperty("0000-0000");
    servoBricks       = FXCollections.observableArrayList(field.getActors());
    distanceBricks    = FXCollections.observableArrayList(field.getSensors());
  }
}