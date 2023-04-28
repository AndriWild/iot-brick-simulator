package main.java.util;

import main.java.model.Location;
import main.java.view.brick.BrickPlacement;

import static main.java.model.Constants.*;

public class Util {


  /**
   *     0   45
   *     | /
   * 270 * 90
   *     |
   *    180
   * @param dLat latitude (Breitengrad)
   * @param dLong longitude (Längengrad)
   * @return degree
   */
  public static double calcAngle(double dLat, double dLong){
    double degrees = Math.toDegrees(Math.atan(dLat / dLong));

    // on x-axis
    if(dLat == 0) {
      // west
      if(dLong >= 0){
        return 90;
      }
      // east
      return 270;
    }

    // on y-axis
    if(dLong == 0){
      // north
      if(dLat >= 0){
        return 0;
      }
      return 180;
    }

    // first quadrant
    if (dLat >= 0 && dLong >= 0){
      return 90 - degrees;
    }

    // second quadrant
    if(dLat > 0 && dLong < 0){
      return 270 - degrees;
    }

    // third quadrant
    if(dLat < 0 && dLong < 0){
      return 180 + (90 - degrees);
    }

    // fourth quadrant
    if(dLat < 0 && dLong >= 0){
      return 90 - degrees;
    }

    throw new IllegalArgumentException("Angle could not be calculated");
  }

  public static int calculateServoPositionFromAngle(BrickPlacement brick, double angle) {
    double result = angle - brick.getFaceAngle() + 90;
    if (result < 0) { result += 360.0; }
    return (int) result;
  }

  public static Location convertPixelToLocation(double x, double y) {
    double lon = (LEFT_LONG + ((RIGHT_LONG - LEFT_LONG) / WINDOW_WIDTH) * x);
    double lat = (BOTTOM_LAT + ((TOP_LAT - BOTTOM_LAT) / WINDOW_HEIGHT) * y);
    return new Location(lat, lon);
  }

  public static int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }
}
