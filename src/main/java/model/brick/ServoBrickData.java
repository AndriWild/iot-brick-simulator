package main.java.model.brick;

import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import main.java.util.mvcbase.ObservableValue;

public class ServoBrickData extends BrickData {

    public final ObservableValue<Double> mostActiveAngle;
    public final ObservableValue<Double> viewPortAngle;
    private final ServoBrick inner;

    public ServoBrickData(ServoBrick inner) {
        super(inner);
        this.inner = inner;
        mostActiveAngle = new ObservableValue<>(0d);
        viewPortAngle   = new ObservableValue<>(0d);
    }

    public void getPosition(int i) {
        inner.setPosition(i);
    }


    @Override
    public String toString() {
        return "ServoBrick" + ", " + super.toString();
    }
}
