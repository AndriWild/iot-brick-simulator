package main.java.view;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.java.model.Brick;
import main.java.presentation.PresentationModel;

import java.util.ArrayList;
import java.util.List;

public class Field extends Pane {

  private List<Brick> bricks;
  private Button btn;

  public Field() {
    initializeControls();
    layoutControls();
  }

  private void layoutControls() {
    this.getChildren().addAll(bricks);
    this.getChildren().add(btn);
  }

  private void updateControls(){
    this.getChildren().clear();
    layoutControls();
  }
  private void initializeControls() {
    PresentationModel pm = PresentationModel.getInstance();
    bricks = new ArrayList<>(pm.getBricks());

    pm.getBricks().addListener(
        (ListChangeListener<Brick>) change -> {
          bricks = pm.getBricks();
          updateControls();
        }
    );

    btn = new Button("Add Brick");
    btn.setOnAction(event -> PresentationModel.getInstance().addBrick());
  }
}
