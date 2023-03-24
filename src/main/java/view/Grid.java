package main.java.view;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import main.java.presentation.PresentationModel;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class Grid extends Pane {

  public static final int GAP = 50;

  public Grid (PresentationModel pm){

    var linesH = getHorizontalLines(pm.getWindowSize(), GAP);
    var linesV = getVerticalLines(pm.getWindowSize(),   GAP);
    var g = new Group();
    g.getChildren().addAll(linesH);
    g.getChildren().addAll(linesV);
    this.getChildren().addAll(g);
  }

  private List<Group> getVerticalLines(Dimension dimension, int gap){
    double start = 0, end = dimension.height;
    int nofLines = (dimension.width / gap);
    return IntStream
        .range(1, nofLines)
        .mapToObj(n -> new Line(n * gap, start, n * gap, end))
        .map(l -> new Group(
            new Text(l.getStartX() + 5 , l.getStartY() + 15, "" + (int) l.getStartX()),
            l)
        )
        .toList();
  }
  private List<Group> getHorizontalLines(Dimension dimension, int gap){
    double start = 0, end = dimension.width;
    int nofLines = dimension.height / gap;
    return IntStream
        .range(1, nofLines)
        .mapToObj(n -> new Line(start, n * gap, end, n * gap))
        .map(l -> new Group(
            new Text(l.getStartX() + 5 , l.getStartY() - 5, "" + (int) l.getStartY()),
            l)
        )
        .toList();
  }

}
