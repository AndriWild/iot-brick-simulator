package main.java.model;

import ch.fhnw.imvs.bricks.core.ProxyGroup;
import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.mqtt.MqttProxy;
import main.java.Constants;
import main.java.model.brick.DistanceBrickData;
import main.java.model.brick.ServoBrickData;
import main.java.util.mvcbase.ObservableValue;

import java.util.Collections;
import java.util.List;

public class Garden {
  public final ObservableValue<List<DistanceBrickData>> sensors = new ObservableValue<>(Collections.emptyList());
  public final ObservableValue<List<ServoBrickData>> actuators  = new ObservableValue<>(Collections.emptyList());
  public final MockProxy mockProxy = MockProxy.fromConfig(Constants.BASE_URL);
  public final MqttProxy mqttProxy = MqttProxy.fromConfig(Constants.BASE_URL);
}
