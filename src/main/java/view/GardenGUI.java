package main.java.view;

import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import main.java.Constants;
import main.java.controller.BrickController;
import main.java.model.Garden;
import main.java.model.brick.BrickData;
import main.java.model.brick.DistanceBrickData;
import main.java.model.brick.ServoBrickData;
import main.java.util.mvcbase.ViewMixin;
import main.java.view.brick.BrickPlacement;
import main.java.view.brick.DistancePlacement;
import main.java.view.brick.ServoPlacement;

import java.util.List;

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
    onChangeOf(model.isLoading).execute((oldValue, newValue) -> {
      if(newValue) {
        this.getChildren().add(spinner);
        return;
      }
      this.getChildren().remove(spinner);
    });

    onChangeOf(model.actuators).execute((oldValue, newValue) -> {
          if(oldValue.size() > newValue.size()) {
            removePlacement(oldValue, newValue);
          } else {
            if (newValue.isEmpty()) return;
            addActuatorPlacement(model, oldValue, newValue);
          }
        }
    );

    onChangeOf(model.sensors).execute((oldValue, newValue) -> {
          if(oldValue.size() > newValue.size()) {
            removePlacement(oldValue, newValue);
          } else {
            if (newValue.isEmpty()) return;
            addDistancePlacement(model, oldValue, newValue);
          }
        }
    );
  }

  private void addDistancePlacement(Garden model, List<DistanceBrickData> oldValue, List<DistanceBrickData> newValue) {
    DistancePlacement newBrick = newValue
        .stream()
        .filter(brick -> !oldValue.contains(brick))
        .map(brick -> new DistancePlacement(controller, brick))
        .toList()
        .get(0);

    addSensorListeners(newBrick);
    addDistancePlacement(model, newBrick);
  }

  private void addActuatorPlacement(Garden model, List<ServoBrickData> oldValue, List<ServoBrickData> newValue) {
    ServoPlacement newBrick = newValue
        .stream()
        .filter(brick -> !oldValue.contains(brick))
        .map(brick -> new ServoPlacement(controller, brick))
        .toList()
        .get(0);

    addActuatorListeners(newBrick);
    addDistancePlacement(model, newBrick);
  }

  private void addDistancePlacement(Garden model, BrickPlacement placement) {
    addPlacementListener(model, placement);
    this.getChildren().add(placement);
  }

  private void removePlacement(List<? extends BrickData> oldValue, List<? extends BrickData> newValue) {
   BrickData removed = oldValue
        .stream()
        .filter(brick -> !newValue.contains(brick))
        .toList()
        .get(0);

    this.getChildren().forEach(brick -> {
      if(brick instanceof BrickPlacement sp){
        if(sp.getBrick().getID().equals(removed.getID())) {
          Platform.runLater(() -> this.getChildren().removeAll(sp));
        }
      }
    });
  }

  private void addPlacementListener(Garden model, BrickPlacement placement) {
    onChangeOf(model.removeButtonVisible).execute((oldVal, newVal) -> placement.setRemoveBtnVisible(newVal));

    onChangeOf(placement.getBrick().faceAngle).execute((oldValue, newValue) -> {
      placement.setRotateBrickSymbol(newValue);
      refreshLabel(placement);
    });

    onChangeOf(placement.getBrick().location).execute((oldValue, newValue) -> {
      placement.setLayoutY(Constants.WINDOW_WIDTH - newValue.lat());
      placement.setLayoutX(newValue.lon());
      refreshLabel(placement);
    });
  }

  private void addSensorListeners(DistancePlacement placement) {
    onChangeOf(placement.getBrick().value)       .execute((oldVal, currentVal) -> refreshLabel(placement));
    onChangeOf(placement.getBrick().isMostActive).execute((oldVal, newVal)     -> placement.setHighlighted(newVal));
  }

  private void addActuatorListeners(ServoPlacement placement) {
    onChangeOf(placement.getBrick().mostActiveAngle).execute((oldVal, newVal) -> {
      placement.setMostActiveSensorAngle(newVal);
      refreshLabel(placement);
    });
    onChangeOf(placement.getBrick().viewPortAngle).execute((oldVal, newVal) -> placement.setFrontViewAngle(newVal));
  }

  private void refreshLabel(BrickPlacement placement){
    placement.setLabel(placement.getBrick().toStringFormatted());
  }
}