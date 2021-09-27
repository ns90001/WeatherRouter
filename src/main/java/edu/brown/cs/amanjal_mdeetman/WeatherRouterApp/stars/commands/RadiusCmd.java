package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.MultiTreeMap;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.KDPoint;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.KDTree;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.Star;

import java.util.List;
import java.util.Map;

/**
 * Class representing the radius command.
 */
public class RadiusCmd implements ICallable {
  private final StarsCmd starsCmd;

  /**
   * Constructor for RadiusCmd which initializes instance variables.
   * @param stars instance of the StarsCmd object which contains the list of stars
   *                 as well as helper methods
   */
  public RadiusCmd(StarsCmd stars) {
    starsCmd = stars;
  }

  /**
   * Executes the radius command.
   *
   * @param tokens arguments for the radius command
   * @return output of the stars within r from the target sorted from closest to
   * farthest, or an error message if an error occurs
   */
  @Override
  public String run(List<String> tokens) {
    if (tokens.size() != 2 && tokens.size() != 4) {
      return "ERROR: radius command should have 2 or 4 arguments";
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
        return search(r, targetStar.getCoords(), targetStar.getId());
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
      return search(r, location, -1);
    }
  }

  /**
   * Performs the radius search with a k-d tree.
   *
   * @param r radius around the target where stars should be found
   * @param location the target location
   * @param id the id of the star which should be excluded from the output. Should be
   *           -1 if all stars should be included
   * @return output of the stars within a set radius from the target
   */
  public String search(double r, double[] location, int id) {
    if (r < 0) {
      return "ERROR: radius must be non-negative";
    }
    KDTree starsTree = starsCmd.getStarsTree();
    MultiTreeMap<Double, Star> nearStars = starsTree.radiusSearch(r, location, id);
    if (nearStars.size() == 0) {
      return "";
    }
    StringBuilder out = new StringBuilder();
    while (true) {
      Map.Entry<Double, List<Star>> minEntry = nearStars.pollFirstEntry();
      if (minEntry == null) {
        return out.substring(0, out.length() - 1);
      }
      for (KDPoint star : minEntry.getValue()) {
        out.append(star.getId()).append("\n");
      }
    }
  }
}
