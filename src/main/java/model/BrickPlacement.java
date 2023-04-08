package main.java.model;

import ch.fhnw.imvs.bricks.core.Brick;
import javafx.geometry.Point2D;
import javafx.scene.Group;

public abstract class BrickPlacement extends Group {

  private final double ARROW_LENGTH = 20;

  protected double latitude, longitude, faceAngle;

  public BrickPlacement(double longitude, double latitude, double faceAngle) {
    super();
    this.latitude  = latitude;
    this.longitude = longitude;
    this.faceAngle = faceAngle;
  }

  public abstract Brick getBrick();

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
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
