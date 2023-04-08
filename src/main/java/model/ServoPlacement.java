package main.java.model;

import ch.fhnw.imvs.bricks.actuators.RelayBrick;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import main.java.presentation.PresentationModel;

public class ServoPlacement extends BrickPlacement {

  private final RelayBrick brick;
  private Group  servoShape;
  private Text   label;
  private Rotate mostActiveSensorAngle;
  private Rotate frontViewAngle;

  public ServoPlacement(RelayBrick brick, double longitude, double latitude, double faceAngle) {
    super(longitude, latitude, faceAngle);
    this.brick = brick;

    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    int windowHeight = PresentationModel.getInstance().getWindowSize().height;

    Line mostActiveSensorIndicator = new Line(BrickShape.CENTER_X, BrickShape.CENTER_Y, BrickShape.CENTER_X, -5);
    mostActiveSensorAngle = new Rotate();
    mostActiveSensorAngle.setPivotX(BrickShape.CENTER_X);
    mostActiveSensorAngle.setPivotY(BrickShape.CENTER_Y);
    mostActiveSensorIndicator.getTransforms().addAll(mostActiveSensorAngle);
    mostActiveSensorIndicator.setStrokeWidth(2);

    Line frontViewIndicator = new Line(BrickShape.CENTER_X, BrickShape.CENTER_Y, BrickShape.CENTER_X, 5);
    frontViewAngle = new Rotate();
    frontViewAngle.setPivotX(BrickShape.CENTER_X);
    frontViewAngle.setPivotY(BrickShape.CENTER_Y);
    frontViewIndicator.getTransforms().addAll(frontViewAngle);
    frontViewIndicator.setStrokeWidth(2);

    Circle outerCircle = new Circle(BrickShape.CENTER_X, BrickShape.CENTER_Y, BrickShape.CENTER_X - 3);
    Circle innerCircle = new Circle(BrickShape.CENTER_X, BrickShape.CENTER_Y, BrickShape.CENTER_X - 12);
    innerCircle.setFill(Color.LIGHTGRAY);
    innerCircle.setStroke(Color.BLACK);
    outerCircle.setFill(Color.GREY);
    outerCircle.setStroke(Color.BLACK);

    BrickShape brickIcon = new BrickShape(Color.BLUE);
    servoShape = new Group(brickIcon, outerCircle, mostActiveSensorIndicator, innerCircle, frontViewIndicator);
    servoShape.setLayoutX(longitude);
    servoShape.setLayoutY(windowHeight - latitude);
    servoShape.setRotate(faceAngle);

    label = new Text();
    label.relocate(this.longitude - 15, windowHeight - this.latitude + BrickShape.WIDTH);
  }

  public void setMostActiveSensorAngle(double angle) {
    mostActiveSensorAngle.setAngle(angle);
  }

  public void setFrontViewAngle(double angle) {
    frontViewAngle.setAngle(angle);
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
