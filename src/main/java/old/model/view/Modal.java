package main.java.old.model.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Modal extends Pane {

  private Button openDialog;

  public Modal(Stage stage) {
    initializeControls(stage);
    layoutControls();
  }

  private void layoutControls() {
    openDialog.relocate(700, 10);
    this.getChildren().addAll(openDialog);
  }

  private void initializeControls(Stage stage) {
    openDialog = new Button("Menu");
    openDialog.setOnAction(e -> {
      Stage dialog     = new Stage();
      Scene popUpScene = new Scene(new Controls(dialog::close), 350, 450);
      dialog.setScene(popUpScene);
//      dialog.initOwner(stage);
      dialog.showAndWait();
    });
  }
}