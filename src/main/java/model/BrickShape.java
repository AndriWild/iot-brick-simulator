package main.java.model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class BrickShape extends Group {

  public static final double CENTER_X = BrickShape.WIDTH / 2;
  public static final double CENTER_Y = BrickShape.HEIGHT / 2;
  public static final double HEIGHT   = 30;
  public static final double WIDTH    = 48;

  private final Color     color;
  private       Line      frontIndicator;
  private       Rectangle body;

  public BrickShape(Color color){
    this.color = color;
    initializeControls();
    layoutControls();
  }

  private void layoutControls() {
    this.getChildren().addAll(body, frontIndicator);
  }

  private void initializeControls() {
    frontIndicator = new Line(
        CENTER_X,
        0,
        CENTER_X,
        - BrickShape.HEIGHT / 2
    );
    body = new Rectangle(WIDTH, HEIGHT);

    frontIndicator.setFill(Color.BLACK);
    body          .setFill(color);
  }
}
