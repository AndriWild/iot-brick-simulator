package main.java.model;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.core.ProxyGroup;
import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;

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

  private final ProxyGroup proxies;
  private final Proxy mockProxy;
  private final Proxy proxy;

  public FieldModel() {

    proxies = new ProxyGroup();
    mockProxy = MockProxy.fromConfig(BASE_URL);
    proxy = MockProxy.fromConfig(BASE_URL);

    proxies.addProxy(proxy);
    proxies.addProxy(mockProxy);

    actors  = new ArrayList<>();
    sensors = new ArrayList<>();
  }

  private DistanceBrick getMostActiveSensor(List<DistanceBrick> bricks){
    return bricks.stream()
        .sorted(Comparator.comparing(DistanceBrick::getDistance))
        .toList()
        .get(0);
  }

  public DistanceBrick getMostActive() {
    DistanceBrick mostActiveSensor = null;
    proxies.waitForUpdate();
    if(!sensors.isEmpty()) {
      mostActiveSensor = getMostActiveSensor(sensors);
    }
    return mostActiveSensor;
  }

  public List<ServoBrick> getActors() {
    return Collections.unmodifiableList(actors);
  }

  public List<DistanceBrick> getSensors() {
    return Collections.unmodifiableList(sensors);
  }

  public ServoBrick addActor() {
    ServoBrick newBrick = ServoBrick.connect(mockProxy, String.valueOf(actorId++));
    actors.add(newBrick);
    return newBrick;
  }

  public DistanceBrick addSensor() {
    DistanceBrick newBrick = DistanceBrick.connect(mockProxy, String.valueOf(sensorId++));
    sensors.add(newBrick);
    return newBrick;
  }
}
