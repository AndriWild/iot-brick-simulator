package main.java.controller;

import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import main.java.model.DistanceBrickData;
import main.java.model.Garden;
import main.java.util.mvcbase.ControllerBase;
import main.java.util.mvcbase.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public class GardenController extends ControllerBase<Garden> {

  private final Proxy proxy;

  public GardenController(Garden model) {
    super(model);
    proxy = MockProxy.fromConfig("");
    updateLoop();
  }


  public void increase() {
    updateModel(increase(model.id));
  }

  private void updateLoop() {
    new Thread(() -> {
      while(true) {
        proxy.waitForUpdate();
        model.bricksList.getValue().forEach(b -> {
          updateModel(set(b.value, b.getDistance()));
        });
      }
    }).start();
  }

  public void addBrickToArray() {
    List<DistanceBrickData> bs;
    bs = new ArrayList<>(model.bricksList.getValue());
    DistanceBrickData db = new DistanceBrickData(DistanceBrick.connect(proxy,""));
    bs.add(db);
    updateModel(set(model.bricksList, bs));
  }
}
