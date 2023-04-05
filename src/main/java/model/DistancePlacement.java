package main.java.model;

import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DistancePlacement extends BrickPlacement {

  private final DistanceBrick brick;
  private       Group         distanceShape;
  private       Text          label;

  public DistancePlacement(DistanceBrick brick, double x, double y, double angle) {
    super(x, y, angle);
    this.brick = brick;
    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    label = new Text();
    BrickShape brickIcon = new BrickShape(Color.RED);

    distanceShape = new Group(brickIcon);
    distanceShape.setLayoutX(x);
    distanceShape.setLayoutY(y);
    distanceShape.setRotate(faceAngle);
    label.relocate(this.x + BrickShape.WIDTH, this.y);
  }

  public void setLabel(String text) {
    label.setText(text);
  }

  private void layoutControls() {
    super.getChildren().addAll(distanceShape, label);
  }

  @Override
  public DistanceBrick getBrick() {
    return brick;
  }
}
