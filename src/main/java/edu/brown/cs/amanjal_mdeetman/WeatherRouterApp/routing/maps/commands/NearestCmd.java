package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.Cache.Cache;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.KDTree;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.MultiTreeMap;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;

import java.util.List;

/**
 * Class for NearestCmd which implements the nearest command for maps 1+2.
 */
public class NearestCmd implements ICallable {
  private final MapCmd mapCmd;
  private MapNode nearNode;
  private Cache<String, String> cacheMap;

  /**
   * Constructor for NearestCmd. Takes in a MapCmd, and creates a cacheMap.
   * @param map - map of type MapCmd.
   */
  public NearestCmd(MapCmd map) {
    cacheMap = new Cache<String, String>();
    cacheMap.build();
    mapCmd = map;
  }

  /**
   * Executes the nearest command.
   * @param tokens arguments for the command, not including the command name
   * @return  a string of the name of the nearest node
   */
  @Override
  public String run(List<String> tokens) {
    if (mapCmd.getNodeMap() == null) {
      return "ERROR: No database to use command on";
    }
    if (tokens.size() != 2) { // make sure correct num of arguments pass in
      return "ERROR: Nearest should have 2 arguments <lat> <long>";
    }
    double[] coords = new double[2];
    for (int i = 0; i < 2; i++) {
      try { // make sure first arg is a double
        coords[i] = Double.parseDouble(tokens.get(i));
      } catch (NumberFormatException e) {
        return "ERROR: argument " + (i + 1) + " should be a number representing a coordinate";
      }
    }
    String key = tokens.get(0) + " " + tokens.get(1);
    if (cacheMap.hasKey(key)) { // if in cacheMap already, use map to return
      return cacheMap.returnValue(key);
    }
    KDTree<MapNode> nodeTree = mapCmd.getNodeTree();
    MultiTreeMap<Double, MapNode> nearestNode = nodeTree.kNearestNeighbors(1, coords);
    if (nearestNode.size() == 0) { // if no neighbors
      return "";
    } else {
      nearNode = nearestNode.pollFirstEntry().getValue().get(0);
      String output = nearNode.toString(); // string to output
      // rough mem size of string
      int size = 4 * 2 * (int) ((((output.length()) * 2) + 5 * 6 + 3 * 5) / (4 * 2));
      cacheMap.setSizeOfVal(size);
      cacheMap.add(key, output); // add to cacheMap
      return output;
    }
  }
  /**
   * Method that will be used in dijsktras so we have access to the
   * nearest node found.
   * @return - a MapNode
   */
  public MapNode getNearNode() {
    return nearNode;
  }
}
