package main.java.presentation;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.java.view.Field;
import main.java.view.Grid;

public class ApplicationUi extends Pane {

  private Circle circle;
  private Grid grid;
  private Field field;

  public ApplicationUi() {
    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    this.setBackground(new Background(
        new BackgroundFill(
            Color.LIGHTGRAY,
            new CornerRadii(0),
            new Insets(0))
    ));

    grid   = new Grid(PresentationModel.getInstance());
    field  = new Field();
    circle = new Circle(10);
    circle.relocate(100, 100);
    circle.setFill(Color.RED);
  }

  private void layoutControls() {
    this.getChildren().add(circle);
    this.getChildren().add(grid);
    this.getChildren().add(field);
  }
}
