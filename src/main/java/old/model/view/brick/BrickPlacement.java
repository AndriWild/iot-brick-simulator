package main.java.old.model.view.brick;

import ch.fhnw.imvs.bricks.core.Brick;
import javafx.scene.Cursor;
import javafx.scene.Group;
import main.java.model.DistanceBrickData;

import java.util.concurrent.atomic.AtomicReference;

public abstract class BrickPlacement extends Group {

  protected double yPos, xPos, faceAngle;

  public BrickPlacement(double xPos, double yPos, double faceAngle) {
    super();
    this.xPos = xPos;
    this.yPos = yPos;
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
      this.xPos = this.xPos + offsetX;
      this.yPos = this.yPos - offsetY;
    });
  }

  public abstract DistanceBrickData getBrick();

  public double getXPos() {
    return xPos;
  }

  public double getYPos() {
    return yPos;
  }

  public double getFaceAngle() {
    return faceAngle;
  }
}