package main.java.model;

import main.java.model.brick.DistanceBrickData;
import main.java.model.brick.ServoBrickData;
import main.java.util.mvcbase.ObservableValue;

import java.util.Collections;
import java.util.List;

public class Garden {
  public final ObservableValue<List<DistanceBrickData>> sensors = new ObservableValue<>(Collections.emptyList());
  public final ObservableValue<List<ServoBrickData>> actuators  = new ObservableValue<>(Collections.emptyList());
}
