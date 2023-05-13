package main.java.ch.fhnw.controller;

import main.java.ch.fhnw.model.Garden;
import main.java.ch.fhnw.model.brick.BrickData;
import main.java.ch.fhnw.model.brick.DistanceBrickData;
import main.java.ch.fhnw.model.brick.ServoBrickData;
import main.java.ch.fhnw.util.Location;
import main.java.ch.fhnw.util.mvcbase.ControllerBase;

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

  public void addServoBrick(boolean isSimulated, String id) {
    menuController.addServoBrick(isSimulated, id);
  }

  public void addDistanceBrick(boolean isSimulated, String id) {
    menuController.addDistanceBrick(isSimulated, id);
  }

  public void printAllBrickData() {
    menuController.printAllBrickData();
  }
}
