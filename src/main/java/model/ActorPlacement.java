package main.java.model;

import ch.fhnw.imvs.bricks.core.Brick;
import javafx.scene.paint.Color;

public class ActorPlacement extends BrickPlacement {

  public ActorPlacement(Brick brick, double x, double y, double angle) {
    super(x, y, angle, Color.BLUE);
  }
}
