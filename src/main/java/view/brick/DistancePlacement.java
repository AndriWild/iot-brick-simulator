package main.java.view.brick;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import main.java.controller.GardenController;
import main.java.model.brick.DistanceBrickData;

public class DistancePlacement extends BrickPlacement {

  private final DistanceBrickData brick;
  private       Group         distanceShape;
  private BrickNode brickIcon;

  public DistancePlacement(GardenController controller, DistanceBrickData brick) {
    super(controller, brick);
    this.brick = brick;
    initializeControls();
    layoutControls();
  }

  public void setHighlighted(boolean isHighlighted) {
    if (isHighlighted) {
      brickIcon.getBody().setStroke(Color.YELLOW);
    } else {
      brickIcon.getBody().setStroke(null);
    }
  }

  private void initializeControls() {
    brickIcon = new BrickNode(Color.RED);

    Arc viewPort = new Arc(
        BrickNode.CENTER_X,
        5,
        25.0,
        25.0,
        45.0,
        90
    );
    viewPort.setType(ArcType.ROUND);
    viewPort.setFill(Color.grayRgb(100, 0.7));

    distanceShape = new Group(viewPort, brickIcon);
    distanceShape.setRotate(faceAngle);
  }

  public void setRotateBrickSymbol(double angel){
    distanceShape.setRotate(angel);
  }

  private void layoutControls() {
    super.getChildren().addAll(distanceShape);
  }

  @Override
  public DistanceBrickData getBrick() {
    return brick;
  }
}
