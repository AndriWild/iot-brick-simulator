package main.java.model;

import ch.fhnw.imvs.bricks.core.Brick;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public abstract class BrickPlacement extends Group {

  private final double ARROW_LENGTH = 20;

  private Line faceAngle;
  private Circle body;
  protected double x, y;

  public BrickPlacement(double x, double y, double angle, Color color) {
    super();
    this.x = x;
    this.y = y;
    initializeControls(x, y, angle, color);
    layoutControls();
  }

  public abstract Brick getBrick();

  private void initializeControls(double x, double y, double angle, Color color) {
    body      = new Circle(x, y, 5);
    faceAngle = new Line();

    Point2D endPoint = calcGlanceDirectionFromAngle(x, y, Math.toRadians(angle));
    faceAngle.setStartX(x);
    faceAngle.setStartY(y);
    faceAngle.setEndX(endPoint.getX());
    faceAngle.setEndY(endPoint.getY());

    body     .setFill(color);
    faceAngle.setFill(color);
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
