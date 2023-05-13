package ch.fhnw.iotbricksimulator.model;

import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.mqtt.MqttProxy;
import ch.fhnw.iotbricksimulator.model.brick.ServoBrickData;
import ch.fhnw.iotbricksimulator.util.Constants;
import ch.fhnw.iotbricksimulator.util.mvcbase.ObservableValue;
import ch.fhnw.iotbricksimulator.model.brick.DistanceBrickData;

import java.util.Collections;
import java.util.List;

public class Garden {
  public final ObservableValue<List<DistanceBrickData>> sensors = new ObservableValue<>(Collections.emptyList());
  public final ObservableValue<List<ServoBrickData>> actuators  = new ObservableValue<>(Collections.emptyList());
  public final MockProxy mockProxy = MockProxy.fromConfig(Constants.BASE_URL);
  public final MqttProxy mqttProxy = MqttProxy.fromConfig(Constants.BASE_URL);
  public final ObservableValue<Boolean> isLoading           = new ObservableValue<>(false);
  public final ObservableValue<Boolean> removeButtonVisible = new ObservableValue<>(false);
}
