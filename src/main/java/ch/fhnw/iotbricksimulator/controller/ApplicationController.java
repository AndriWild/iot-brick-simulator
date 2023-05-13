package ch.fhnw.iotbricksimulator.controller;

import ch.fhnw.iotbricksimulator.model.Garden;
import ch.fhnw.iotbricksimulator.model.brick.BrickData;
import ch.fhnw.iotbricksimulator.model.brick.DistanceBrickData;
import ch.fhnw.iotbricksimulator.model.brick.ServoBrickData;
import ch.fhnw.iotbricksimulator.util.Location;
import ch.fhnw.iotbricksimulator.util.mvcbase.ControllerBase;

public class ApplicationController extends ControllerBase<Garden> {
  private final BrickController brickController;
  private final MenuController menuController;

  public ApplicationController(Garden model) {
    super(model);
    brickController = new BrickController(model);
    menuController  = new MenuController(model);
  }

  @Override
  public void awaitCompletion() {
    super.awaitCompletion();
    brickController.awaitCompletion();
    menuController .awaitCompletion();
  }

  @Override
  public void shutdown() {
    super.shutdown();
    brickController.shutdown();
    menuController .shutdown();
  }

  // BrickController delegation
  public void move(Location target, BrickData brickData){
    brickController.move(target, brickData);
  }

  public void rotate(double angle, BrickData brickData) {
    brickController.rotate(angle, brickData);
  }

  public void toggleRemoveButtonVisible() {
    brickController.toggleRemoveButtonVisible();
  }

  public void removeBrick(DistanceBrickData data) {
    brickController.removeBrick(data);
  }

  public void removeBrick(ServoBrickData data) {
    brickController.removeBrick(data);
  }

  // Menu Controller delegation
  public void importConfig() {
    menuController.importConfig();
  }

  public void exportConfig(){
    menuController.exportConfig();
  }

  public ServoBrickData addServoBrick(String id, boolean isSimulated) {
    return menuController.addServoBrick(id, isSimulated);
  }

  public DistanceBrickData addDistanceBrick(String id, boolean isSimulated) {
    return menuController.addDistanceBrick(id, isSimulated);
  }

  public void printAllBrickData() {
    menuController.printAllBrickData();
  }
}
