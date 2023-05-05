package main.java.controller;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.core.ProxyGroup;
import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.mqtt.MqttProxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import main.java.model.brick.BrickData;
import main.java.model.brick.DistanceBrickData;
import main.java.model.Garden;
import main.java.model.brick.ServoBrickData;
import main.java.old.model.Constants;
import main.java.util.Util;
import main.java.util.mvcbase.ControllerBase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GardenController extends ControllerBase<Garden> {

  private final Proxy mockProxy;
  private final Proxy mqttProxy;
  private final ProxyGroup proxyGroup;

  private int actuatorIdCounter = 0;
  private int sensorIdCounter   = 0;


  public GardenController(Garden model) {
    super(model);
    mockProxy = MockProxy.fromConfig(Constants.BASE_URL);
    mqttProxy = MqttProxy.fromConfig(Constants.BASE_URL);

    proxyGroup = new ProxyGroup();
    proxyGroup.addProxy(mockProxy);
    proxyGroup.addProxy(mqttProxy);

    updateLoop();
  }

  private void updateLoop() {
    new Thread(() -> {
      while(true) {
        mockProxy.waitForUpdate();
        model.sensors.getValue().forEach(b -> {
          updateModel(set(b.value, b.getDistance()));
          updateModel(set(b.isMostActive, false));
        });
        updateActorPositions(model.sensors.getValue());
      }
    }).start();
  }

  private void updateActorPositions(List<DistanceBrickData> bricks){
    Optional<DistanceBrickData> maybeBrick = bricks
        .stream()
        .min(Comparator.comparing(DistanceBrickData::getDistance));

    maybeBrick.ifPresent(distanceBrickData -> {
      updateModel(set(distanceBrickData.isMostActive, true));
      model.actuators.getValue().forEach(brick -> updateServoAngles(brick, maybeBrick.get()));
    });
  }

  private void updateServoAngles(ServoBrickData servo, DistanceBrickData mostActivePlacement) {
    double dLat  = mostActivePlacement.x.getValue() - servo.x.getValue();
    double dLong = mostActivePlacement.y.getValue() - servo.y.getValue();
    double angle = Util.calcAngle(dLat, dLong);
    int pos      = Util.calculateServoPositionFromAngle(servo, angle);
//        servo.adjustServoPosition(pos);
    updateModel(set(servo.mostActiveAngle, angle - servo.faceAngle.getValue()));
    updateModel(set(servo.viewPortAngle, 180 + angle - 2 * servo.faceAngle.getValue()));
  }

  public void move(double x, double y, BrickData brick){
    updateModel(set(brick.x, x));
    updateModel(set(brick.y, y));
  }

  public void rotate(double angle, BrickData brick) {
    updateModel(set(brick.faceAngle, angle));
  }

  public void addSensor(boolean isSimulated, String id) {
   Proxy proxy = mqttProxy;

    if(isSimulated) {
      id = "sensor id: " + sensorIdCounter++;
      proxy = mockProxy;
    }

    List<DistanceBrickData> bs;
    bs = new ArrayList<>(model.sensors.getValue());
    DistanceBrickData db = new DistanceBrickData(DistanceBrick.connect(proxy, id));
    bs.add(db);
    updateModel(set(model.sensors, bs));
  }

  public void addActuator(boolean isSimulated, String id) {
    Proxy proxy = mqttProxy;

    if(isSimulated) {
      id = "sensor id: " + actuatorIdCounter++;
      proxy = mockProxy;
    }
    List<ServoBrickData> currentServoBricks;
    currentServoBricks      = new ArrayList<>(model.actuators.getValue());
    ServoBrickData newServo = new ServoBrickData(ServoBrick.connect(proxy, id));
    currentServoBricks.add(newServo);
    updateModel(set(model.actuators, currentServoBricks));
  }
}
