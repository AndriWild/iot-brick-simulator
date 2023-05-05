package main.java.controller;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import main.java.model.Garden;
import main.java.model.brick.BrickData;
import main.java.model.brick.DistanceBrickData;
import main.java.model.brick.ServoBrickData;
import main.java.util.mvcbase.ControllerBase;

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

  public void addSensor(boolean isSimulated, String id) {
    Proxy proxy = model.mqttProxy;

    if(isSimulated) {
      id = String.valueOf(sensorIdCounter++);
      proxy = model.mockProxy;
    }

    List<DistanceBrickData> bs;
    bs = new ArrayList<>(model.sensors.getValue());
    DistanceBrickData db = new DistanceBrickData(DistanceBrick.connect(proxy, id));
    bs.add(db);
    updateModel(set(model.sensors, bs));
  }

  public void addActuator(boolean isSimulated, String id) {
    Proxy proxy = model.mqttProxy;

    if(isSimulated) {
      id = String.valueOf(actuatorIdCounter++);
      proxy = model.mockProxy;
    }
    List<ServoBrickData> currentServoBricks;
    currentServoBricks      = new ArrayList<>(model.actuators.getValue());
    ServoBrickData newServo = new ServoBrickData(ServoBrick.connect(proxy, id));
    currentServoBricks.add(newServo);
    updateModel(set(model.actuators, currentServoBricks));
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

  public void export(){
    String res = convertToCSV(
        Stream.concat(
            model.actuators.getValue().stream(),
            model.sensors.getValue().stream()
        ).toList()
    );
    System.out.println(res);
  }

  private String convertToCSV(List<? extends BrickData> bricks){
    String header = "type,id,lat,long,faceAngle\n";
    List<String> brickStrings = bricks.stream()
        .map(BrickData::toString)
        .map(s -> s.replace(" ", ""))
        .toList();
    return header.concat(String.join("\n", brickStrings));
  }
}
