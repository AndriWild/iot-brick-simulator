package main.java.model;

import ch.fhnw.imvs.bricks.sensors.DistanceBrick;
import main.java.util.mvcbase.ObservableValue;

import java.util.Date;

public class DistanceBrickData {
    private final DistanceBrick inner;

    public final ObservableValue<Integer> value;
    public final ObservableValue<Double> x;
    public final ObservableValue<Double> y;


    public DistanceBrickData(DistanceBrick inner) {
        this.inner = inner;
        value = new ObservableValue<>(0);
        x = new ObservableValue<>(0.0);
        y = new ObservableValue<>(0.0);
    }

    public int getDistance() {
        return inner.getDistance();
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
