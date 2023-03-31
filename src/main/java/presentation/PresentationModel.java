package main.java.presentation;

import ch.fhnw.imvs.bricks.actuators.RelayBrick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.core.ProxyGroup;
import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.model.ActorPlacement;
import main.java.model.BrickPlacement;
import main.java.model.SensorPlacement;

import java.awt.*;
import java.util.List;

public class PresentationModel {

  private final int    WINDOW_HEIGHT = 800;
  private final int    WINDOW_WIDTH  = 800;
  private final String WINDOW_TITLE  = "IoT - Brick Simulator";

  private static final String BASE_URL            = "brick.li/";
  private static final String DISTANCE_BRICK_0_ID = "0000-0030";
  private static final String DISTANCE_BRICK_1_ID = "0000-0300";
  private static final String SERVO_BRICK_0_ID    = "0000-0008";
  private static final String SERVO_BRICK_1_ID    = "0000-0080";

  private static final PresentationModel INSTANCE = new PresentationModel();

  private ObjectProperty<Dimension> windowSize;
  private SimpleStringProperty      windowTitle;

  private ObservableList<BrickPlacement> sensorPlacement;
  private ObservableList<BrickPlacement> actorPlacement;

  private PresentationModel(){
    initializeBrickPlacements();
    initializeProperties();
  }

  private void initializeBrickPlacements() {
    ProxyGroup proxies = new ProxyGroup();
    sensorPlacement = FXCollections.observableArrayList(initializeSensors(proxies));
    actorPlacement  = FXCollections.observableArrayList(initializeActors(proxies));
  }

  private List<SensorPlacement> initializeSensors(ProxyGroup group) {
    Proxy sensorProxy  = MockProxy.fromConfig(BASE_URL);
    group.addProxy(sensorProxy);

    List<DistanceBrick> sensors = List.of(
        DistanceBrick.connect(sensorProxy, DISTANCE_BRICK_0_ID),
        DistanceBrick.connect(sensorProxy, DISTANCE_BRICK_0_ID)
    );

    return sensors.stream()
        .map(brick ->
            new SensorPlacement(
                brick,
                getRandomNumber(10, 790),
                getRandomNumber(10, 790),
                getRandomNumber(0, 360)
            )
        )
        .toList();
  }

  private List<ActorPlacement> initializeActors(ProxyGroup group) {
    Proxy actorProxy  = MockProxy.fromConfig(BASE_URL);
    group.addProxy(actorProxy);


    List<RelayBrick> actors = List.of(
        RelayBrick.connect(actorProxy, SERVO_BRICK_0_ID),
        RelayBrick.connect(actorProxy, SERVO_BRICK_0_ID)
    );

    return actors.stream()
        .map(brick ->
            new ActorPlacement(
                brick,
                getRandomNumber(10, 790),
                getRandomNumber(10, 790),
                getRandomNumber(0, 360)
            )
        )
        .toList();
  }

  public void addBrick() {
//    bricks.add(new BrickPlacement(
//        getRandomNumber(10, 790),
//        getRandomNumber(10, 790),
//        getRandomNumber( 0, 360),
//        getRandomNumber( 0, 255)
//    ));
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

  public ObservableList<BrickPlacement> getSensorPlacement() {
    return sensorPlacement;
  }

  public ObservableList<BrickPlacement> getActorPlacement() {
    return actorPlacement;
  }

  private void initializeProperties(){
    windowSize  = new SimpleObjectProperty<>(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    windowTitle = new SimpleStringProperty(WINDOW_TITLE);
  }

  public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }
}
