package main.java.view;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.java.presentation.PresentationModel;

public class Controls extends Pane {

  private Button addButton;

  public Controls(){

    initializeControls();
    layoutControls();

  }

  private void layoutControls() {
    this.getChildren().add(addButton);
  }

  private void initializeControls() {
    addButton = new Button("Add Actor");
  }


}
