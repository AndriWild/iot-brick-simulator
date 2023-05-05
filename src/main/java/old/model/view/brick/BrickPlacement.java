package main.java.old.model.view.brick;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.text.Text;
import main.java.controller.GardenController;
import main.java.model.brick.BrickData;
import main.java.old.model.Constants;

import java.util.concurrent.atomic.AtomicReference;

public abstract class BrickPlacement extends Group {

  protected double faceAngle;

  private       Text             label;
  private final GardenController controller;
  private final BrickData        brickData;

  public BrickPlacement(GardenController controller, BrickData brick) {
    super();
    this.controller = controller;
    this.brickData  = brick;
    initializeControls();
    layoutControls();
    initializeMouseListeners();
  }

  private void initializeMouseListeners() {
    addDragNDropSupport();

    this.setOnMouseEntered(event -> super.getChildren().add(label));
    this.setOnMouseExited (event -> super.getChildren().remove(label));

    this.setOnScroll( e -> {
      int dAngle = 0;
      // need to be in separate if statements to work properly
      if(e.getDeltaY() < 0) dAngle = -2;
      if(e.getDeltaY() > 0) dAngle =  2;
      controller.rotate(brickData.faceAngle.getValue() + dAngle, brickData);
    });
  }

  private void addDragNDropSupport(){
    AtomicReference<Double> orgSceneX = new AtomicReference<>(0d);
    AtomicReference<Double> orgSceneY = new AtomicReference<>(0d);

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
      orgSceneX.set(event.getSceneX());
      orgSceneY.set(event.getSceneY());
      controller.move(bp.getLayoutX() + offsetX, Constants.WINDOW_WIDTH - (bp.getLayoutY() + offsetY), brickData);
    });
  }

  private void layoutControls() {
    label.relocate(BrickNode.WIDTH + 5, -BrickNode.HEIGHT + 5);
  }

  private void initializeControls() {
    this.faceAngle = 0;
    this.setCursor(Cursor.HAND);
    label = new Text();
  }

  public void setLabel(String label) {
    this.label.setText(label);
  }

  public abstract BrickData getBrick();

  public abstract void setRotateBrickSymbol(double angel);

  public double getFaceAngle() {
    return faceAngle;
  }
}