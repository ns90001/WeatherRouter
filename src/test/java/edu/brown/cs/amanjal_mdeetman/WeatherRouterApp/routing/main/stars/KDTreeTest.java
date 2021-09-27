package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.stars;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.KDTree;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.Star;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.StarXComparator;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.text.DecimalFormat;
import java.util.*;

public class KDTreeTest {

  /**
   * Fluff testing for neighbors. Will return a comprehensive error message if a bug is found
   */
  @Test
  public void randomNeighborsTest() {
    //testing done with 1000000 repeats
    int numRepeats = 1000;
    int maxStars = 100;
    for (int n = 0; n < numRepeats; n++) {
      StarsCmd starsCmd = new StarsCmd();
      Random random = new Random();
      int numStars = random.nextInt(maxStars) + 1;
      HashMap<Integer, Star> starsMap = getRandomStars(starsCmd, numStars);
      DecimalFormat df = new DecimalFormat("0.#");
      NeighborsCmd neighborsCmd = new NeighborsCmd(starsCmd);
      NaiveNeighborsCmd naiveNeighborsCmd = new NaiveNeighborsCmd(starsCmd);
      int k = random.nextInt(numStars + 1);
      double[] location = new double[3];
      for (int i = 0; i < 3; i++) {
        location[i] = Double.parseDouble(df.format(random.nextDouble() * 10 - 5));
      }
      String neighborsOut = neighborsCmd.search(k, location, -1);
      String naiveNeighborsOut = naiveNeighborsCmd.search(k, location[0], location[1], location[2], "");
      String[] neighborsStars = neighborsOut.split("\n");
      String[] naiveNeighborsStars = naiveNeighborsOut.split("\n");
      if (k == 0) {
        assertEquals(0, neighborsOut.length());
        assertEquals(0, naiveNeighborsOut.length());
        continue;
      } else if (k == 1) {
        neighborsStars = new String[]{neighborsOut};
        naiveNeighborsStars = new String[]{naiveNeighborsOut};
      }
      double neighborsDist = 0;
      double naiveNeighborsDist = 0;
      for (String neighborsStar : neighborsStars) {
        int starID;
        try {
          starID = Integer.parseInt(neighborsStar);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("num neighbors: " + k);
        }
        Star star = starsMap.get(starID);
        neighborsDist += star.getDist(location);
      }
      for (String naiveNeighborsStar : naiveNeighborsStars) {
        int starID = Integer.parseInt(naiveNeighborsStar);
        Star star = starsMap.get(starID);
        naiveNeighborsDist += star.getDist(location);
      }
      neighborsDist = Double.parseDouble(df.format(neighborsDist));
      naiveNeighborsDist = Double.parseDouble(df.format(naiveNeighborsDist));
      if (Math.abs(neighborsDist - naiveNeighborsDist) > 0.11) {
        StringBuilder out = new StringBuilder();
        List<Star> starsList = starsCmd.getStars();
        for (Star star : starsList) {
          out.append("ID: ").append(star.getId()).append(" Location: ").append(Arrays.toString(star.getCoords())).append(", ");
        }
        throw new RuntimeException("Location: " + Arrays.toString(location) + " Input: " + out +
          " naive neighbors output: " + naiveNeighborsOut + " neighbors output: " + neighborsOut);
      }
    }
  }

  /**
   * Fluff testing for radius. Will return a comprehensive error message if a bug is found
   */
  @Test
  public void randomRadiusTest() {
    //testing done using 1000000
    int numRepeats = 1000;
    int maxStars = 100;
    for (int n = 0; n < numRepeats; n++) {
      StarsCmd starsCmd = new StarsCmd();
      Random random = new Random();
      DecimalFormat df = new DecimalFormat("0.#");
      int numStars = random.nextInt(maxStars) + 1;
      HashMap<Integer, Star> starsMap = getRandomStars(starsCmd, numStars);
      RadiusCmd radiusCmd = new RadiusCmd(starsCmd);
      NaiveRadiusCmd naiveRadiusCmd = new NaiveRadiusCmd(starsCmd);
      double r = random.nextDouble() * 9;
      double[] location = new double[3];
      for (int i = 0; i < 3; i++) {
        location[i] = Double.parseDouble(df.format(random.nextDouble() * 10 - 5));
      }
      String radiusOut = radiusCmd.search(r, location, -1);
      String naiveRadiusOut = naiveRadiusCmd.search(r, location[0], location[1], location[2], "");
      String[] radiusStars = radiusOut.split("\n");
      String[] naiveRadiusStars = naiveRadiusOut.split("\n");
      if (radiusOut.length() == 0) {
        assertEquals(0, naiveRadiusOut.length());
        continue;
      } else if (radiusStars.length == 0) {
        radiusStars = new String[]{radiusOut};
        naiveRadiusStars = new String[]{naiveRadiusOut};
      }
      double radiusDist = 0;
      double naiveRadiusDist = 0;
      for (String radiusStar : radiusStars) {
        int starID;
        try {
          starID = Integer.parseInt(radiusStar);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("radius: " + r);
        }
        Star star = starsMap.get(starID);
        radiusDist += star.getDist(location);
      }
      for (String naiveRadiusStar : naiveRadiusStars) {
        int starID = Integer.parseInt(naiveRadiusStar);
        Star star = starsMap.get(starID);
        naiveRadiusDist += star.getDist(location);
      }
      naiveRadiusDist = Double.parseDouble(df.format(naiveRadiusDist));
      radiusDist = Double.parseDouble(df.format(radiusDist));
      if (Math.abs(radiusDist - naiveRadiusDist) > 0.11) {
        StringBuilder out = new StringBuilder();
        List<Star> starsList = starsCmd.getStars();
        for (Star star : starsList) {
          out.append("ID: ").append(star.getId()).append(" Location: ").append(Arrays.toString(star.getCoords())).append(", ");
        }
        throw new RuntimeException("Location: " + Arrays.toString(location) + " Input: " + out +
          " naive radius output: " + naiveRadiusOut + " radius output: " + radiusOut + " naive dist: "
          + naiveRadiusDist + " KD dist: " + radiusDist);
      }
    }
  }

  public HashMap<Integer, Star> getRandomStars(StarsCmd starsCmd, int numStars) {
    HashMap<Integer, Star> starsMap = new HashMap<>();
    Random random = new Random();
    List<Star> starsList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.#");
    for (int i = 0; i < numStars; i++) {
      String randX = df.format(random.nextDouble() * 10 - 5);
      String randY = df.format(random.nextDouble() * 10 - 5);
      String randZ = df.format(random.nextDouble() * 10 - 5);
      Star newStar = starsCmd.addStar(new String[]{Integer.toString(i), "", randX, randY, randZ});
      starsList.add(newStar);
      starsMap.put(i, newStar);
    }
    starsCmd.setStars(starsList);
    List<Star> starsCpy = new ArrayList<>(starsList);
    starsCpy.sort(new StarXComparator());
    int midPoint = (starsCpy.size() - 1) / 2;
    Star median = starsCpy.get(midPoint);
    starsCpy.remove(midPoint);
    KDTree<Star> starsTree = starsCmd.getStarsTree();
    starsTree.clear();
    starsTree.insert(median);
    for (Star star : starsCpy) {
      starsTree.insert(star);
    }
    return starsMap;
  }


}
