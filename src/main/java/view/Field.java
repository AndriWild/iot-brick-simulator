package main.java.view;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.core.Brick;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.java.presentation.PresentationModel;
import main.java.util.Util;
import main.java.view.brick.BrickShape;
import main.java.view.brick.DistancePlacement;
import main.java.view.brick.ServoPlacement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Field extends Pane {

  private List<ServoPlacement>          servoPlacements;
  private List<DistancePlacement>       distancePlacements;
  private DistancePlacement             mostActivePlacement;
  private Group                         legend;
  private BooleanProperty               refresh;
  private ObjectProperty<DistanceBrick> mostActiveSensor;

  public Field() {
    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    PresentationModel pm = PresentationModel.getInstance();

    refresh = new SimpleBooleanProperty();
    refresh.bind(pm.getRefreshFlag());
    refresh.addListener( (_1, _2, _3 ) -> updateUi());

    mostActiveSensor = new SimpleObjectProperty<>();
    mostActiveSensor.bind(pm.getMostActiveSensor());
    mostActiveSensor.addListener((obj, oldValue, newValue) -> updateMostActiveSensor(newValue));

    servoPlacements    = new ArrayList<>();
    distancePlacements = new ArrayList<>();
    mostActivePlacement = null;

    initializeListeners(pm);
    drawLegend(pm);
  }

  private void initializeListeners(PresentationModel pm) {
    pm.getActors().addListener((ListChangeListener<Brick>) c -> {
      while(c.next()){
        if(c.wasAdded()){
          ServoBrick servo = (ServoBrick) c.getList().get(c.getFrom());
          ServoPlacement actorPlacement = new ServoPlacement(servo,100.0,100.0,0);
          servoPlacements.add(actorPlacement);
          this.getChildren().add(actorPlacement);
        }
      }
    });

    pm.getSensors().addListener((ListChangeListener<Brick>) c -> {
      while(c.next()){
        if(c.wasAdded()){
          DistanceBrick b = (DistanceBrick) c.getList().get(c.getFrom());
          DistancePlacement sensorPlacement = new DistancePlacement(b,100.0,100.0,0);
          distancePlacements.add(sensorPlacement);
          this.getChildren().add(sensorPlacement);
        }
      }
    });
  }

  private void updateMostActiveSensor(DistanceBrick obj) {
    Optional<DistancePlacement> optionallyMostActive =
        distancePlacements
            .stream()
            .filter(placement -> placement.getBrick().getID().equals(obj.getID()))
            .findFirst();

    optionallyMostActive.ifPresent(distancePlacement -> mostActivePlacement = distancePlacement);
  }

  private void updateUi() {
    distancePlacements.forEach(this::showSensorValues);
    if(mostActivePlacement != null) {
      servoPlacements   .forEach(servo -> updateServoAngles(servo, mostActivePlacement));
    }
    servoPlacements   .forEach(this::showActorValues);
  }

  private void layoutControls() {
    this.getChildren().addAll(servoPlacements);
    this.getChildren().addAll(distancePlacements);
    this.getChildren().add(legend);
  }

  private void showSensorValues(DistancePlacement sensor) {
    sensor.setLabel(
        "Sensor ID: " + sensor.getBrick().getID() +
            "\nval: " + sensor.getBrick().getDistance() +
            "\nlat: " + sensor.getLatitude() +
            "\nlon: " + sensor.getLongitude()
    );
  }

  private void showActorValues(ServoPlacement actor) {
    actor.setLabel(
        "Actor ID: "    +  actor.getBrick().getID() +
        "\nfaceAngle: " + actor.getFaceAngle() +
        "\npos: "       + Math.floor(actor.getMostActiveSensorAngle()) +
//      "\npos:"        + pos +
        "\nlat:"        + actor.getLatitude() +
        "\nlon:"        + actor.getLongitude()
    );
  }

  private void updateServoAngles(ServoPlacement servo, DistancePlacement mostActivePlacement) {
        double dLat  = mostActivePlacement.getLatitude() - servo.getLatitude();
        double dLong = mostActivePlacement.getLongitude() - servo.getLongitude();
        double angle = Util.calcAngle(dLat, dLong);
        int pos      = Util.calculateServoPositionFromAngle(servo, angle);
//        servo.adjustServoPosition(pos);
        servo.setMostActiveSensorAngle(angle - servo.getFaceAngle());
        servo.setFrontViewAngle(180 + angle - 2 * servo.getFaceAngle());
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
