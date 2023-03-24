package main.java.presentation;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import main.java.view.Grid;

public class ApplicationUi extends Pane {

  private Circle circle;
  private Grid grid;

  public ApplicationUi() {
    initializeControls();
    layoutControls();

  }

  private void initializeControls() {
    this.setStyle("-fx-background-color: grey;");
    circle = new Circle(1);
    circle.relocate(100, 100);
    grid = new Grid(PresentationModel.getInstance());
    this.getChildren().add(grid);
  }

  private void layoutControls() {
    this.getChildren().add(circle);
  }
}
