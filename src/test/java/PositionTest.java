package test.java;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static main.java.util.Util.calcAngle;

public class PositionTest {

  private static final double PRECISION = 0.005;



  @Test
  public void testTypicalPointsInEachQuadrant() {

    assertEquals("first",   90 - 45, calcAngle( 5.0,  5.0), PRECISION);
    assertEquals("second", 360 - 45, calcAngle( 5.0, -5.0), PRECISION);
    assertEquals("third",  180 + 45, calcAngle(-5.0, -5.0), PRECISION);
    assertEquals("fourth",  90 + 45, calcAngle(-5.0,  5.0), PRECISION);
  }

  @Test
  public void testShiftedPointsInEachQuadrant() {
    assertEquals("first",   90 - 75.963, calcAngle( 4.0,  1.0), PRECISION);
    assertEquals("first",   90 - 51.340, calcAngle( 5.0,  4.0), PRECISION);
    assertEquals("first",   90 - 38.659, calcAngle( 4.0,  5.0), PRECISION);

    assertEquals("second", 270 + 75.963, calcAngle( 4.0, -1.0), PRECISION);
    assertEquals("second", 270 + 51.340, calcAngle( 5.0, -4.0), PRECISION);
    assertEquals("second", 270 + 38.659, calcAngle( 4.0, -5.0), PRECISION);

    assertEquals("third",  180 + 75.963, calcAngle(-4.0, -1.0), PRECISION);
    assertEquals("third",  180 + 51.340, calcAngle(-5.0, -4.0), PRECISION);
    assertEquals("third",  180 + 38.659, calcAngle(-4.0, -5.0), PRECISION);

    assertEquals("fourth",  90 + 75.963, calcAngle(-4.0,  1.0), PRECISION);
    assertEquals("fourth",  90 + 51.340, calcAngle(-5.0,  4.0), PRECISION);
    assertEquals("fourth",  90 + 38.659, calcAngle(-4.0,  5.0), PRECISION);
  }

  @Test
  public void testPointsOnAxis() {
    assertEquals("north",   0, calcAngle( 5.0,  0.0), PRECISION); // 90
    assertEquals("south", 180, calcAngle(-5.0,  0.0), PRECISION); // -90
    assertEquals("east",   90, calcAngle( 0.0,  5.0), PRECISION); // 0
    assertEquals("west",  270, calcAngle( 0.0, -5.0), PRECISION); // 0
  }
}
