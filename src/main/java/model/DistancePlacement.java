package main.java.model;

import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DistancePlacement extends BrickPlacement {

  private DistanceBrick brick;
  private Text label;

  public DistancePlacement(DistanceBrick brick, double x, double y, double angle) {
    super(x, y, angle, Color.RED);
    this.brick = brick;
    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    label = new Text(this.x + 5, this.y + 5, "test");
  }

  public void setLabel(String text) {
    label.setText(text);
  }

  private void layoutControls() {
    super.getChildren().add(label);
  }

  @Override
  public DistanceBrick getBrick() {
    return brick;
  }
}
