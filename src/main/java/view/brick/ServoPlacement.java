package main.java.view.brick;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import main.java.controller.GardenController;
import main.java.model.brick.ServoBrickData;

public class ServoPlacement extends BrickPlacement {

  private final ServoBrickData brick;
  private Group  servoShape;
  private Rotate mostActiveSensorAngle;
  private Rotate frontViewAngle;

  public ServoPlacement(GardenController controller, ServoBrickData brick) {
    super(controller, brick);
    this.brick = brick;

    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    Line mostActiveSensorIndicator = new Line(BrickNode.CENTER_X, BrickNode.CENTER_Y, BrickNode.CENTER_X, -5);
    mostActiveSensorAngle = new Rotate();
    mostActiveSensorAngle.setPivotX(BrickNode.CENTER_X);
    mostActiveSensorAngle.setPivotY(BrickNode.CENTER_Y);
    mostActiveSensorIndicator.getTransforms().addAll(mostActiveSensorAngle);
    mostActiveSensorIndicator.setStrokeWidth(2);

    Line frontViewIndicator = new Line(BrickNode.CENTER_X, BrickNode.CENTER_Y, BrickNode.CENTER_X, 5);
    frontViewAngle = new Rotate();
    frontViewAngle.setPivotX(BrickNode.CENTER_X);
    frontViewAngle.setPivotY(BrickNode.CENTER_Y);
    frontViewIndicator.getTransforms().addAll(frontViewAngle);
    frontViewIndicator.setStrokeWidth(2);

    Circle outerCircle = new Circle(BrickNode.CENTER_X, BrickNode.CENTER_Y, BrickNode.CENTER_X - 2);
    Circle innerCircle = new Circle(BrickNode.CENTER_X, BrickNode.CENTER_Y, BrickNode.CENTER_X - 10);
    innerCircle.setFill(Color.LIGHTGRAY);
    innerCircle.setStroke(Color.BLACK);
    outerCircle.setFill(Color.GREY);
    outerCircle.setStroke(Color.BLACK);

    BrickNode brickIcon = new BrickNode(Color.BLUE);
    servoShape = new Group(brickIcon, outerCircle, mostActiveSensorIndicator, innerCircle, frontViewIndicator);
    servoShape.setRotate(faceAngle);
  }

  public void setRotateBrickSymbol(double angel){
    servoShape.setRotate(angel);
  }

  public void setMostActiveSensorAngle(double angle) {
    mostActiveSensorAngle.setAngle(angle);
  }

  public void setFrontViewAngle(double angle) {
    frontViewAngle.setAngle(angle);
  }

  private void layoutControls() {
    super.getChildren().addAll(servoShape);
  }

  @Override
  public  ServoBrickData getBrick() {
    return brick;
  }
}
