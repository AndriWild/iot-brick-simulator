package main.java.view;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.java.presentation.PresentationModel;

public class Controls extends Pane {

  private Button addActorButton;
  private Button addSensorButton;
  private Button printSnapshot;

  public Controls() {
    initializeControls();
    layoutControls();
  }

  private void layoutControls() {
    addSensorButton.relocate(0, 30);
    printSnapshot.relocate(80, 0);
    this.getChildren().addAll(addActorButton, addSensorButton, printSnapshot);
  }

  private void initializeControls() {
    PresentationModel pm = PresentationModel.getInstance();
    addActorButton = new Button("Add Actor");
    addSensorButton = new Button("Add Sensor");
    printSnapshot = new Button("Print Brick Data");
    addActorButton.setOnAction(e -> pm.addActor());
    addSensorButton.setOnAction(e -> pm.addSensor());
    printSnapshot.setOnAction(e -> pm.printSnapshot());
  }
}