package test.java;

import main.java.old.model.Location;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static main.java.old.model.Constants.*;
import static main.java.util.Util.toCoordinates;

public class CoordPixelMapTest {

  private static final double PRECISION = 0.0001;

  @Test
  public void testOrigin() {
    Location result = toCoordinates(0, 0);
    Location expected = new Location(BOTTOM_LAT, LEFT_LONG);
    assertEquals("origin lat", expected.lat(), result.lat(), PRECISION);
    assertEquals("origin lon", expected.lon(), result.lon(), PRECISION);
  }

  @Test
  public void testTopRightCorner() {
    Location result = toCoordinates(WINDOW_WIDTH, WINDOW_HEIGHT);
    Location expected = new Location(TOP_LAT, RIGHT_LONG);
    assertEquals("lat", expected.lat(), result.lat(), PRECISION);
    assertEquals("lon", expected.lon(), result.lon(), PRECISION);
  }

  @Test
  public void testTopLeftCorner() {
    Location result = toCoordinates(0, WINDOW_HEIGHT);
    Location expected = new Location(TOP_LAT, LEFT_LONG);
    assertEquals("lat", expected.lat(), result.lat(), PRECISION);
    assertEquals("lon", expected.lon(), result.lon(), PRECISION);
  }

  @Test
  public void testBottomRightCorner() {
    Location result = toCoordinates(WINDOW_WIDTH, 0);
    Location expected = new Location(BOTTOM_LAT, RIGHT_LONG);
    assertEquals("lat", expected.lat(), result.lat(), PRECISION);
    assertEquals("lon", expected.lon(), result.lon(), PRECISION);
  }

  @Test
  public void testCenter() {
    Location result = toCoordinates((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / 2);
    Location expected = new Location(
        BOTTOM_LAT + ((TOP_LAT - BOTTOM_LAT) / 2),
        LEFT_LONG + ((RIGHT_LONG - LEFT_LONG) / 2)
    );
    assertEquals("lat", expected.lat(), result.lat(), PRECISION);
    assertEquals("lon", expected.lon(), result.lon(), PRECISION);
  }
}
