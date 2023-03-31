package main.java.view;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.java.model.BrickPlacement;
import main.java.presentation.PresentationModel;

import java.util.ArrayList;
import java.util.List;

public class Field extends Pane {

  private List<BrickPlacement> actorBricks;
  private List<BrickPlacement> sensorBricks;
  private Button btn;

  public Field() {
    initializeControls();
    layoutControls();
  }

  private void layoutControls() {
    this.getChildren().addAll(actorBricks);
    this.getChildren().addAll(sensorBricks);
    this.getChildren().add(btn);
  }

  private void updateControls(){
    this.getChildren().clear();
    layoutControls();
  }
  private void initializeControls() {
    PresentationModel pm = PresentationModel.getInstance();
    actorBricks  = new ArrayList<>(pm.getActorPlacement());
    sensorBricks = new ArrayList<>(pm.getSensorPlacement());

    pm.getActorPlacement().addListener(
        (ListChangeListener<BrickPlacement>) change -> {
          actorBricks = pm.getActorPlacement();
          updateControls();
        }
    );

    pm.getSensorPlacement().addListener(
        (ListChangeListener<BrickPlacement>) change -> {
          sensorBricks = pm.getSensorPlacement();
          updateControls();
        }
    );

    btn = new Button("Add Brick");
    btn.setOnAction(event -> PresentationModel.getInstance().addBrick());
  }
}
