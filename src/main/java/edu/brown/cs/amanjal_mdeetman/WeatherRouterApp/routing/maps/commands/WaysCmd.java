package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.Cache.Cache;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra.DirectedEdge;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.Way;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for the WayCmd which is used to find all the ways inside a bounding box.
 */
public class WaysCmd implements ICallable {
  private final MapCmd mapCmd;
  private List<String> returnList = new ArrayList<>();
  private Cache<String, String> cacheMap;
  private HashMap<String, List<String>> wayToNode = new HashMap<>();
  private List<Way> updatedList = new ArrayList<>();
  private HashMap<String, Boolean> tempHash = new HashMap<>();
  private static final int SECONDLAST = 7;
  private static final int LAST = 8;
  private double residentialSpeed = 20;
  private double primarySpeed = 55;
  private double secondarySpeed = 40;
  private double motorwaySpeed = 200;
  private double serviceSpeed = 40;
  private double motorway_linkSpeed = 100;

  /**
   * Constructor for WaysCmd, makes a new Cache and sets mapCmd.
   * @param map - of type MapCmd.
   */
  public WaysCmd(MapCmd map) {
    mapCmd = map;
    cacheMap = new Cache<String, String>();
    cacheMap.build();
  }

  /**
   * Method to execute WaysCmd. Takes in the argument and uses an SQL query to
   * find all the ways inside a certain region.
   * @param tokens arguments for the command, not including the command name
   * @return - a string output
   */
  @Override
  public String run(List<String> tokens) {
    if (tokens.size() != 4) {
      return "ERROR: ways command should have 4 arguments: <lat1> <long1> <lat2> <long2>";
    }
    //stores the 4 latitude/longitude inputs from the user [lat1, long1, lat2, long2]
    double[] box = new double[4];
    //stores the min and max latitude
    double[] lat = new double[2];
    //stores the min and max longitude
    double[] lon = new double[2];
    List<String> ways = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      try {
        box[i] = Double.parseDouble(tokens.get(i));
      } catch (NumberFormatException e) {
        return "ERROR: argument " + (i + 1)
          + " should be a number representing a latitude or longitude";
      }
    }
    lat[0] = Math.min(box[0], box[2]);
    lat[1] = Math.max(box[0], box[2]);
    lon[0] = Math.min(box[1], box[3]);
    lon[1] = Math.max(box[1], box[3]);
    Connection conn = mapCmd.getDatabaseConn();
    if (conn == null) {
      return "ERROR: database is not yet set";
    }
    String key = tokens.get(0) + " " + tokens.get(1) + " " + tokens.get(2) + " " + tokens.get(3);
//    if (cacheMap.hasKey(key)) { // if cache has key, use map to print
//      return cacheMap.returnValue(key);
//    }
    try {

      //gets the id of ways with a start or end node in the bounding box
      PreparedStatement prep = conn.prepareStatement("SELECT * "
          + "FROM (SELECT way.id, n1.latitude as startlat, n1.longitude as startlong,"
          + "n2.latitude as endlat, n2.longitude as endlong, way.type "
          + "FROM way, node as n1, node as n2 "
          + "WHERE way.start = n1.id and way.end = n2.id) "
          + "WHERE (startlat BETWEEN ? AND ? "
          + "AND startlong BETWEEN ? AND ?) "
          + "OR (endlat BETWEEN ? AND ? "
          + "AND endlong BETWEEN ? AND ?) "
          + "ORDER BY id");

      prep.setDouble(1, lat[0]);
      prep.setDouble(2, lat[1]);
      prep.setDouble(5, lat[0]);
      prep.setDouble(6, lat[1]);
      prep.setDouble(3, lon[0]);
      prep.setDouble(4, lon[1]);
      prep.setDouble(SECONDLAST, lon[0]);
      prep.setDouble(LAST, lon[1]);

      ResultSet rs = prep.executeQuery();
      if (!rs.isClosed()) {
        int counter = 0;
        while (rs.next()) {
          counter++;
          List<String> twoNode = new ArrayList<>();
          String id = rs.getString(1);
          String type = rs.getString(6);
          if(!tempHash.containsKey(id) && !(type.equals("") || type.equals("unclassified"))) {
            NearestCmd nearestCmd = new NearestCmd(mapCmd);

            String startLat = rs.getString(2);
            String startLong = rs.getString(3);
            List<String> startNear = new ArrayList<>();
            startNear.add(startLat); startNear.add(startLong);
            nearestCmd.run(startNear);
            MapNode startNode = nearestCmd.getNearNode();

            String endLat = rs.getString(4);
            String endLong = rs.getString(5);
            List<String> endNear = new ArrayList<>();
            endNear.add(endLat); startNear.add(endLong);
            nearestCmd.run(endNear);
            MapNode endNode = nearestCmd.getNearNode();

            double traversable = 0;

            if (type.equals("track")) {
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
              traversable = 5;
            } else if (type.equals("cycleway")) {
              traversable = 5;
            } else {
              traversable = 0;
            }

            Way newWay = new Way(startNode, endNode, id, traversable);
            updatedList.add(newWay);

          }
          ways.add(id);

          twoNode.add(rs.getString(6));
          twoNode.add(rs.getString(2));
          twoNode.add(rs.getString(3));
          twoNode.add(rs.getString(4));
          twoNode.add(rs.getString(5));

          // this if for our front end when we return a list of coordinates
          this.returnList.add(rs.getString(6));
          this.returnList.add(rs.getString(2));
          this.returnList.add(rs.getString(3));
          this.returnList.add(rs.getString(4));
          this.returnList.add(rs.getString(5));

          wayToNode.put(rs.getString(1), twoNode);

        }
      }
      prep.close();
      rs.close();

    } catch (SQLException e) {
      return "ERROR: could not query the database";
    }
    StringBuilder out = new StringBuilder();
    //adds all the way ids to the output string
    for (String way : ways) {
      out.append(way);
      out.append('\n');
    }
    if (out.length() == 0) { // when no ways are in the box
      return "ERROR: No ways in box";
    }
    String output = out.substring(0, out.length() - 1);
    int size = 4 * 2 * (int) ((((output.length()) * 2)
      + 5 * 5 + 4 * 5) / (4 * 2)); // rough size of string
    cacheMap.setSizeOfVal(size);
    cacheMap.add(key, output); // add k,v pair to cacheMap
    return output;
  }

  /**
   * Returns a hashmap of ways mapping to nodes used for the gui in the frontend.
   * @return - a hashmap.
   */
  public HashMap<String, List<String>> returnNodes() {
    return this.wayToNode;
  }

  /**
   * Returns the nodes of the ways as a string.
   * @return - a string.
   */
  public String returnNodesString() {
    String s = "";
    for (String coord: this.returnList) {
      s += coord + " ";
    }
    return s.substring(0, s.length() - 1);
  }

  public List<Way> updatedWays(Double lat1, Double long1, Double lat2, Double long2, List<Way> oldList){
    String one = "" + lat1;
    String two = "" + long1;
    String three = "" + lat2;
    String four = "" + long2;
    List<String> tokens = new ArrayList<>();
    tokens.add(one); tokens.add(two); tokens.add(three); tokens.add(four);
    tempHash = new HashMap<>();
    for(int i = 0; i < oldList.size(); i++) {
      tempHash.put(oldList.get(i).toString(), true);
    }
    updatedList = oldList;
    this.run(tokens);
    return updatedList;
  }
}
