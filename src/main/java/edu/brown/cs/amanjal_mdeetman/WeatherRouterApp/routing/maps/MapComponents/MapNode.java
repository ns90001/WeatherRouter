package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.KDPoint;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra.DirectedNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Node class containing info on each node from database.
 */
public class MapNode extends DirectedNode<MapNode, Way> implements KDPoint  {
  private final double latitude;
  private final double longitude;
  private final double[] coords;
  private final String id;
  private final MapCmd mapCmd;
  private final Connection conn;
  private Set<Way> outgoing;
  private double residentialSpeed = 20;
  private double primarySpeed = 55;
  private double secondarySpeed = 40;
  private double motorwaySpeed = 200;
  private double serviceSpeed = 40;
  private double motorway_linkSpeed = 100;
  private double weatherHeuristic = 0;
  /**
   * Constructor for MapNode which initializes instance variables.
   * @param lat latitude of this node
   * @param lon longitude of this node
   * @param nodeId id of this node (including /n/)
   * @param map MapCmd object which has the database connection
   */
  public MapNode(double lat, double lon, String nodeId, MapCmd map) {
    latitude = lat;
    longitude = lon;
    coords = new double[]{lat, lon};
    id = nodeId;
    mapCmd = map;
    conn = map.getDatabaseConn();
    outgoing = null;
  }

  /**
   * Method returns Latitude of Node.
   * @return - double latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * Method returns Longitude of Node.
   * @return - double longitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * Method returns the coordinates of Node.
   * @return - double array of coordinates.
   */
  @Override
  public double[] getCoords() {
    return coords;
  }

  /**
   * Method returns distance from Node to point passed in.
   * @param point another KDPoint
   * @return - double distance
   */
  @Override
  public double getDist(KDPoint point) {
    double[] compCoords = point.getCoords();
    return calcDist(compCoords);
  }

  /**
   * Method returns distance to coordinates passed in.
   * @param coordinates - double distance.
   * @return
   */
  @Override
  public double getDist(double[] coordinates) {
    return calcDist(coordinates);
  }

  /**
   * Method returns id of node (always 0).
   * @return - 0
   */
  @Override
  public int getId() {
    return 0;
  }

  /**
   * Gets all the ways for which this node is the start.
   * @return a set of all outgoing ways. Returns null on error.
   * @param edgeList
   */
  @Override
  public Set<Way> getOutgoingEdges(List<Way> edgeList) {

    //if this method has not been called yet
    if (outgoing == null) {
      outgoing = new HashSet<>();
      try {
        //queries the database for ways where this node is the start
        PreparedStatement prep = conn.prepareStatement(
          "SELECT way.id, way.type, way.end\n"
            + "FROM way\n"
            + "WHERE way.start = ?");
        prep.setString(1, this.toString());
        ResultSet rs = prep.executeQuery();
        if (!rs.isClosed()) {
          while (rs.next()) {
            double traversable = 0;
            String type = rs.getString(2);
            if (type.equals("") || type.equals("unclassified")) {
              traversable = 0;
            } else if (type.equals("track")) {
              traversable = 10;
            } else if (type.equals("service")) {
              traversable = serviceSpeed;
            } else if (type.equals("secondary")) {
              traversable = secondarySpeed;
            } else if (type.equals ("residential")) {
              traversable = residentialSpeed;
            } else if (type.equals ("primary")) {
              traversable = primarySpeed;
            } else if (type.equals("path")) {
              traversable = 5;
            } else if (type.equals("motorway_link")) {
              traversable = motorway_linkSpeed;
            } else if (type.equals("motorway")) {
              traversable = motorwaySpeed;
            } else if (type.equals("footway")) {
              traversable = 10;
            } else if (type.equals("cycleway")) {
              traversable = 5;
            } else {
              traversable = 0;
            }
            String endNode = rs.getString(3);
              if(mapCmd.getNode(endNode) != null) {
                Way thisWay = new Way(this, mapCmd.getNode(endNode), rs.getString(1), traversable);
                outgoing.add(thisWay);
              }
          }
        }
        prep.close();
        rs.close();
      } catch (SQLException e) {
        System.out.println("null");
        return null;
      }
    }
    return this.outgoing;
  }

  /**
   * Method to set the outgoing edges of the MapNode.
   * @param outgoin - a Set of Ways
   */
  public void setOutgoingEdges(Set<Way> outgoin) {
    this.outgoing = outgoin;
  }

  /**
   * To string method for the node.
   * @return the id of the node.
   */
  @Override
  public String toString() {
    return this.id;
  }
  /**
   * Calculates the distance between the MapNode and the specified coordinates.
   * @param compCoords destination coords, the distance to which will be found.
   * @return the square of the distance.
   */
  private double calcDist(double[] compCoords) {
    if (compCoords.length != coords.length) {
      return -1;
    }
    double dist = 0;
    for (int i = 0; i < coords.length; i++) {
      double diff = coords[i] - compCoords[i];
      dist += diff * diff;
    }
    //square of the distance returned to reduce computations.
    return dist;
  }

  public String getNodeId() {
    return id;
  }

  @Override
  public void setWeatherHeuristic(double weatherHeuristic) {
    this.weatherHeuristic = weatherHeuristic;
  }
}
