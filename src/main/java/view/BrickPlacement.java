package main.java.view;

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

    this.setOnMousePressed(event -> {

      orgSceneX.set(event.getSceneX());
      orgSceneY.set(event.getSceneY());

      BrickPlacement bp = (BrickPlacement) (event.getSource());
      bp.toFront();
    });
    this.setOnMouseDragged(event -> {

      double offsetX = event.getSceneX() - orgSceneX.get();
      double offsetY = event.getSceneY() - orgSceneY.get();
      BrickPlacement bp = (BrickPlacement) (event.getSource());
      bp.setLayoutX(bp.getLayoutX() + offsetX);
      bp.setLayoutY(bp.getLayoutY() + offsetY);
      orgSceneX.set(event.getSceneX());
      orgSceneY.set(event.getSceneY());
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