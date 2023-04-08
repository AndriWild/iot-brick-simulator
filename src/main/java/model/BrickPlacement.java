package main.java.model;

import ch.fhnw.imvs.bricks.core.Brick;
import javafx.scene.Cursor;
import javafx.scene.Group;

import java.util.concurrent.atomic.AtomicReference;

public abstract class BrickPlacement extends Group {

  protected double latitude, longitude, faceAngle;

  public BrickPlacement(double longitude, double latitude, double faceAngle) {
    super();
    this.latitude  = latitude;
    this.longitude = longitude;
    this.faceAngle = faceAngle;
    this.setCursor(Cursor.HAND);

    AtomicReference<Double> orgSceneX = new AtomicReference<>((double) 0);
    AtomicReference<Double> orgSceneY = new AtomicReference<>((double) 0);

    this.setOnMousePressed((t) -> {

      orgSceneX.set(t.getSceneX());
      orgSceneY.set(t.getSceneY());

      BrickPlacement bp = (BrickPlacement) (t.getSource());
      bp.toFront();
    });
    this.setOnMouseDragged((t) -> {

      double offsetX = t.getSceneX() - orgSceneX.get();
      double offsetY = t.getSceneY() - orgSceneY.get();
      BrickPlacement bp = (BrickPlacement) (t.getSource());
      bp.setLayoutX(bp.getLayoutX() + offsetX);
      bp.setLayoutY(bp.getLayoutY() + offsetY);
      orgSceneX.set(t.getSceneX());
      orgSceneY.set(t.getSceneY());
      this.longitude = this.longitude + offsetX;
      this.latitude = this.latitude - offsetY;
    });
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
}