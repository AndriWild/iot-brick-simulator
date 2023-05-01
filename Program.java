// $ curl -Lo lib/org.eclipse.paho.client.mqttv3-1.2.3.jar https://repo.eclipse.org/content/repositories/paho-releases/org/eclipse/paho/org.eclipse.paho.client.mqttv3/1.2.3/org.eclipse.paho.client.mqttv3-1.2.3.jar
// $ javac -d target -cp target:lib/fhnw-iot-bricks-0.0.1.jar:lib/org.eclipse.paho.client.mqttv3-1.2.3.jar *.java
// $ java -ea -cp ./target:lib/org.eclipse.paho.client.mqttv3-1.2.3.jar:lib/fhnw-iot-bricks-0.0.1.jar Program

import java.util.concurrent.TimeUnit;
import ch.fhnw.imvs.bricks.core.Brick;
import ch.fhnw.imvs.bricks.core.Proxy;
import ch.fhnw.imvs.bricks.core.ProxyGroup;
import ch.fhnw.imvs.bricks.mock.MockProxy;
import ch.fhnw.imvs.bricks.mqtt.MqttProxy;
import ch.fhnw.imvs.bricks.actuators.ServoBrick;
import ch.fhnw.imvs.bricks.sensors.DistanceBrick;

public final class Program {
    private static final String BASE_URL = "brick.li/";
    private static final String DISTANCE_BRICK_0_ID = "0000-0030";
    private static final String DISTANCE_BRICK_1_ID = "0000-0300";

    private static final String DISTANCE_BRICK_2_ID = "0000-0302";
    private static final String DISTANCE_BRICK_3_ID = "0000-0303";
    private static final String DISTANCE_BRICK_4_ID = "0000-0304";
    private static final String DISTANCE_BRICK_5_ID = "0000-0305";
    private static final String DISTANCE_BRICK_6_ID = "0000-0306";
    private static final String DISTANCE_BRICK_7_ID = "0000-0307";
    private static final String DISTANCE_BRICK_8_ID = "0000-0308";

    private static final String SERVO_BRICK_0_ID = "0000-0008";
    private static final String SERVO_BRICK_1_ID = "0000-0080";
    private static final String SERVO_BRICK_2_ID = "0000-0800";

    static final class BrickPlacement {
        Brick b;
        double e, n, a; // koordinaten und winkel (0 könnte Nord sein)
        public BrickPlacement(Brick b, double e, double n, double a) {
            this.b = b;
            this.e = e;
            this.n = n;
            this.a = a;
        }
        // https://www.swisstopo.admin.ch/en/knowledge-facts/surveying-geodesy/coordinates/swiss-coordinates.html
        double getLV95East() { return e; }
        double getLV95North() { return n; }
        double getFaceAngle() { return a; }
        Brick getBrick() { return b; }
    }

    static int i = 0;

    static DistanceBrick pickNextSensor(DistanceBrick sensors[]) {
        DistanceBrick result = sensors[i];
        i = (i + 1) % sensors.length;
        return result;
    }

    static DistanceBrick pickMostActiveSensor(DistanceBrick sensors[]) {
        int min = Integer.MAX_VALUE;
        int i = 0;
        DistanceBrick result = null;
        while (i < sensors.length) {
            int d = sensors[i].getDistance();
            System.out.print(d + " ");
            if (d < min) {
                min = d;
                result = sensors[i];
            }
            i++;
        }
        System.out.println("] => " + result.getDistance());
        return result;
    }

    static BrickPlacement getPlacementOfBrick(BrickPlacement placements[], Brick brick) {
        BrickPlacement result = null;
        int i = 0;
        while (result == null && i < placements.length) {
            if (placements[i].getBrick() == brick) {
                result = placements[i];
            }
            i++;
        }
        return result;
    }

    static double calculateAngleToPlacement(BrickPlacement fromPlacement, BrickPlacement toPlacement) {
        //
        //  t       t
        //  |\     /|
        //  | \   / |dy
        //  *--a a--*
        //  -dx f dx
        //  *--a a--*
        //  | /   \ |-dy
        //  |/     \|
        //  t       t
        //
        double dx = toPlacement.getLV95East() - fromPlacement.getLV95East();
        double dy = toPlacement.getLV95North() - fromPlacement.getLV95North();
        double a = Math.toDegrees(Math.atan(Math.abs(dx) / Math.abs(dy)));
        System.out.println("dx = " + dx + ", dy = " + dy + ", arctan(abs(dx)/abs(dy)) = " + a);
        double result; // 0 degree => north, increase clockwise
        if (dx > 0 && dy > 0) {
            result = 90 - a;
        } else if (dx > 0 && dy < 0) {
            result = 90 + a;
        } else if (dx < 0 && dy < 0) {
            result = 270 - a;
        } else if (dx < 0 && dy > 0) {
            result = 270 + a;
        } else if (dx == 0 && dy > 0) {
            result = 0;
        } else if (dx == 0 && dy < 0) {
            result = 180;
        } else if (dx > 0 && dy == 0) {
            result = 90;
        } else if (dx < 0 && dy == 0) {
            result = 270;
        } else {
            assert (dx == 0 && dy == 0);
            result = 0;
        }
        return result;
    }

