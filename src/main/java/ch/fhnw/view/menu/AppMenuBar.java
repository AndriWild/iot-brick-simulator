package main.java.ch.fhnw.view.menu;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.ch.fhnw.controller.ApplicationController;
import main.java.ch.fhnw.controller.MenuController;

public class AppMenuBar extends MenuBar {

  private Menu     menu;
  private MenuItem addBrick;
  private MenuItem printBrickData;
  private MenuItem exportConfig;
  private MenuItem importConfig;
  private MenuItem shutdown;

  private final ApplicationController controller;

  public AppMenuBar(ApplicationController controller, Stage stage, Runnable shutdownCallback) {
    this.controller = controller;
    initializeControls();
    layoutControls();
    initializeListeners(stage, shutdownCallback);
  }

  private void initializeListeners(Stage stage, Runnable shutdownCallback) {
    addBrick.setOnAction(_e -> {
      Stage dialog     = new Stage();
      Scene popUpScene = new Scene(new Controls(controller, dialog::close), 350, 450);
      dialog.setScene(popUpScene);
      dialog.initOwner(stage);
      dialog.showAndWait();
    });

    printBrickData.setOnAction(_e -> controller.printAllBrickData());

    exportConfig.setOnAction(_e -> controller.exportConfig());
    importConfig.setOnAction(_e -> controller.importConfig());

    shutdown.setOnAction(_e -> {
      shutdownCallback.run();
      stage.close();
      Platform.exit();
      System.exit(0);
    });
  }

  private void initializeControls() {
    menu           = new Menu    ("Menu");
    addBrick       = new MenuItem("Add Brick");
    printBrickData = new MenuItem("Print Brick Data");
    exportConfig   = new MenuItem("Export");
    importConfig   = new MenuItem("Import");
    shutdown       = new MenuItem("Close");
  }

  private void layoutControls() {
    SeparatorMenuItem separator = new SeparatorMenuItem();
    menu.getItems().addAll(
        addBrick,
        printBrickData,
        exportConfig,
        importConfig,
        separator,
        shutdown
    );
    this.getMenus().add(menu);
  }
}
