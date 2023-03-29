package main.java.model;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Brick extends Group {

  private final double ARROW_LENGTH = 20;

  private Line faceAngle;
  private Circle body;
  private double activity;

  public Brick(double x, double y, double angle, double activity) {
    super();
    this.activity = activity;

    initializeControls(x, y, angle);
    layoutControls();
  }

  private void initializeControls(double x, double y, double angle) {
    body      = new Circle(x, y, 5);
    faceAngle = new Line();

    Point2D endPoint = calcGlanceDirectionFromAngle(x, y, Math.toRadians(angle));
    faceAngle.setStartX(x);
    faceAngle.setStartY(y);
    faceAngle.setEndX(endPoint.getX());
    faceAngle.setEndY(endPoint.getY());

    body     .setFill(Color.BLUE);
    faceAngle.setFill(Color.BLUE);
  }

  private void layoutControls() {
    this.getChildren().addAll(faceAngle, body);
  }

  private Point2D calcGlanceDirectionFromAngle(double x0, double y0, double angle){
    double x = ARROW_LENGTH * Math.cos(angle) + x0;
    double y = ARROW_LENGTH * Math.sin(angle) + y0;
    return new Point2D(x, y);
  }
}
