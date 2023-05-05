package main.java.view;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import main.java.controller.BrickController;
import main.java.model.Garden;
import main.java.model.brick.DistanceBrickData;
import main.java.model.brick.ServoBrickData;
import main.java.Constants;
import main.java.util.Location;
import main.java.view.brick.DistancePlacement;
import main.java.view.brick.ServoPlacement;
import main.java.util.Util;
import main.java.util.mvcbase.ViewMixin;

public class GardenGUI extends Pane implements ViewMixin<Garden, BrickController> {

  private final BrickController controller;
  private ProgressIndicator spinner;

  public GardenGUI(BrickController controller) {
    init(controller);
    this.controller = controller;
  }

  @Override
  public void initializeSelf() {
    ViewMixin.super.initializeSelf();
  }

  @Override
  public void initializeParts() {
    spinner = new ProgressIndicator();
  }

  @Override
  public void layoutParts() {
    spinner.relocate(350, 350);
    this.getChildren().add(spinner);
  }

  @Override
  public void setupModelToUiBindings(Garden model) {
    onChangeOf(model.isLoading).execute((oldVal, newVal) -> {
      if(newVal) {
        this.getChildren().add(spinner);
        return;
      }
      this.getChildren().remove(spinner);
    });

    onChangeOf(model.sensors).execute((oldValue, newValue) ->
        newValue.stream()
        .filter(brick -> !oldValue.contains(brick))
        .forEach(newBrick -> {
          DistancePlacement dp = new DistancePlacement(controller, newBrick);
          addSensorListeners(newBrick, dp);
          this.getChildren().add(dp);
        }));

    onChangeOf(model.actuators).execute((oldValue, newValue) ->
        newValue.stream()
        .filter(brick -> !oldValue.contains(brick))
        .forEach(newBrick -> {
          ServoPlacement dp = new ServoPlacement(controller, newBrick);
          addActuatorListeners(newBrick, dp);
          this.getChildren().add(dp);
        }));
  }

  private String getSensorLabelData(DistanceBrickData brick) {
    Location location = Util.toCoordinates(brick.location.getValue().lon(), brick.location.getValue().lat());
    return "id: "          + brick.getID() +
           "\nval: "       + brick.value.getValue() +
//        "\nx: "         + brick.location.getValue().lon() +
//        "\ny: "         + brick.location.getValue().lat() +
           "\nx: "         + location.lat() +
           "\ny: "         + location.lon() +
           "\nfaceAngle: " + brick.faceAngle.getValue();
  }

  private String getActuatorLabelData(ServoBrickData brick) {
    Location location = Util.toCoordinates(brick.location.getValue().lon(), brick.location.getValue().lat());
    return "id: "          + brick.getID() +
           "\nangle: "     + Math.round(brick.mostActiveAngle.getValue()) +
//        "\nx: "         + brick.location.getValue().lon() +
//        "\ny: "         + brick.location.getValue().lat() +
           "\nx: "         + location.lat() +
           "\ny: "         + location.lon() +
           "\nfaceAngle: " + Math.round(brick.faceAngle.getValue());
  }

  private void addSensorListeners(DistanceBrickData newBrick, DistancePlacement dp) {
    onChangeOf(newBrick.location).execute((oldValue, newValue) -> {
      dp.setLayoutY(Constants.WINDOW_WIDTH - newValue.lat());
      dp.setLayoutX(newValue.lon());
      dp.setLabel(getSensorLabelData(dp.getBrick()));
    });

    onChangeOf(newBrick.faceAngle).execute((oldValue, newValue) -> {
      dp.setRotateBrickSymbol(newValue);
      dp.setLabel(getSensorLabelData(dp.getBrick()));
    });

    onChangeOf(newBrick.value).execute((oldVal, currentVal) ->
        dp.setLabel(getSensorLabelData(newBrick)));

    onChangeOf(newBrick.isMostActive).execute((oldVal, newVal) ->
        dp.setHighlighted(newVal));
  }

  private void addActuatorListeners(ServoBrickData newBrick, ServoPlacement dp) {
    onChangeOf(newBrick.location).execute((oldValue, newValue) -> {
      dp.setLayoutY(Constants.WINDOW_WIDTH - newValue.lat());
      dp.setLayoutX(newValue.lon());
      dp.setLabel(getActuatorLabelData(dp.getBrick()));
    });

    onChangeOf(newBrick.faceAngle).execute((oldValue, newValue) -> {
      dp.setLabel(getActuatorLabelData(dp.getBrick()));
      dp.setRotateBrickSymbol(newValue);
    });

    onChangeOf(newBrick.mostActiveAngle).execute((oldVal, newVal) -> {
      dp.setMostActiveSensorAngle(newVal);
      dp.setLabel(getActuatorLabelData(newBrick));
    });

    onChangeOf(newBrick.viewPortAngle).execute((oldVal, newVal) ->
        dp.setFrontViewAngle(newVal));
  }
}