package main.java.presentation;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
  }
}