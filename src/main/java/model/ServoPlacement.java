package main.java.model;

import ch.fhnw.imvs.bricks.actuators.RelayBrick;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import main.java.presentation.PresentationModel;

public class ServoPlacement extends BrickPlacement {

  private final RelayBrick brick;
  private       Polygon    arrow;
  private       Group      servoShape;
  private       Text       label;

  public ServoPlacement(RelayBrick brick, double longitude, double latitude, double faceAngle) {
    super(longitude, latitude, faceAngle);
    this.brick = brick;

    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    int windowHeight = PresentationModel.getInstance().getWindowSize().height;
    arrow = new Polygon();
    arrow.setFill(Color.WHITE);
    double delta = BrickShape.WIDTH / 3;
    arrow.getPoints().addAll(
        BrickShape.WIDTH / 2, 3.0,
        delta, BrickShape.HEIGHT - 3,
        delta * 2, BrickShape.HEIGHT - 3);

    BrickShape brickIcon = new BrickShape(Color.BLUE);
    servoShape = new Group(brickIcon, arrow);
    servoShape.setLayoutX(longitude);
    servoShape.setLayoutY(windowHeight - latitude);
    servoShape.setRotate(faceAngle);

    label = new Text();
    label.relocate(this.longitude - 15, windowHeight - this.latitude + BrickShape.WIDTH);
  }

  public void setArrowAngle(double angle) {
    arrow.setRotate(angle);
  }

  private void layoutControls() {
    super.getChildren().addAll(servoShape, label);
  }

  public void setLabel(String label) {
    this.label.setText(label);
  }

  @Override
  public RelayBrick getBrick() {
    return brick;
  }
}
