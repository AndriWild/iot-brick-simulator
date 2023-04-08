package main.java.model;

import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
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

    Arc viewPort = new Arc(
        BrickShape.CENTER_X,
        5,
        30.0,
        30.0,
        45.0,
        90
    );
    viewPort.setType(ArcType.ROUND);
    viewPort.setFill(Color.grayRgb(100, 0.5));

    distanceShape = new Group(viewPort, brickIcon);
    distanceShape.setLayoutX(longitude);
    distanceShape.setLayoutY(windowHeight - latitude);
    distanceShape.setRotate(faceAngle);
    label.relocate(this.longitude + BrickShape.WIDTH + 10, windowHeight - this.latitude - BrickShape.HEIGHT);
  }

  public void setLabel(String text) {
    label.setText(text);
  }

  private void layoutControls() {
    this.setOnMouseEntered(event -> super.getChildren().add(label));
    this.setOnMouseExited(event -> super.getChildren().remove(label));
    super.getChildren().addAll(distanceShape);
  }

  @Override
  public DistanceBrick getBrick() {
    return brick;
  }
}