	// servo position (Pfeil auf dem Brick)
    static int calculatePositionFromAngle(BrickPlacement placement, double angle) {
        double result = (angle - placement.getFaceAngle()) - 90.0;
        if (result < 0) { result += 360.0; }
        return (int) result;
    }

    static void sleep(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Proxy proxy = MockProxy.fromConfig(BASE_URL);
        Proxy proxy2 = MqttProxy.fromConfig(BASE_URL);

        DistanceBrick sensors[] = {
//            DistanceBrick.connect(proxy, DISTANCE_BRICK_0_ID),
//            DistanceBrick.connect(proxy, DISTANCE_BRICK_1_ID)
            DistanceBrick.connect(proxy, DISTANCE_BRICK_0_ID),
            DistanceBrick.connect(proxy, DISTANCE_BRICK_1_ID),
            DistanceBrick.connect(proxy, DISTANCE_BRICK_2_ID),
            DistanceBrick.connect(proxy, DISTANCE_BRICK_3_ID),
            DistanceBrick.connect(proxy, DISTANCE_BRICK_4_ID),
            DistanceBrick.connect(proxy, DISTANCE_BRICK_5_ID),
            DistanceBrick.connect(proxy, DISTANCE_BRICK_6_ID),
            DistanceBrick.connect(proxy, DISTANCE_BRICK_7_ID),
            DistanceBrick.connect(proxy, DISTANCE_BRICK_8_ID)
        };

        ServoBrick servos[] = {
            ServoBrick.connect(proxy2, SERVO_BRICK_0_ID)
//            ServoBrick.connect(proxy, SERVO_BRICK_0_ID),
//            ServoBrick.connect(proxy, SERVO_BRICK_1_ID),
//            ServoBrick.connect(proxy, SERVO_BRICK_2_ID)
        };

        BrickPlacement placements[] = { // 682029.60, 247706.52
            new BrickPlacement(servos[0], 10, 10, 0),
//            new BrickPlacement(sensors[0], 10, 0, 0),
//            new BrickPlacement(servos[1], 20, 0, 0),
//            new BrickPlacement(sensors[1], 30, 0, 0),
//            new BrickPlacement(servos[2], 40, 0, 0)
            new BrickPlacement(sensors[0], 20, 20, 0),
            new BrickPlacement(sensors[1], 20, 0, 0),
            new BrickPlacement(sensors[2], 0, 0, 0),
            new BrickPlacement(sensors[3], 0, 20, 0),
            new BrickPlacement(sensors[4], 10, 20, 0),
            new BrickPlacement(sensors[5], 10, 0, 0),
            new BrickPlacement(sensors[6], 20, 10, 0),
            new BrickPlacement(sensors[7], 0, 10, 0),
            new BrickPlacement(sensors[8], 10, 10, 0)
        };

        ProxyGroup proxies = new ProxyGroup();
        proxies.addProxy(proxy);
        proxies.addProxy(proxy2);

        while (true) {
            //DistanceBrick sensor = pickNextSensor(sensors); 
            DistanceBrick sensor = pickMostActiveSensor(sensors); 
            BrickPlacement sensorPlacement = getPlacementOfBrick(placements, sensor);
            for (ServoBrick servo : servos) {
                BrickPlacement servoPlacement = getPlacementOfBrick(placements, servo);
                double angle = calculateAngleToPlacement(servoPlacement, sensorPlacement);
                int pos = calculatePositionFromAngle(servoPlacement, angle);
                System.out.println("Servo " + servo.getID() + 
                    " target angle = " + angle + ", pos = " + pos);
                pos = Math.max(0, Math.min(pos, 180));
                servo.setPosition(pos);
            }
            //proxy.waitForUpdate();
            proxies.waitForUpdate();
            sleep(3000); // ms
        }
    }
}
