package main.java.model;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.core.ProxyGroup;
import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.mqtt.MqttProxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static main.java.model.Constants.BASE_URL;

public class FieldModel {

  private static int actorId  = 0;
  private static int sensorId = 0;

  private final List<ServoBrick>    actors;
  private final List<DistanceBrick> sensors;

  private final SimpleBooleanProperty refresh;

  private final ProxyGroup proxies;
  private final Proxy      mockProxy;
  private final Proxy      mqttProxy;

  public FieldModel() {

    proxies   = new ProxyGroup();
    mockProxy = MockProxy.fromConfig(BASE_URL);
    mqttProxy = MqttProxy.fromConfig(BASE_URL);

    proxies.addProxy(mqttProxy);
    proxies.addProxy(mockProxy);

    actors  = new ArrayList<>();
    sensors = new ArrayList<>();
    refresh = new SimpleBooleanProperty(false);
    updateLoop();
  }

  private void updateLoop() {
    new Thread(() -> {
      while(true){
        proxies.waitForUpdate();
        refresh.set(!refresh.get());
      }
    }).start();
  }

  public SimpleBooleanProperty refreshProperty() {
    return refresh;
  }

  public DistanceBrick getMostActiveSensor(){
    if(sensors.isEmpty()) return null;
    return sensors.stream()
        .sorted(Comparator.comparing(DistanceBrick::getDistance))
        .toList()
        .get(0);
  }

  public List<ServoBrick> getActors() {
    return Collections.unmodifiableList(actors);
  }

  public List<DistanceBrick> getSensors() {
    return Collections.unmodifiableList(sensors);
  }

  public ServoBrick addSimulatedActor() {
    ServoBrick newBrick = ServoBrick.connect(mockProxy, "auto gen " + actorId++);
    actors.add(newBrick);
    return newBrick;
  }

  public DistanceBrick addSimulatedSensor() {
    DistanceBrick newBrick = DistanceBrick.connect(mockProxy, "auto gen " + sensorId++);
    sensors.add(newBrick);
    return newBrick;
  }

  public DistanceBrick addMqttSensor(String id) {
    DistanceBrick newBrick = DistanceBrick.connect(mqttProxy, id);
    sensors.add(newBrick);
    return newBrick;
  }

  public ServoBrick addMqttActor(String id) {
    ServoBrick newBrick = ServoBrick.connect(mqttProxy, id);
    actors.add(newBrick);
    return newBrick;
  }
}
