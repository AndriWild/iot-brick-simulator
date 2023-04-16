package main.java.view;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.java.presentation.PresentationModel;

public class Controls extends Pane {

  private Button addActorButton;
  private Button addSensorButton;

  public Controls(){
    initializeControls();
    layoutControls();
  }

  private void layoutControls() {
    addSensorButton.relocate(0, 30);
    this.getChildren().addAll(addActorButton, addSensorButton);
  }

  private void initializeControls() {
    PresentationModel pm = PresentationModel.getInstance();
    addActorButton= new Button("Add Actor");
    addActorButton.setOnAction(e -> pm.addActor());
    addSensorButton= new Button("Add Sensor");
    addSensorButton.setOnAction(e -> pm.addSensor());
  }


}
