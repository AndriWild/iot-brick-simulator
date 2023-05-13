package main.java.ch.fhnw.view.brick;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.java.ch.fhnw.controller.BrickController;
import main.java.ch.fhnw.model.brick.BrickData;
import main.java.ch.fhnw.util.Constants;
import main.java.ch.fhnw.util.Location;

import java.util.concurrent.atomic.AtomicReference;

public abstract class BrickPlacement extends Group {

  protected double faceAngle;

  private Button removeBtn;
  private Text   label;
  private Region labelBackground;

  private final BrickController  controller;
  private final BrickData        brickData;

  public BrickPlacement(BrickController controller, BrickData brick, Runnable removeMe) {
    super();
    this.controller = controller;
    this.brickData  = brick;
    initializeControls(removeMe);
    layoutControls();
    initializeMouseListeners();
  }

  private void initializeMouseListeners() {
    addDragNDropSupport();

    this.setOnMouseEntered(event -> {
      this.toFront();
      super.getChildren().addAll(labelBackground, label);
    });
    this.setOnMouseExited (event -> super.getChildren().removeAll(labelBackground, label));

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
      Location brickLocation = new Location(
          Constants.WINDOW_WIDTH - (bp.getLayoutY() + offsetY), // mirroring the y-axis
          bp.getLayoutX() + offsetX
      );
      controller.move(brickLocation,brickData);
    });
  }

  private void layoutControls() {
    int margin = 5;
    int gap    = 12;
    label.relocate          (BrickNode.WIDTH_BRICK + gap,          - BrickNode.HEIGHT_BRICK + gap);
    labelBackground.relocate(BrickNode.WIDTH_BRICK + gap - margin, - BrickNode.HEIGHT_BRICK + gap - margin);

    super.getChildren().add(removeBtn);
  }

  private void initializeControls(Runnable removeMe) {
    this.faceAngle = 0;
    this.setCursor(Cursor.HAND);

    labelBackground = new Region();
    labelBackground.setMinHeight(90);
    labelBackground.setMinWidth(105);
    BackgroundFill bgFill = new BackgroundFill(Color.rgb(255,255,255, 0.5), new CornerRadii(5), null);
    labelBackground.setBackground(new Background(bgFill));

    label = new Text();
    label.setFont(Font.font("SourceCodePro", FontWeight.NORMAL, 12));

    removeBtn = new Button("Del");
    removeBtn.relocate(-30,-30);
    removeBtn.setOnAction(e -> {
      removeMe.run();
    });
  }

  public void setLabel(String label) {
    this.label.setText(label);
  }

  public void setRemoveBtnVisible(boolean isVisible){
    if(isVisible) removeBtn.toFront();
    removeBtn.setVisible(isVisible);
  }

  public abstract BrickData getBrick();

  public abstract void setRotateBrickSymbol(double angel);

  public double getFaceAngle() {
    return faceAngle;
  }
}