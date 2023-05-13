package ch.fhnw.iotbricksimulator.controller;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import ch.fhnw.iotbricksimulator.model.Garden;
import ch.fhnw.iotbricksimulator.model.brick.BrickData;
import ch.fhnw.iotbricksimulator.model.brick.DistanceBrickData;
import ch.fhnw.iotbricksimulator.model.brick.ServoBrickData;
import ch.fhnw.iotbricksimulator.util.Constants;
import ch.fhnw.iotbricksimulator.util.Location;
import ch.fhnw.iotbricksimulator.util.mvcbase.ControllerBase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

  public synchronized DistanceBrickData addDistanceBrick(String id, boolean isSimulated) {
    Proxy proxy = model.mqttProxy;

    if(isSimulated) {
      id = Constants.MOCK_SENSOR_PREFIX + sensorIdCounter++;
      proxy = model.mockProxy;
    }

    List<DistanceBrickData> bs;
    bs = new ArrayList<>(model.sensors.getValue());
    DistanceBrickData newBrick = new DistanceBrickData(DistanceBrick.connect(proxy, id));
    bs.add(newBrick);
    updateModel(set(model.sensors, bs));
    return newBrick;
  }

  public synchronized ServoBrickData addServoBrick(String id, boolean isSimulated) {
    Proxy proxy = model.mqttProxy;

    if(isSimulated) {
      id = Constants.MOCK_ACTUATOR_PREFIX +  actuatorIdCounter++;
      proxy = model.mockProxy;
    }
    List<ServoBrickData> currentServoBricks;
    currentServoBricks      = new ArrayList<>(model.actuators.getValue());
    ServoBrickData newServo = new ServoBrickData(ServoBrick.connect(proxy, id));
    currentServoBricks.add(newServo);
    updateModel(set(model.actuators, currentServoBricks));
    return newServo;
  }

  public void printAllBrickData() {
    StringBuilder sb      = new StringBuilder();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now     = LocalDateTime.now();
    sb.append("Data Snapshot from:").append(dtf.format(now)).append("\n");
    sb.append("\nSensors:\n");
    sb.append(toStringOfBrickList(model.sensors.getValue()));
    sb.append("\nActuators:\n");
    sb.append(toStringOfBrickList(model.actuators.getValue()));
    System.out.println(sb);
  }

  private String toStringOfBrickList(List<? extends BrickData> bricks) {
          return String.join("\n", bricks.stream().map(BrickData::toString).toList());
  }

  public void exportConfig(){
    updateModel(set(model.isLoading, true));
    new Thread(this::exportConfigAsync).start();
  }

  public void exportConfigAsync(){
    convertToCSV(
        Stream.concat(
            model.actuators.getValue().stream(),
            model.sensors  .getValue().stream()
        ).toList()
    );
   updateModel(set(model.isLoading, false));
  }

  private void convertToCSV(List<? extends BrickData> bricks) {
    try (PrintWriter printWriter = new PrintWriter(Constants.CSV_PATH + "test.csv")) {
      printWriter.write("mock,brick,id,lat,long,faceAngle\n");
      bricks.stream()
          .map(s -> {
            boolean type = s.getID().contains("mock");
            return String.valueOf(type).concat(",").concat(s.toString());
          })
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

  private void importConfigAsync() {
    try (Stream<String> lines = Files.lines(Paths.get(Constants.CSV_PATH + "test.csv"))) {
      lines
          .skip(1) // header
          .map(line -> line.split(","))
          .forEach(this::createBrickFromStringLine);
    } catch (IOException e) {
      updateModel(set(model.isLoading, false));
      System.err.println("Read CSV: Could not read csv file!");
    }
    updateModel(set(model.isLoading, false));
  }

  private void createBrickFromStringLine(String[] line) {
    // line content:  1: mock, 2: brick, 3: id, 4: lat, 5: long, 6: faceAngle
    BrickData brick;
    boolean isMock = Boolean.parseBoolean(line[0]);
    if (line[1].contains(DistanceBrick.class.getSimpleName())) {
      brick = addDistanceBrick(line[2], isMock);
    } else if (line[1].contains(ServoBrick.class.getSimpleName())) {
      brick = addServoBrick(line[2], isMock);
    } else {
      throw new IllegalArgumentException("Import CSV: Could not recognize Brick type!");
    }
    this.awaitCompletion();

    updateModel(set(brick.location, new Location(Double.parseDouble(line[3]), Double.parseDouble(line[4]))));
    updateModel(set(brick.faceAngle, Double.parseDouble(line[5])));
  }
}
