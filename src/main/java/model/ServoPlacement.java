package main.java.model;

import ch.fhnw.imvs.bricks.actuators.RelayBrick;
import javafx.scene.paint.Color;

public class ServoPlacement extends BrickPlacement {

  private RelayBrick brick;

  public ServoPlacement(RelayBrick brick, double x, double y, double angle) {
    super(x, y, angle, Color.BLUE);
    this.brick = brick;
  }

  @Override
  public RelayBrick getBrick() {
    return brick;
  }
}
