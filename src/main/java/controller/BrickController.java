package main.java.controller;

import ch.fhnw.imvs.bricks.core.ProxyGroup;
import main.java.model.brick.BrickData;
import main.java.model.brick.DistanceBrickData;
import main.java.model.Garden;
import main.java.model.brick.ServoBrickData;
import main.java.util.Location;
import main.java.util.Util;
import main.java.util.mvcbase.ControllerBase;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BrickController extends ControllerBase<Garden> {

  private final ProxyGroup proxyGroup;

  public BrickController(Garden model) {
    super(model);

    proxyGroup = new ProxyGroup();
    proxyGroup.addProxy(model.mockProxy);
    proxyGroup.addProxy(model.mqttProxy);

    updateLoop();
  }

  private void updateLoop() {
    new Thread(() -> {
      while(true) {
        proxyGroup.waitForUpdate();
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
    Location mostActive    = mostActivePlacement.location.getValue();
    Location servoLocation = servo.location.getValue();

    double dLat  = mostActive.lat() - servoLocation.lat();
    double dLong = mostActive.lon() - servoLocation.lon();
//    System.out.println("dLat: " + dLat + ",  dLong: " + dLong);
    double angle = Util.calcAngle(dLong, dLat);
    int pos      = Util.calculateServoPositionFromAngle(servo, angle);
//        servo.adjustServoPosition(pos);
    updateModel(set(servo.mostActiveAngle, angle - servo.faceAngle.getValue()));
    updateModel(set(servo.viewPortAngle, 180 + angle - 2 * servo.faceAngle.getValue()));
  }

  public void move(Location target, BrickData brick){
    updateModel(set(brick.location, target));
  }

  public void rotate(double angle, BrickData brick) {
    updateModel(set(brick.faceAngle, angle));
  }
}
