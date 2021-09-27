package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars;

import java.util.Comparator;

/**
 * Comparator which compares based on x coordinate of a star.
 */
public class StarXComparator implements Comparator<Star> {

  /**
   * Empty constructor for StarXComparator.
   */
  public StarXComparator() { }

  /**
   * Compares stars by x coordinate.
   * @param star1 first star which you want to compare.
   * @param star2 second star which you want to compare.
   * @return returns 1 if x coord of first is bigger than second, 0 if they are equal,
   * and -1 otherwise.
   */
  @Override
  public int compare(Star star1, Star star2) {
    double x1 = star1.getX();
    double x2 = star2.getX();
    if (x1 > x2) {
      return 1;
    } else if (x1 == x2) {
      return 0;
    } else {
      return -1;
    }
  }
}
