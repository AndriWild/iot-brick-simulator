package main.java.view;

import javafx.scene.layout.Pane;
import main.java.controller.GardenController;
import main.java.model.Garden;
import main.java.model.brick.BrickData;
import main.java.model.brick.DistanceBrickData;
import main.java.model.brick.ServoBrickData;
import main.java.old.model.view.brick.BrickPlacement;
import main.java.old.model.view.brick.DistancePlacement;
import main.java.old.model.view.brick.ServoPlacement;
import main.java.util.mvcbase.ViewMixin;

public class GardenGUI extends Pane implements ViewMixin<Garden, GardenController> {

  private final GardenController controller;

  public GardenGUI(GardenController controller) {
    init(controller);
    this.controller = controller;
  }

  @Override
  public void initializeSelf() {
    ViewMixin.super.initializeSelf();
  }

  @Override
  public void initializeParts() {
  }

  @Override
  public void layoutParts() {
  }

  @Override
  public void setupModelToUiBindings(Garden model) {

    onChangeOf(model.sensors).execute((oldValue, newValue) -> {
      newValue.stream()
          .filter(brick -> !oldValue.contains(brick))
          .forEach(newBrick -> {
            DistancePlacement dp = new DistancePlacement(controller, newBrick);
            addSensorListeners(newBrick, dp);
            this.getChildren().add(dp);
          });
    });

    onChangeOf(model.actuators).execute((oldValue, newValue) -> {
      newValue.stream()
          .filter(brick -> !oldValue.contains(brick))
          .forEach(newBrick -> {
            ServoPlacement dp = new ServoPlacement(controller, newBrick);
            addActuatorListeners(newBrick, dp);
            this.getChildren().add(dp);
          });
    });
  }

  private String getSensorLabelData(DistanceBrickData brick) {
    return "id: "          + brick.getID() +
           "\nval: "       + brick.value.getValue() +
           "\nx: "         + brick.x.getValue() +
           "\ny: "         + brick.y.getValue() +
           "\nfaceAngle: " + brick.faceAngle.getValue();
  }

  private String getActuatorLabelData(ServoBrickData brick) {
    return "id: "          + brick.getID() +
           "\nangle: "     + brick.mostActiveAngle.getValue() +
           "\nx: "         + brick.x.getValue() +
           "\ny: "         + brick.y.getValue() +
           "\nfaceAngle: " + brick.faceAngle.getValue();
  }

  private void addSensorListeners(DistanceBrickData newBrick, DistancePlacement dp) {
    onChangeOf(newBrick.x).execute((oldValue, newValue) ->
        dp.setLayoutX(newValue));

    onChangeOf(newBrick.y).execute((oldValue, newValue) ->
        dp.setLayoutY(newValue));

    onChangeOf(newBrick.faceAngle).execute((oldValue, newValue) ->
        dp.setRotateBrickSymbol(newValue));

    onChangeOf(newBrick.value).execute((oldVal, currentVal) ->
        dp.setLabel(getSensorLabelData(newBrick)));

    onChangeOf(newBrick.isMostActive).execute((oldVal, newVal) ->
        dp.setHighlighted(newVal));
  }

  private void addActuatorListeners(ServoBrickData newBrick, ServoPlacement dp) {
    onChangeOf(newBrick.x).execute((oldValue, newValue) ->
        dp.setLayoutX(newValue));

    onChangeOf(newBrick.y).execute((oldValue, newValue) ->
        dp.setLayoutY(newValue));

    onChangeOf(newBrick.faceAngle).execute((oldValue, newValue) ->
        dp.setRotateBrickSymbol(newValue));

    onChangeOf(newBrick.mostActiveAngle).execute((oldVal, newVal) -> {
      dp.setMostActiveSensorAngle(newVal);
      dp.setLabel(getActuatorLabelData(newBrick));
    });

    onChangeOf(newBrick.viewPortAngle).execute((oldVal, newVal) ->
        dp.setFrontViewAngle(newVal));
  }
}