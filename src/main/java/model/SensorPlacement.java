package main.java.model;

import ch.fhnw.imvs.bricks.core.Brick;
import javafx.scene.paint.Color;

public class SensorPlacement extends BrickPlacement {

  public SensorPlacement(Brick brick, double x, double y, double angle) {
    super(x, y, angle, Color.RED);
  }
}
