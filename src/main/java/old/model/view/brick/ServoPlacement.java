package main.java.old.model.view.brick;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import main.java.old.model.presentation.PresentationModel;

public class ServoPlacement extends BrickPlacement {

  private final ServoBrick brick;
  private Group  servoShape;
  private Text   label;
  private Rotate mostActiveSensorAngle;
  private Rotate frontViewAngle;

  public ServoPlacement(ServoBrick brick, double longitude, double latitude, double faceAngle) {
    super(longitude, latitude, faceAngle);
    this.brick = brick;

    initializeControls();
    layoutControls();
  }

  private void initializeControls() {
    int windowHeight = PresentationModel.getInstance().getWindowSize().height;

    System.out.println("ServoPlacement.initializeControls " + Thread.currentThread());
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

    Circle outerCircle = new Circle(BrickShape.CENTER_X, BrickShape.CENTER_Y, BrickShape.CENTER_X - 2);
    Circle innerCircle = new Circle(BrickShape.CENTER_X, BrickShape.CENTER_Y, BrickShape.CENTER_X - 10);
    innerCircle.setFill(Color.LIGHTGRAY);
    innerCircle.setStroke(Color.BLACK);
    outerCircle.setFill(Color.GREY);
    outerCircle.setStroke(Color.BLACK);

    BrickShape brickIcon = new BrickShape(Color.BLUE);
    servoShape = new Group(brickIcon, outerCircle, mostActiveSensorIndicator, innerCircle, frontViewIndicator);
    servoShape.setLayoutX(xPos);
    servoShape.setLayoutY(windowHeight - yPos);
    servoShape.setRotate(faceAngle);

    this.setOnScroll( e -> {

      if(e.getDeltaY() > 0){
        this.faceAngle += 2;
      }
      if(e.getDeltaY() < 0) {
        this.faceAngle -= 2;
      }
      servoShape.setRotate(faceAngle);
    });

    label = new Text();
    label.relocate(this.xPos + BrickShape.WIDTH + 10, windowHeight - this.yPos - BrickShape.HEIGHT);
  }

  public void setMostActiveSensorAngle(double angle) {
    mostActiveSensorAngle.setAngle(angle);
  }

  public double getMostActiveSensorAngle() {
    return mostActiveSensorAngle.getAngle();
  }

  public void adjustServoPosition(int newPosition){
   this.brick.setPosition(newPosition);
  }

  public void setFrontViewAngle(double angle) {
    frontViewAngle.setAngle(angle);
  }

  private void layoutControls() {
    this.setOnMouseEntered(event -> super.getChildren().add(label));
    this.setOnMouseExited(event -> super.getChildren().remove(label));
    super.getChildren().addAll(servoShape);
  }

  public void setLabel(String label) {
    this.label.setText(label);
  }

  @Override
  public ServoBrick getBrick() {
    return brick;
  }
}
