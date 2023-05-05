package main.java.model.brick;

import ch.fhnw.imvs.bricks.core.Brick;
import main.java.util.Location;
import main.java.util.mvcbase.ObservableValue;

import java.util.Date;

public abstract class BrickData {

  public final ObservableValue<Location> location;
  public final ObservableValue<Double> faceAngle;
  private final Brick inner;

  public BrickData(Brick inner){
    location   = new ObservableValue<>(new Location(400, 400));
    faceAngle  = new ObservableValue<>(0.0);
    this.inner = inner;
  }

  public String getID() {
    return inner.getID();
  }

  public double getBatteryVoltage() {
    return inner.getBatteryVoltage();
  }

  public Date getTimestamp() {
    return inner.getTimestamp();
  }

  public String getTimestampIsoUtc() {
    return inner.getTimestampIsoUtc();
  }

  @Override
  public String toString() {
    return inner.getID()
        + ", " + location.getValue().lat()
        + ", " + location.getValue().lon()
        + ", " + faceAngle.getValue();
  }
}
