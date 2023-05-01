package main.java.controller;

import ch.fhnw.imvs.bricks.core.Brick;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import main.java.model.Garden;
import main.java.old.model.view.brick.DistancePlacement;
import main.java.util.mvcbase.ControllerBase;

import java.util.Collections;
import java.util.List;

public class GardenController extends ControllerBase<Garden> {


  public GardenController(Garden model) {
    super(model);
  }

  public void increase() {
    updateModel(increase(model.id));
  }

  public void addBrickToArray() {
    List<Brick> bs = model.bricksList.getValue();
    bs.add(DistanceBrick.connect(null,""));
    updateModel(set(model.bricksList, bs));
  }
}
