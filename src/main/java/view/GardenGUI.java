package main.java.view;

import ch.fhnw.imvs.bricks.core.Brick;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import main.java.controller.GardenController;
import main.java.model.Garden;
import main.java.old.model.view.brick.BrickPlacement;
import main.java.old.model.view.brick.DistancePlacement;
import main.java.old.model.view.brick.ServoPlacement;
import main.java.util.mvcbase.ViewMixin;

import java.util.ArrayList;
import java.util.List;

public class GardenGUI extends Pane implements ViewMixin<Garden, GardenController> {

  private Button idButton;
  private Button addBrick;
  private Label idLabel;
  private List<DistancePlacement> sensors;

  public GardenGUI(GardenController controller) {
    init(controller);
  }

  @Override
  public void initializeSelf() {
    ViewMixin.super.initializeSelf();
  }

  @Override
  public void initializeParts() {
    idButton = new Button("+");
    addBrick = new Button("add Brick");
    idLabel = new Label();
    addBrick.relocate(50,50);
    sensors = new ArrayList<>();
  }

  @Override
  public void layoutParts() {
    getChildren().addAll(idButton, idLabel, addBrick);
  }

  @Override
  public void setupUiToActionBindings(GardenController controller) {
    idButton.setOnAction(e -> controller.increase());
    addBrick.setOnAction(e -> controller.addBrickToArray());
  }

  @Override
  public void setupModelToUiBindings(Garden model) {
    onChangeOf(model.id)
        .convertedBy(String::valueOf)
        .update(idLabel.textProperty());

    onChangeOf(model.bricksList).execute((oldValue, newValue) -> {
      newValue.stream()
              .filter(brick -> !oldValue.contains(brick))
              .forEach(newBrick -> {
                DistancePlacement dp = new DistancePlacement(newBrick, 100, 100, 0);
                onChangeOf(newBrick.value).execute((oldVal, currentVal) ->
                        dp.setLabel(String.valueOf(currentVal)));
                this.getChildren().add(dp);
              });
    });

  }
}
