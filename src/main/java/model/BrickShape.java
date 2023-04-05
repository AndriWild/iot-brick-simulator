package main.java.model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class BrickShape extends Group {

  public static final double INDICATOR_RADIUS =  3;
  public static final double HEIGHT           = 30;
  public static final double WIDTH            = 48;

  private final Color     color;
  private       Circle    frontIndicator;
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
    frontIndicator = new Circle(WIDTH - INDICATOR_RADIUS, INDICATOR_RADIUS, INDICATOR_RADIUS);
    body           = new Rectangle(WIDTH, HEIGHT);

    frontIndicator.setFill(Color.BLACK);
    body          .setFill(color);
  }
}
