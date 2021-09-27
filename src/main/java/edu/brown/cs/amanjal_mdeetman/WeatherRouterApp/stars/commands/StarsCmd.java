package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.KDTree;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.csv.CSVParser;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.Star;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.StarXComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Class representing the stars command.
 */
public class StarsCmd implements ICallable {
  private List<Star> stars;
  private HashMap<Integer, Star> starMap;
  private final KDTree<Star> starsTree;

  /**
   * Constructor for StarsCmd which initializes instance variables.
   */
  public StarsCmd() {
    stars = new ArrayList<>();
    starsTree = new KDTree<>(3);
    starMap = new HashMap<>();
  }

  /**
   * Will take in a csv file containing stars, parse it, and store a list of Star objects.
   *
   * @param tokens which should just be the name of a csv file which should contain stars
   * @return String which will either contain an error message or, if successful, will
   * indicate the number of stars loaded in.
   */
  @Override
  public String run(List<String> tokens) {
    if (tokens.size() != 1) {
      return "ERROR: stars command should have 1 argument: <csv>";
    }
    String filename = tokens.get(0);
    stars = new ArrayList<>();
    CSVParser csvParser = new CSVParser();
    String error = csvParser.parse(filename, true);
    if (error.length() > 0) {
      return error;
    }
    //checks, using the header that the csv file parsed contains stars in the correct format
    String[] header = csvParser.getHeader();
    if (!Arrays.toString(header).equals("[StarID, ProperName, X, Y, Z]")) {
      return "ERROR: csv should have header \"StarID,ProperName,X,Y,Z\"";
    }
    List<String[]> csvList = csvParser.getList();
    for (String[] starValues : csvList) {
      Star star = addStar(starValues);
      if (star == null) {
        return "ERROR: could not add star";
      } else {
        stars.add(star);
      }
    }
    setStars(stars);
    if (stars.size() > 0) {
      //sorts star list and adds the stars to the KDTree, with the median as the root
      List<Star> starsCpy = new ArrayList<>(stars);
      starsCpy.sort(new StarXComparator());
      int midPoint = (starsCpy.size() - 1) / 2;
      Star median = starsCpy.get(midPoint);
      starsCpy.remove(midPoint);
      starsTree.clear();
      starsTree.insert(median);
      for (Star star : starsCpy) {
        starsTree.insert(star);
      }
    }
    return String.format("Read %d stars from %s", stars.size(), filename);
  }

  /**
   * Will create a new instance of Star and store it in the list of Star objects.
   *
   * @param values an array of strings representing the star ID, proper name and the
   *               coordinates representing the star location
   * @return the added Star, returns null if an error occurred
   */
  public Star addStar(String[] values) {
    int starID;
    double xPos;
    double yPos;
    double zPos;
    try {
      starID = Integer.parseInt(values[0]);
    } catch (NumberFormatException e) {
      return null;
    }
    String properName = values[1];
    try {
      xPos = Double.parseDouble(values[2]);
    } catch (NumberFormatException e) {
      return null;
    }
    try {
      yPos = Double.parseDouble(values[3]);
    } catch (NumberFormatException e) {
      return null;
    }
    try {
      zPos = Double.parseDouble(values[4]);
    } catch (NumberFormatException e) {
      return null;
    }
    return new Star(starID, properName, xPos, yPos, zPos);
  }

  /**
   * When given a star name as an argument, will determine it's validity and return
   * the relevant portion of the argument.
   * @param starName star name which should be surrounded by double quotes
   * @return star name without double quotes or null if argument is invalid.
   */
  public String parseStarName(String starName) {
    int strLen = starName.length();
    if (starName.charAt(0) == '"' && starName.charAt(strLen - 1) == '"') {
      return starName.substring(1, strLen - 1);
    } else {
      return null;
    }
  }

  /**
   * Finds the location of a star given its proper name.
   *
   * @param name the proper name a star which has been loaded
   * @return the Star object specified by the name
   */
  public Star findStar(String name) {
    if (name.length() != 0) {
      for (Star star : stars) {
        if (star.getName().equals(name)) {
          return star;
        }
      }
    }
    return null;
  }

  /**
   * Adds the distance of the star to the TreeMap.
   *
   * @param distances TreeMap with distance as the key and a list of star IDs that distance
   *                  away as the value
   * @param id the star ID
   * @param dist distance the star is from the center point
   */
  public void addDistance(TreeMap<Double, List<Integer>> distances, int id, double dist) {
    if (distances.get(dist) == null) {
      List<Integer> starIDs = new ArrayList<>();
      starIDs.add(id);
      distances.put(dist, starIDs);
    } else {
      distances.get(dist).add(id);
    }
  }

  /**
   * getter method for the number of stars currently loaded.
   *
   * @return number of loaded stars
   */
  public int getNumStars() {
    return this.stars.size();
  }

  /**
   * getter method for the List of Star objects.
   *
   * @return List of currently loaded Star objects
   */
  public List<Star> getStars() {
    return this.stars;
  }

  /**
   * Gets the k-d tree where the stars are stored.
   * @return k-d tree containing stars
   */
  public KDTree<Star> getStarsTree() {
    return this.starsTree;
  }

  /**
   * Sets the list of stars.
   * @param starList list containing stars
   */
  public void setStars(List<Star> starList) {
    this.stars = starList;
    starMap.clear();
  }

  /**
   * Creates a hash map mapping ID to star object.
   * @return the hash map just created
   */
  public HashMap<Integer, Star> createHash() {
    starMap.clear();
    for (Star star : this.stars) {
      starMap.put(star.getId(), star);
    }
    return starMap;
  }

  /**
   * Returns a hash map of stars.
   * @return a hash map of stars mapping ID to star object
   */
  public HashMap<Integer, Star> getHash() {
    return starMap;
  }
}
