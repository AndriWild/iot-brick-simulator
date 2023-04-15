package main.java.view;

import ch.fhnw.imvs.bricks.actuators.RelayBrick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.core.ProxyGroup;
import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.java.model.BrickShape;
import main.java.model.DistancePlacement;
import main.java.model.ServoPlacement;
import main.java.presentation.PresentationModel;
import main.java.util.Util;

import java.util.Comparator;
import java.util.List;

public class Field extends Pane {

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

  private List<ServoPlacement>    servoPlacements;
  private List<DistancePlacement> distancePlacements;
  private Group                   legend;

  public Field() {
    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    PresentationModel pm = PresentationModel.getInstance();

    ProxyGroup proxies = new ProxyGroup();
    Proxy actorProxy   = MockProxy.fromConfig(BASE_URL);
    Proxy sensorProxy  = MockProxy.fromConfig(BASE_URL);
    proxies.addProxy(sensorProxy);
    proxies.addProxy(actorProxy);


    servoPlacements = List.of(
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_0_ID), 350, 100, 30),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_1_ID), 480, 215, 90),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_2_ID), 600, 350, 20),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_3_ID), 480, 485, 180),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_4_ID), 350, 600, 300),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_5_ID), 215, 485, 320),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_6_ID), 100, 350, 230),
        new ServoPlacement(RelayBrick.connect(actorProxy, SERVO_BRICK_7_ID), 215, 215, 190)
    );

    distancePlacements = List.of(
        new DistancePlacement(DistanceBrick.connect(sensorProxy, DISTANCE_BRICK_0_ID), 260, 380, 0),
        new DistancePlacement(DistanceBrick.connect(sensorProxy, DISTANCE_BRICK_1_ID), 470, 330, 0)
    );

    drawLegend(pm);

    new Thread(() -> update(proxies)).start();
  }

  private void layoutControls() {
    this.getChildren().addAll(servoPlacements);
    this.getChildren().addAll(distancePlacements);
    this.getChildren().add(legend);
  }

  private DistancePlacement getMostActiveSensor(List<DistancePlacement> placements) {
    return placements.stream()
        .sorted(Comparator.comparing(t -> t.getBrick().getDistance()))
        .toList()
        .get(0);
  }

  private int calculateServoPositionFromAngle(ServoPlacement servo, double angle) {
    double result = angle - servo.getFaceAngle() + 90;
    if (result < 0) { result += 360.0; }
    return (int) result;
  }


  private void showSensorValues() {
    distancePlacements.forEach(sensor ->
        sensor.setLabel(
            "id: "    + sensor.getBrick().getID() +
            "\nval: " + sensor.getBrick().getDistance() +
            "\nlat: " + sensor.getLatitude() +
            "\nlon: " + sensor.getLongitude()
        )
    );
  }

  private void update(ProxyGroup proxies) {
    while(true) {
      showSensorValues();

      for (ServoPlacement servo: servoPlacements) {
        DistancePlacement mostActiveSensor = getMostActiveSensor(distancePlacements);
        double dLat  = mostActiveSensor.getLatitude() - servo.getLatitude();
        double dLong =  mostActiveSensor.getLongitude() - servo.getLongitude();
        double angle = Util.calcAngle(dLat, dLong);
        int pos      = calculateServoPositionFromAngle(servo, angle);

        servo.setMostActiveSensorAngle(angle - servo.getFaceAngle());
        servo.setFrontViewAngle(180 + angle - 2 * servo.getFaceAngle());
        servo.setLabel(
            "faceAngle: " + servo.getFaceAngle() +
            "\nangle: "   + Math.floor(angle) +
            "\npos:"      + pos +
            "\nlat:"      + servo.getLatitude() +
             "\nlon:"     + servo.getLongitude());
      }

//      try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
      proxies.waitForUpdate();
    }
  }

  private void drawLegend(PresentationModel pm) {
    int gap          = 25;
    int legendWidth  = 160;
    int legendHeight =  80;
    int windowHeight = pm.getWindowSize().height;
    int windowWidth  = pm.getWindowSize().width;

    Rectangle legendBorder = new Rectangle(legendWidth, legendHeight);
    legendBorder.setFill(Color.WHITE);

    DistancePlacement dummyDistance = new DistancePlacement(null, gap,                         windowHeight -gap,  0);
    ServoPlacement    dummyServo    = new ServoPlacement   (null, gap + BrickShape.WIDTH + gap, windowHeight - gap, 0);

    Text labelDistance = new Text(gap,                              gap + 45, "Sensor");
    Text labelServo    = new Text(gap + BrickShape.WIDTH + gap + 10,gap + 45, "Actor");
    Text frontLabel    = new Text(gap + BrickShape.WIDTH - 5,       gap - 5,  "Front");

    legend = new Group(legendBorder, dummyDistance, dummyServo, labelDistance, labelServo, frontLabel);
    legend.setLayoutX(windowWidth - legendWidth);
    legend.setLayoutY(0);
  }
}
