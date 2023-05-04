package main.java.model;

import ch.fhnw.imvs.bricks.core.Brick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import main.java.util.mvcbase.ObservableArray;
import main.java.util.mvcbase.ObservableValue;

import java.util.Collections;
import java.util.List;

public class Garden {
  public final ObservableValue<Integer> id = new ObservableValue<>(1);
  public final ObservableValue<List<DistanceBrickData>> bricksList = new ObservableValue<>(Collections.emptyList());
}
