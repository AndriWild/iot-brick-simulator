package main.java.view;

import ch.fhnw.imvs.bricks.core.Brick;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import main.java.controller.GardenController;
import main.java.model.Garden;
import main.java.util.mvcbase.ViewMixin;

public class GardenGUI extends Pane implements ViewMixin<Garden, GardenController> {

  private Button idButton;
  private Button addBrick;
  private Label idLabel;

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
      addBrick= new Button("add Brick");
      idLabel = new Label();
  }

  @Override
  public void layoutParts() {
    getChildren().addAll(idButton, idLabel);
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
  }
}
