package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.Star;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Class representing the naive_neighbors command.
 */
public class NaiveNeighborsCmd implements ICallable {
  private final StarsCmd starsCmd;

  /**
   * Constructor for NaiveNeighborsCmd which initializes instance variables.
   * @param stars instance of the StarsCmd object which contains the list of stars
   *                 as well as helper methods
   */
  public NaiveNeighborsCmd(StarsCmd stars) {
    starsCmd = stars;
  }

  @Override
  public String run(List<String> tokens) {
    if (tokens.size() != 2 && tokens.size() != 4) {
      return "ERROR: naive_neighbors command should have 2 or 4 arguments";
    }
    double[] location = new double[3];
    int k;
    try {
      k = Integer.parseInt(tokens.get(0));
    } catch (NumberFormatException e) {
      return "ERROR: first argument should be a number representing the number of neighbors";
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
        return search(k, location[0], location[1], location[2], starName);
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
      return search(k, location[0], location[1], location[2], "");
    }
  }

  /**
   * Will search all loaded stars and find the k closest stars to the indicated starting point.
   *
   * @param k number of stars which the user wants to output
   * @param x x coordinate of the center point
   * @param y y coordinate of the center point
   * @param z z coordinate of the center point
   * @param name Proper name of the star if the user used a name instead of coordinates.
   *             This star will not be included in the output. Empty string should be passed
   *             in to indicate that the user did not pick a specific star as the starting point.
   * @return either an error message or, if successful, will contain a list of the k star IDs
   * which are closest to the center point in order from closest to furthest.
   */
  public String search(int k, double x, double y, double z, String name) {
    if (k < 0) {
      return "ERROR: number of neighbors must be non-negative";
    } else if (k == 0) {
      return "";
    }
    TreeMap<Double, List<Integer>> distances = new TreeMap<>();
    List<Star> stars = starsCmd.getStars();
    for (Star star : stars) {
      double xDif = Math.abs(x - star.getX());
      double yDif = Math.abs(y - star.getY());
      double zDif = Math.abs(z - star.getZ());
      //distance from the center point squared is calculated
      double distance = xDif * xDif + yDif * yDif + zDif * zDif;
      if (!(name.length() != 0 && name.equals(star.getName()))) {
        starsCmd.addDistance(distances, star.getId(), distance);
      }
    }
    if (distances.size() == 0) {
      return "";
    }
    int starsOut = 0;
    StringBuilder out = new StringBuilder();
    for (int i = 0; i < stars.size(); i++) {
      List<Integer> starIDs = distances.pollFirstEntry().getValue();
      if (starsOut + starIDs.size() > k) {
        //randomizes the list to remove bias when only a portion of the list will be used
        Collections.shuffle(starIDs);
        int tieNum = k - starsOut;
        for (int j = 0; j < tieNum; j++) {
          out.append(starIDs.get(j)).append("\n");
          starsOut++;
        }
      } else {
        for (Integer starID : starIDs) {
          out.append(starID).append("\n");
          starsOut++;
        }
      }
      if (starsOut == k || distances.size() == 0) {
        return out.substring(0, out.length() - 1);
      }
    }
    return out.substring(0, out.length() - 1);
  }
}
