package main.java.model;

import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.presentation.PresentationModel;

public class DistancePlacement extends BrickPlacement {

  private final DistanceBrick brick;
  private       Group         distanceShape;
  private       Text          label;

  public DistancePlacement(DistanceBrick brick, double longitude, double latitude, double angle) {
    super(longitude, latitude, angle);
    this.brick = brick;
    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    int windowHeight = PresentationModel.getInstance().getWindowSize().height;
    label = new Text();
    BrickShape brickIcon = new BrickShape(Color.RED);
    distanceShape = new Group(brickIcon);
    distanceShape.setLayoutX(longitude);
    distanceShape.setLayoutY(windowHeight - latitude);
    distanceShape.setRotate(faceAngle);
    label.relocate(this.longitude, windowHeight - this.latitude + BrickShape.WIDTH);
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
