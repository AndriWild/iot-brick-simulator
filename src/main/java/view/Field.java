package main.java.view;

import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.java.model.BrickShape;
import main.java.model.DistancePlacement;
import main.java.model.ServoPlacement;
import main.java.presentation.PresentationModel;

import java.util.ArrayList;
import java.util.List;

public class Field extends Pane {

  private List<ServoPlacement> actorBricks;
  private List<DistancePlacement> sensorBricks;
  private Button btn;
  private Group legend;

  public Field() {
    initializeControls();
    layoutControls();
  }

  private void layoutControls() {
    this.getChildren().addAll(actorBricks);
    this.getChildren().addAll(sensorBricks);
//    this.getChildren().add(btn);
    this.getChildren().add(legend);
  }

  private void updateControls(){
    this.getChildren().clear();
    layoutControls();
  }
  private void initializeControls() {
    PresentationModel pm = PresentationModel.getInstance();
    actorBricks  = new ArrayList<>(pm.getActorPlacement());
    sensorBricks = new ArrayList<>(pm.getSensorPlacement());

    pm.getActorPlacement().addListener(
        (ListChangeListener<ServoPlacement>) change -> {
          actorBricks = pm.getActorPlacement();
          updateControls();
        }
    );

    pm.getSensorPlacement().addListener(
        (ListChangeListener<DistancePlacement>) change -> {
          sensorBricks = pm.getSensorPlacement();
          updateControls();
        }
    );

    btn = new Button("Add Brick");
    btn.setOnAction(event -> PresentationModel.getInstance().addBrick());

    int gap = 25;
    int legendWidth  = 160;
    int legendHeight =  80;

    Rectangle legendBorder = new Rectangle(legendWidth, legendHeight);
    legendBorder.setFill(Color.WHITE);

    DistancePlacement dummyDistance = new DistancePlacement(null, gap,                          gap, 0);
    ServoPlacement    dummyServo    = new ServoPlacement   (null, gap + BrickShape.WIDTH + gap, gap, 0);

    Text labelDistance = new Text(gap,                              gap + 45, "Sensor");
    Text labelServo    = new Text(gap + BrickShape.WIDTH + gap + 10,gap + 45, "Actor");
    Text frontLabel    = new Text(gap + BrickShape.WIDTH - 5,       gap - 5,  "Front");

    legend = new Group(legendBorder, dummyDistance, dummyServo, labelDistance, labelServo, frontLabel);
    legend.setLayoutX(pm.getWindowSize().width - legendWidth);
    legend.setLayoutY(pm.getWindowSize().height - legendHeight);
  }
}
