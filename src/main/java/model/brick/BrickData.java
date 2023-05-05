package main.java.model.brick;

import ch.fhnw.imvs.bricks.core.Brick;
import main.java.old.model.Constants;
import main.java.util.mvcbase.ObservableValue;

import java.util.Date;

public abstract class BrickData {

  public final ObservableValue<Double> x;
  public final ObservableValue<Double> y;
  public final ObservableValue<Double> faceAngle;
  private final Brick inner;

  public BrickData(Brick inner){
    x          = new ObservableValue<>(100.0);
    y          = new ObservableValue<>(100.0);
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
}
