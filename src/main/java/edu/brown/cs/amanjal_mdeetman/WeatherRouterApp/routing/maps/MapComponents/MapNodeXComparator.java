package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents;

import java.util.Comparator;

/**
 * Class to Compare Latitude of MapNodes.
 */
public class MapNodeXComparator implements Comparator<MapNode> {
  /**
   * Method compares the latitude of two nodes passed in.
   * @param o1 - node 1
   * @param o2 - node 2
   * @return - comparasion of latitude of two nodes
   */
  @Override
  public int compare(MapNode o1, MapNode o2) {
    return Double.compare(o1.getLatitude(), o2.getLatitude());
  }
}
