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
import main.java.model.BrickPlacement;
import main.java.model.ServoPlacement;
import main.java.model.DistancePlacement;

import java.awt.*;
import java.util.Comparator;
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
  private static final String SERVO_BRICK_2_ID    = "0000-0080";
  private static final String SERVO_BRICK_3_ID    = "0000-0080";
  private static final String SERVO_BRICK_4_ID    = "0000-0080";
  private static final String SERVO_BRICK_5_ID    = "0000-0080";
  private static final String SERVO_BRICK_6_ID    = "0000-0080";
  private static final String SERVO_BRICK_7_ID    = "0000-0080";

  private static final PresentationModel INSTANCE = new PresentationModel();

  private ObjectProperty<Dimension> windowSize;
  private SimpleStringProperty      windowTitle;

  private ObservableList<DistancePlacement> sensorPlacement;
  private ObservableList<ServoPlacement>    actorPlacement;

  private PresentationModel(){
    initializeBrickPlacements();
    initializeProperties();
  }

  private void initializeBrickPlacements() {
    ProxyGroup proxies = new ProxyGroup();
    sensorPlacement = FXCollections.observableArrayList(initializeSensors(proxies));
    actorPlacement  = FXCollections.observableArrayList(initializeActors(proxies));

    new Thread(() -> update(proxies)).start();
  }

  private DistancePlacement getMostActiveSensor() {
    return sensorPlacement.stream()
        .sorted(Comparator.comparing(t -> t.getBrick().getDistance()))
        .toList()
        .get(0);
  }

  private double calculateAngleToPlacement(BrickPlacement from, BrickPlacement to) {

    //
    //  t       t
    //  |\     /|
    //  | \   / |dy
    //  *--a a--*
    //  -dx f dx
    //  *--a a--*
    //  | /   \ |-dy
    //  |/     \|
    //  t       t
    //
    double dx = to.getX() - from.getX();
    double dy = to.getY() - from.getY();
    double a = Math.toDegrees(Math.atan(Math.abs(dx) / Math.abs(dy)));
    //System.out.println("dx = " + dx + ", dy = " + dy + ", arctan(abs(dx)/abs(dy)) = " + a);
    double result; // 0 degree => north, increase clockwise
    if (dx > 0 && dy > 0) {
      result = 90 - a;
    } else if (dx > 0 && dy < 0) {
      result = 90 + a;
    } else if (dx < 0 && dy < 0) {
      result = 270 - a;
    } else if (dx < 0 && dy > 0) {
      result = 270 + a;
    } else if (dx == 0 && dy > 0) {
      result = 0;
    } else if (dx == 0 && dy < 0) {
      result = 180;
    } else if (dx > 0 && dy == 0) {
      result = 90;
    } else if (dx < 0 && dy == 0) {
      result = 270;
    } else {
      assert (dx == 0 && dy == 0);
      result = 0;
    }
    return result;
  }

  private void update(ProxyGroup proxies) {
    while(true) {
      showSensorValues();

      for (ServoPlacement servo: actorPlacement) {
        double angle = calculateAngleToPlacement(servo, getMostActiveSensor());
        int pos      = calculatePositionFromAngle(servo, angle);

        pos = Math.max(0, Math.min(pos, 180));
        servo.setArrowAngle(pos);
        servo.setLabel(
                "faceAngle: " + servo.getFaceAngle() +
                "\nangle: "   + Math.floor(angle) +
                "\npos:"      + pos +
                "\nx:"        + servo.getX() +
                "\ny:"        + servo.getY());
      }

//      try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
      proxies.waitForUpdate();
    }
  }

  private int calculatePositionFromAngle(ServoPlacement servo, double angle) {
    double result = (angle - servo.getFaceAngle()) - 90.0;
    if (result < 0) { result += 360.0; }
    return (int) result;
  }

  private void showSensorValues() {
    sensorPlacement.forEach(sensor ->
        sensor.setLabel(
                "id: "    + sensor.getBrick().getID() +
                "\nval: " + sensor.getBrick().getDistance() +
                "\nx: "   + sensor.getX() +
                "\ny: "   + sensor.getY()
        )
    );
  }

  private List<DistancePlacement> initializeSensors(ProxyGroup group) {
    Proxy sensorProxy  = MockProxy.fromConfig(BASE_URL);
    group.addProxy(sensorProxy);

    List<DistancePlacement> sensors = List.of(
        new DistancePlacement(DistanceBrick.connect(sensorProxy, DISTANCE_BRICK_0_ID), 260, 380, 0),
        new DistancePlacement(DistanceBrick.connect(sensorProxy, DISTANCE_BRICK_1_ID), 470, 330, 0)
    );
    return sensors;
  }

  private List<ServoPlacement> initializeActors(ProxyGroup group) {
    Proxy actorProxy  = MockProxy.fromConfig(BASE_URL);
    group.addProxy(actorProxy);

    List<ServoPlacement> servos = List.of(
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_0_ID), 350, 100, 0),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_1_ID), 480, 215, 0),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_2_ID), 600, 350, 0),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_3_ID), 480, 485, 0),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_4_ID), 350, 600, 0),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_5_ID), 215, 485, 0),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_6_ID), 100, 350, 0),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_7_ID), 215, 215, 0)
    );
    servos.get(0).setArrowAngle(90);
    return servos;
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

  public ObservableList<DistancePlacement> getSensorPlacement() {
    return sensorPlacement;
  }

  public ObservableList<ServoPlacement> getActorPlacement() {
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
