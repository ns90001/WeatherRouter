package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.KDPoint;

/**
 * Star class which stores information about a particular star.
 */
public class Star implements KDPoint {
  private final int id;
  private final String name;
  private final double x;
  private final double y;
  private final double z;
  private final double[] coords;

  /**
   * Constructor which instantiates local variables.
   *
   * @param id star's ID
   * @param name star's proper name
   * @param x star's x coordinate
   * @param y star's y coordinate
   * @param z star's z coordinate
   */
  public Star(int id, String name, double x, double y, double z) {
    this.id = id;
    this.name = name;
    this.x = x;
    this.y = y;
    this.z = z;
    this.coords = new double[]{x, y, z};
  }

  /**
   * getter method for the star ID.
   *
   * @return ID associated with this star
   */
  @Override
  public int getId() {
    return this.id;
  }

  /**
   * getter method for the star's proper name.
   *
   * @return name associated with this star
   */
  public String getName() {
    return this.name;
  }

  /**
   * getter method for the x coordinate of star.
   *
   * @return x coordinate of the star's position
   */
  public double getX() {
    return this.x;
  }

  /**
   * getter method for the z coordinate of star.
   *
   * @return z coordinate of the star's position
   */
  public double getY() {
    return this.y;
  }

  /**
   * getter method for the z coordinate of star.
   *
   * @return z coordinate of the star's position
   */
  public double getZ() {
    return this.z;
  }

  /**
   * Outputs a description of the star.
   *
   * @return the ID and name associate with this star
   */
  @Override
  public String toString() {
    return String.format("ID: %d,Name: %s", this.id, this.name);
  }

  @Override
  public double[] getCoords() {
    return coords;
  }

  /**
   * Gets the distance between two stars.
   *
   * @param point another star with a distance you want to compare
   * @return distance between the two stars
   */
  @Override
  public double getDist(KDPoint point) {
    double[] compCoords = point.getCoords();
    return calcDist(compCoords);
  }

  @Override
  public double getDist(double[] compCoords) {
    return calcDist(compCoords);
  }

  private double calcDist(double[] compCoords) {
    if (compCoords.length != coords.length) {
      return -1;
    }
    double dist = 0;
    for (int i = 0; i < coords.length; i++) {
      double diff = coords[i] - compCoords[i];
      dist += diff * diff;
    }
    return dist;
  }
}
