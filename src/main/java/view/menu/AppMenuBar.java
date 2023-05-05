package main.java.view.menu;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import main.java.controller.GardenController;

public class AppMenuBar extends MenuBar {

  private Menu menu;
  private MenuItem addBrick;
  private MenuItem shutdown;

  private final GardenController controller;

  public AppMenuBar(GardenController controller, Stage stage, Runnable shutdownCallback) {
    this.controller = controller;
    initializeControls();
    layoutControls();
    initializeListeners(stage, shutdownCallback);
  }

  private void initializeListeners(Stage stage, Runnable shutdownCallback) {
    addBrick.setOnAction(e -> {
      Stage dialog     = new Stage();
      Scene popUpScene = new Scene(new Controls(controller, dialog::close), 350, 450);
      dialog.setScene(popUpScene);
      dialog.initOwner(stage);
      dialog.showAndWait();
    });

    shutdown.setOnAction(e -> {
      shutdownCallback.run();
      stage.close();
      Platform.exit();
      System.exit(0);
    });
  }

  private void initializeControls() {
    menu = new Menu("Menu");
    addBrick = new MenuItem("Add Brick");
    shutdown = new MenuItem("Close");
  }

  private void layoutControls() {
    menu.getItems().addAll(addBrick, shutdown);
    this.getMenus().add(menu);
  }
}
