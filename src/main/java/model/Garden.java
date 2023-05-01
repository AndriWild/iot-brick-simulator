package main.java.model;

import ch.fhnw.imvs.bricks.core.Brick;
import main.java.util.mvcbase.ObservableArray;
import main.java.util.mvcbase.ObservableValue;

import java.util.Collections;
import java.util.List;

public class Garden {
  public final ObservableValue<Integer> id = new ObservableValue<>(1);
  public final ObservableArray<Brick> bricks = new ObservableArray<>(new Brick[10]);
  public final ObservableValue<List<Brick>> bricksList = new ObservableValue<>(Collections.emptyList());

}
