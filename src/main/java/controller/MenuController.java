package main.java.controller;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import main.java.Constants;
import main.java.model.Garden;
import main.java.model.brick.BrickData;
import main.java.model.brick.DistanceBrickData;
import main.java.model.brick.ServoBrickData;
import main.java.util.Location;
import main.java.util.mvcbase.ControllerBase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MenuController extends ControllerBase<Garden> {

  private int actuatorIdCounter = 0;
  private int sensorIdCounter   = 0;

  public MenuController(Garden model) {
    super(model);
  }

  public synchronized DistanceBrickData addSensor(boolean isSimulated, String id) {
    Proxy proxy = model.mqttProxy;

    if(isSimulated) {
      id = "auto gen: " + sensorIdCounter++;
      proxy = model.mockProxy;
    }

    List<DistanceBrickData> bs;
    bs = new ArrayList<>(model.sensors.getValue());
    DistanceBrickData newBrick = new DistanceBrickData(DistanceBrick.connect(proxy, id));
    bs.add(newBrick);
    updateModel(set(model.sensors, bs));
    return newBrick;
  }

  public synchronized ServoBrickData addActuator(boolean isSimulated, String id) {
    Proxy proxy = model.mqttProxy;

    if(isSimulated) {
      id = "auto gen: " + actuatorIdCounter++;
      proxy = model.mockProxy;
    }
    List<ServoBrickData> currentServoBricks;
    currentServoBricks      = new ArrayList<>(model.actuators.getValue());
    ServoBrickData newServo = new ServoBrickData(ServoBrick.connect(proxy, id));
    currentServoBricks.add(newServo);
    updateModel(set(model.actuators, currentServoBricks));
    return newServo;
  }

  public void printBrickData() {
    StringBuilder sb      = new StringBuilder();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now     = LocalDateTime.now();
    List<? extends BrickData> sensors   = model.sensors.getValue();
    List<? extends BrickData> actuators = model.actuators.getValue();

    sb.append("Data Snapshot from:").append(dtf.format(now)).append("\n");
    if(!sensors.isEmpty()){
      sb.append("Sensors:\n")
          .append(placementDataToString(sensors));
    }
    if(!actuators.isEmpty()){
      sb.append("Actors:\n")
          .append(placementDataToString(actuators));
    }
    System.out.println(sb);
  }

  private String placementDataToString(List<? extends BrickData> placements) {
    StringBuilder sb = new StringBuilder();
    placements.forEach(brick -> {
      sb.append("id: ")      .append(brick.getID());
      sb.append(",\tlat: ")  .append(brick.location.getValue().lat());
      sb.append(",\tlong: ") .append(brick.location.getValue().lon());
      sb.append(",\tangle: ").append(brick.faceAngle.getValue());
      sb.append("\n");
    });
    return sb.toString();
  }

  public void exportConfig(){
    updateModel(set(model.isLoading, true));
    new Thread(this::exportConfigAsync).start();
  }

  public void exportConfigAsync(){
    convertToCSV(
        Stream.concat(
            model.actuators.getValue().stream(),
            model.sensors.getValue().stream()
        ).toList()
    );
   updateModel(set(model.isLoading, false));
  }

  private void convertToCSV(List<? extends BrickData> bricks) {
    try (PrintWriter printWriter = new PrintWriter(Constants.CSV_PATH + "test.csv")) {
      printWriter.write("mock,brick,id,lat,long,faceAngle\n");
      bricks.stream()
          .map(s -> {
            String type = s.getID().contains("auto gen") ? "true" : "false";
            return type.concat(",").concat(s.toString());
          })
          .map(s -> s.replace(" ", ""))
          .map(s -> s.concat("\n"))
          .forEach(printWriter::write);
    } catch (FileNotFoundException e) {
      updateModel(set(model.isLoading, false));
      System.err.println("Create CSV: File could not be created!");
    }
  }

  public void importConfig() {
    updateModel(set(model.isLoading, true));
    new Thread(this::importConfigAsync).start();
  }

  public void importConfigAsync() {
    try (Stream<String> lines = Files.lines(Paths.get(Constants.CSV_PATH + "test.csv"))) {
      lines
          .skip(1) // header
          .map(line -> line.split(","))
          .forEach(this::createBrickFromLine);
    } catch (IOException e) {
      updateModel(set(model.isLoading, false));
      System.err.println("Read CSV: Could not read csv file!");
    }
    updateModel(set(model.isLoading, false));
  }

  private void createBrickFromLine(String[] line) {
    BrickData brick;
    boolean isMock = Boolean.parseBoolean(line[0]);
    if (line[1].contains("Distance")) {
      brick = addSensor(isMock, line[2]);
    } else if (line[1].contains("Servo")) {
      brick = addActuator(isMock, line[2]);
    } else {
      throw new IllegalArgumentException("Import CSV: Could not recognize Brick type!");
    }

    //noinspection StatementWithEmptyBody,SuspiciousMethodCalls
    while (!model.sensors.getValue().contains(brick) && !model.actuators.getValue().contains(brick)) {
        // waiting until the brick is in a bricks list
    }
    updateModel(set(brick.location, new Location(Double.parseDouble(line[3]), Double.parseDouble(line[4]))));
    updateModel(set(brick.faceAngle, Double.parseDouble(line[5])));
  }
}
