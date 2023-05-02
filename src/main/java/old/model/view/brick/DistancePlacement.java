package main.java.old.model.view.brick;

import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import main.java.model.DistanceBrickData;
import main.java.old.model.presentation.PresentationModel;

public class DistancePlacement extends BrickPlacement {

  private final DistanceBrickData brick;
  private       Group         distanceShape;
  private       Text          label;
  private       BrickShape    brickIcon;

  public DistancePlacement(DistanceBrickData brick, double longitude, double latitude, double angle) {
    super(longitude, latitude, angle);
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
    int windowHeight = PresentationModel.getInstance().getWindowSize().height;
    brickIcon = new BrickShape(Color.RED);

    Arc viewPort = new Arc(
        BrickShape.CENTER_X,
        5,
        25.0,
        25.0,
        45.0,
        90
    );
    viewPort.setType(ArcType.ROUND);
    viewPort.setFill(Color.grayRgb(100, 0.7));

    distanceShape = new Group(viewPort, brickIcon);
    distanceShape.setLayoutX(xPos);
    distanceShape.setLayoutY(windowHeight - yPos);
    distanceShape.setRotate(faceAngle);

    this.setOnScroll( e -> {

      if(e.getDeltaY() > 0){
        this.faceAngle += 2;
      }
      if(e.getDeltaY() < 0) {

        this.faceAngle -= 2;
      }
      distanceShape.setRotate(faceAngle);
    });

    label = new Text();
    label.relocate(this.xPos + BrickShape.WIDTH + 10, windowHeight - this.yPos - BrickShape.HEIGHT);
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
  public DistanceBrickData getBrick() {
    return brick;
  }
}
