package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.Star;

import java.util.List;
import java.util.TreeMap;

/**
 * Class representing the naive_radius command.
 */
public class NaiveRadiusCmd implements ICallable {
  private final StarsCmd starsCmd;

  /**
   * Constructor for NaiveRadiusCmd which initializes instance variables.
   * @param stars instance of the StarsCmd object which contains the list of stars
   *                 as well as helper methods
   */
  public NaiveRadiusCmd(StarsCmd stars) {
    starsCmd = stars;
  }

  @Override
  public String run(List<String> tokens) {
    if (tokens.size() != 2 && tokens.size() != 4) {
      return "ERROR: naive_radius command should have 2 or 4 arguments";
    }
    double[] location = new double[3];
    double r;
    try {
      r = Double.parseDouble(tokens.get(0));
    } catch (NumberFormatException e) {
      return "ERROR: first argument should be a number representing the radius";
    }
    if (tokens.size() == 2) {
      String starName = starsCmd.parseStarName(tokens.get(1));
      if (starName == null) {
        return "ERROR: "
          + "second argument should be the star name and should be surrounded in quotation marks";
      }
      Star targetStar = starsCmd.findStar(starName);
      if (targetStar == null) {
        return "ERROR: star with name " + starName + " not found";
      } else {
        location = targetStar.getCoords();
        return search(r, location[0], location[1], location[2], starName);
      }
    } else {
      //coordinates instead of star name given
      for (int i = 0; i < 3; i++) {
        try {
          location[i] = Double.parseDouble(tokens.get(i + 1));
        } catch (NumberFormatException e) {
          return "ERROR: argument " + i + " should be a number representing a coordinate";
        }
      }
      return search(r, location[0], location[1], location[2], "");
    }
  }

  /**
   * Will search all loaded stars and find those which are within a certain distance
   * from a given starting point.
   *
   * @param rad radius around the selected point where stars should be found
   * @param x x coordinate of the center point
   * @param y y coordinate of the center point
   * @param z z coordinate of the center point
   * @param name proper name of the star if the user used a name instead of coordinates.
   *             This star will not be included in the output. Empty string should be
   *             passed in to indicate that the user did not pick a specific star
   *             as the starting point.
   * @return either an message or, if successful, will contain a list of star IDs within
   * the given radius around the center point in order from closest to furthest.
   */
  public String search(double rad, double x, double y, double z, String name) {
    if (rad < 0) {
      return "ERROR: radius must be non-negative";
    }
    TreeMap<Double, List<Integer>> distances = new TreeMap<>();
    List<Star> stars = starsCmd.getStars();
    for (Star star : stars) {
      if (!(name.length() != 0 && name.equals(star.getName()))) {
        double xDif = Math.abs(x - star.getX());
        double yDif = Math.abs(y - star.getY());
        double zDif = Math.abs(z - star.getZ());
        //determines distance from center point
        double dist = Math.sqrt(xDif * xDif + yDif * yDif + zDif * zDif);
        if (dist <= rad) {
          starsCmd.addDistance(distances, star.getId(), dist);
        }
      }
    }
    if (distances.size() > 0) {
      //creates a string containing all the star IDs within the radius, separated by lines
      int distSize = distances.size();
      StringBuilder out = new StringBuilder();
      for (int i = 0; i < distSize; i++) {
        List<Integer> starIDs = distances.pollFirstEntry().getValue();
        for (Integer starID : starIDs) {
          out.append(starID).append("\n");
        }
      }
      //removes final newline character and returns
      return out.substring(0, out.length() - 1);
    } else {
      return "";
    }
  }
}
