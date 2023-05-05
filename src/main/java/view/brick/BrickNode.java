package main.java.view.brick;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class BrickNode extends Group {

  public static final double CENTER_X = BrickNode.WIDTH / 2;
  public static final double CENTER_Y = BrickNode.HEIGHT / 2;
  public static final double HEIGHT   = 22;
  public static final double WIDTH    = 34;

  private final Color     color;
  private       Line      frontIndicator;
  private       Rectangle body;


  public BrickNode(Color color){
    this.color = color;
    initializeControls();
    layoutControls();
  }

  public Rectangle getBody() {
    return body;
  }


  private void layoutControls() {
    this.getChildren().addAll(body, frontIndicator);
  }

  private void initializeControls() {
    frontIndicator = new Line(
        CENTER_X,
        0,
        CENTER_X,
        -BrickNode.HEIGHT / 2
    );
    body = new Rectangle(WIDTH, HEIGHT);
    body.setArcHeight(5.0);
    body.setArcWidth (5.0);

    frontIndicator.setFill(Color.BLACK);
    body          .setFill(color);

  }
}