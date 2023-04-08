package main.java.presentation;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import main.java.view.Field;
import main.java.view.Grid;

public class ApplicationUi extends Pane {

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
  }

  private void layoutControls() {
    this.getChildren().add(grid);
    this.getChildren().add(field);
    Circle c = new Circle(100,100,8);
    c.setFill(Color.GREEN);
    c.setRotationAxis(Rotate.Z_AXIS);
    c.setRotate(180.0);
    this.getChildren().add(c);
  }
}
