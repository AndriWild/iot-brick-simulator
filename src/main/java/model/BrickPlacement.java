package main.java.model;

import ch.fhnw.imvs.bricks.core.Brick;
import javafx.geometry.Point2D;
import javafx.scene.Group;

public abstract class BrickPlacement extends Group {

  private final double ARROW_LENGTH = 20;

  protected double x, y, faceAngle;

  public BrickPlacement(double x, double y, double faceAngle) {
    super();
    this.x = x;
    this.y = y;
    this.faceAngle = faceAngle;
  }

  public abstract Brick getBrick();

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getFaceAngle() {
    return faceAngle;
  }

  private Point2D calcGlanceDirectionFromAngle(double x0, double y0, double angle){
    double x = ARROW_LENGTH * Math.cos(angle) + x0;
    double y = ARROW_LENGTH * Math.sin(angle) + y0;
    return new Point2D(x, y);
  }
}
