package main.java.view;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import main.java.presentation.PresentationModel;

import java.awt.*;

import static main.java.model.Constants.*;

public class Grid extends Pane {

  public static final int GAP = 50;


  private static final double DECIMALS_FACTOR  =  10e4;

  public Grid (PresentationModel pm){
    int windowHeight = PresentationModel.getInstance().getWindowSize().height;
    drawGrid(pm.getWindowSize(), windowHeight);
  }

  private void drawGrid(Dimension dimension, int windowHeight){
    double start = 0, end = dimension.width;
    int nofLines = windowHeight / GAP;
    double coordinateCapLon = (RIGHT_LONG - LEFT_LONG) / nofLines;
    double coordinateCapLat = (TOP_LAT - BOTTOM_LAT)   / nofLines;


    for (int i = 1; i <= nofLines; i++) {
      Line horizontalLine = new Line(start, windowHeight - (i * GAP), end, windowHeight - (i * GAP));
      Line verticalLine   = new Line(i * GAP, start, i * GAP, end);
      horizontalLine.setStrokeWidth(0.2);
      verticalLine  .setStrokeWidth(0.2);

      drawVerticalLine(windowHeight, coordinateCapLon, i, verticalLine);
      drawHorizontalLine(coordinateCapLat, i, horizontalLine);
    }
  }

  private void drawHorizontalLine(double coordinateCap, int i, Line horizontalLine) {
    this.getChildren().add(
        new Group(
            new Text(
                horizontalLine.getStartX() + 5,
                horizontalLine.getStartY() - 5,
                String.valueOf(Math.round((BOTTOM_LAT + coordinateCap * i) * DECIMALS_FACTOR) / DECIMALS_FACTOR)
            ),
            horizontalLine
        )
    );
  }

  private void drawVerticalLine(int windowHeight, double coordinateCap, int i, Line verticalLine) {
    Text label = new Text(
        verticalLine.getStartX() - 15,
        windowHeight - 20,
        String.valueOf(Math.round((LEFT_LONG + coordinateCap * i) * DECIMALS_FACTOR) / DECIMALS_FACTOR)
    );
    label.setRotate(-90);
    this.getChildren().add(
        new Group(
            label,
            verticalLine
        )
    );
  }
}
