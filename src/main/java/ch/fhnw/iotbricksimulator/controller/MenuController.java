package ch.fhnw.iotbricksimulator.controller;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import ch.fhnw.iotbricksimulator.model.Garden;
import ch.fhnw.iotbricksimulator.model.Notification.Notification;
import ch.fhnw.iotbricksimulator.model.Notification.Type;
import ch.fhnw.iotbricksimulator.model.brick.BrickData;
import ch.fhnw.iotbricksimulator.model.brick.DistanceBrickData;
import ch.fhnw.iotbricksimulator.model.brick.ServoBrickData;
import ch.fhnw.iotbricksimulator.util.Constants;
import ch.fhnw.iotbricksimulator.util.Location;
import ch.fhnw.iotbricksimulator.util.Util;
import ch.fhnw.iotbricksimulator.util.mvcbase.ControllerBase;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

import static ch.fhnw.iotbricksimulator.util.ConfigIOHandler.readFromFile;
import static ch.fhnw.iotbricksimulator.util.ConfigIOHandler.writeToFile;

public class MenuController extends ControllerBase<Garden> {

  private int actuatorIdCounter = 0;
  private int sensorIdCounter   = 0;
  private double spiralValue    = 5d;

  private final List<String> mqttIds;

  public MenuController(Garden model) {
    super(model);
    mqttIds = new ArrayList<>();
  }

  public synchronized Optional<DistanceBrickData> addDistanceBrick(String id, boolean isSimulated) {
    Proxy proxy = model.mqttProxy;

    if(isSimulated) {
      id = Constants.MOCK_SENSOR_PREFIX + sensorIdCounter++;
      proxy = model.mockProxy;
    } else {
      if(mqttIds.contains(id)){
        createNotification(Type.ERROR, "Create MQTT Brick", "Id has already been assigned!");
        System.err.println("Id has already been assigned!");
        return Optional.empty();
      }
      mqttIds.add(id);
    }

    List<DistanceBrickData> bs;
    bs = new ArrayList<>(model.sensors.getValue());
    DistanceBrickData newBrick = new DistanceBrickData(DistanceBrick.connect(proxy, id));
    bs.add(newBrick);
    updateModel(set(model.sensors, bs));
    updateModel(set(newBrick.location, calcSpawnPosition()));
    return Optional.of(newBrick);
  }

  public synchronized Optional<ServoBrickData> addServoBrick(String id, boolean isSimulated) {
    Proxy proxy = model.mqttProxy;

    if(isSimulated) {
      id = Constants.MOCK_ACTUATOR_PREFIX +  actuatorIdCounter++;
      proxy = model.mockProxy;
    }
    List<ServoBrickData> currentServoBricks;
    currentServoBricks      = new ArrayList<>(model.actuators.getValue());
    ServoBrickData newBrick = new ServoBrickData(ServoBrick.connect(proxy, id));
    currentServoBricks.add(newBrick);
    updateModel(set(model.actuators, currentServoBricks));
    updateModel(set(newBrick.location, calcSpawnPosition()));
    return Optional.of(newBrick);
  }

  public void exportToFile(File file) {
    updateModel(set(model.isLoading, true));
    boolean success = writeToFile(file,
        Stream.concat(
            model.actuators.getValue().stream(),
            model.sensors  .getValue().stream()
        ).toList()
    );
    if(!success){
      createNotification(Type.ERROR, "Export config", "Failed to export config!");
    }
    updateModel(set(model.isLoading, false));
  }

  public void printAllBrickData() {
    String sb = "Data Snapshot from:" + Util.getTimeStamp() + "\n" +
        "\nSensors:\n" +
        toStringOfBrickList(model.sensors.getValue()) +
        "\nActuators:\n" +
        toStringOfBrickList(model.actuators.getValue());
    System.out.println(sb);
  }

  public void importFromFile(File file){
    updateModel(set(model.isLoading, true));
    readFromFile(file).ifPresentOrElse(
        this::importConfigFromList,
        () -> createNotification(Type.ERROR, "Load Config", "Failed to read config!")
    );
    updateModel(set(model.isLoading, false));
  }

  private String toStringOfBrickList(List<? extends BrickData> bricks) {
    return String.join("\n", bricks.stream().map(BrickData::toString).toList());
  }

  private void createNotification(Type type, String title, String message) {
    Notification newNotification = new Notification(type, title, message);
    Deque<Notification> queue    = new ArrayDeque<>(model.notifications.getValue());
    queue.push(newNotification);
    updateModel(set(
        model.notifications,
        queue
    ));
  }

  private void importConfigFromList(List<String> lines) {
    lines.stream()
        .skip(1) // header
        .map(line -> line.split(","))
        .forEach(this::createBrickFromStringLine);
  }

  private void createBrickFromStringLine(String[] line) {
    // line content:  1: mock, 2: brick, 3: id, 4: lat, 5: long, 6: faceAngle
    Optional<? extends BrickData> brick;
    boolean isMock = Boolean.parseBoolean(line[0]);
    if (line[1].contains(DistanceBrick.class.getSimpleName())) {
      brick = addDistanceBrick(line[2], isMock);
    } else if (line[1].contains(ServoBrick.class.getSimpleName())) {
      brick = addServoBrick(line[2], isMock);
    } else {
      createNotification(
          Type.WARNING,
          "Import CSV",
          "Could not recognize Brick type!\n" + Arrays.toString(line)
      );
      return;
    }
    this.awaitCompletion();
    brick.ifPresentOrElse(
        newBrick ->
            updateModel(
                set(newBrick.location, new Location(Double.parseDouble(line[3]), Double.parseDouble(line[4]))),
                set(newBrick.faceAngle, Double.parseDouble(line[5]))
            ),
        () -> createNotification(
            Type.ERROR,
            "Create Brick from Config",
            "Failed to create Brick from CSV Data!"
        )
    );
  }

  private Location calcSpawnPosition() {
    // archimedic spiral formula: x(t) = at cos(t), y(t) = at sin(t)
    double a = 10;
    double offset = (double) Constants.WINDOW_HEIGHT / 2;
    double t = spiralValue;
    double x = a * t * Math.cos(t);
    double y = a * t * Math.sin(t);
    spiralValue += 0.5;
    return new Location(x + offset, y + offset);
  }
}