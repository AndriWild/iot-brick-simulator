package main.java.ch.fhnw.controller;

import ch.fhnw.imvs.bricks.core.ProxyGroup;
import main.java.ch.fhnw.model.Garden;
import main.java.ch.fhnw.model.brick.BrickData;
import main.java.ch.fhnw.model.brick.DistanceBrickData;
import main.java.ch.fhnw.model.brick.ServoBrickData;
import main.java.ch.fhnw.util.Location;
import main.java.ch.fhnw.util.Util;
import main.java.ch.fhnw.util.mvcbase.ControllerBase;

import java.util.ArrayList;
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

  public void toggleRemoveButtonVisible(){
    updateModel(toggle(model.removeButtonVisible));
  }

  public void removeBrick(DistanceBrickData data) {
    List<DistanceBrickData> modified = new ArrayList<>(model.sensors.getValue())
        .stream()
        .filter(b -> !b.getID().equals(data.getID()))
        .toList();
    updateModel(set(model.sensors, modified));
  }

  public void removeBrick(ServoBrickData data) {
    List<ServoBrickData> modified = new ArrayList<>(model.actuators.getValue())
        .stream()
        .filter(b -> !b.getID().equals(data.getID()))
        .toList();
    updateModel(set(model.actuators, modified));
  }
}
