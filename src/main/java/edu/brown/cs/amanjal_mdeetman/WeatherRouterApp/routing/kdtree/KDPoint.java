package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree;

/**
 * Interface which defines a k-d point.
 */
public interface KDPoint {
  /**
   * Gets the coordinates for the point.
   * @return an array of doubles giving the coordinates
   */
  double[] getCoords();

  /**
   * Gets distance from the point to another point.
   * @param point another KDPoint
   * @return distance between the two points
   */
  double getDist(KDPoint point);

  /**
   * Gets distance from the point to a location.
   * @param coords coordinates from the target location.
   * @return distance between the point and the target location.
   */
  double getDist(double[] coords);

  /**
   * Gets the id associated with the point.
   * @return the id of the point. Should be a positive integer.
   */
  int getId();
}
