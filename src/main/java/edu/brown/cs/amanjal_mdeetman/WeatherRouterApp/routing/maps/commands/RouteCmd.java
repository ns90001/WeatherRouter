package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.Cache.Cache;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra.DirectedEdge;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.Way;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra.Dijkstra;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * RouteCmd class which handles the route user command.
 */
public class RouteCmd implements ICallable {
  private final MapCmd mapCmd;
  private List<List<Double>> returnList = new ArrayList<>();
  private List<Way> wayList = new ArrayList<>();
  private List<Way> firstRouteList = new ArrayList<>();
  private Cache<List<String>, String> cacheMap;

  /**
   * Constructor for RouteCmd which initializes instance variables.
   *
   * @param map the MapCmd instance being used which stores the database connection
   *            as well as a hash map of nodes.
   */
  public RouteCmd(MapCmd map) {
    mapCmd = map;
    cacheMap = new Cache<List<String>, String>();
    cacheMap.build();
  }

  /**
   * Execute method for the RouteCmd command.
   *
   * @param tokens arguments for the command, not including the command name
   * @return the string to be outputted to the user. Either an error message or
   * the path between the specified nodes.
   */
  @Override
  public String run(List<String> tokens) {
    if (tokens.size() != 4) {
      return "ERROR: route command should have 4 arguments";
    }
//    if (cacheMap.hasKey(tokens)) {
//      return cacheMap.returnValue(tokens);
//    }
    Dijkstra<MapNode, Way> dijkstra = new Dijkstra<>();
    MapNode start;
    MapNode end;
    //checks if street names are given rather than coordinates
    if (tokens.get(0).contains("\"")) {
      // for loop to make sure all args are street names
      for (int i = 1; i < tokens.size(); i++) {
        if (!tokens.get(i).contains("\"")) {
          return "ERROR: Invalid Inputs";
        }
      }
      //will contain the start and end node
      MapNode[] nodes = new MapNode[2];
      Connection conn = mapCmd.getDatabaseConn();
      for (int i = 0; i < 2; i++) {
        try {
          //queries the database for a list of nodes which appear in both named ways
          PreparedStatement prep = conn.prepareStatement("SELECT intersection\n"
            + "FROM (SELECT *,\n"
            + "CASE WHEN way1.start = way2.start THEN way1.start\n"
            + "\t WHEN way1.end = way2.end THEN way1.end\n"
            + "\t WHEN way1.start = way2.end THEN way1.start\n"
            + "\t WHEN way1.end = way2.start THEN way1.end\n"
            + "\t ELSE NULL END AS intersection\n"
            + "FROM way as way1, way as way2\n"
            + "WHERE way1.name = ? AND way2.name = ?\n"
            + "GROUP BY intersection)");
          //sets the names of the ways which will be queried to be the user's arguments
          String way1 = tokens.get(2 * i);
          String way2 = tokens.get((2 * i) + 1);
          prep.setString(1, way1.substring(1, way1.length() - 1));
          prep.setString(2, way2.substring(1, way2.length() - 1));
          ResultSet rs = prep.executeQuery();
          if (!rs.isClosed()) {
            if (rs.next()) {
              nodes[i] = mapCmd.getNode(rs.getString(1));
            } else {
              prep.close();
              rs.close();
              return "ERROR: no valid intersection found";
            }
          }
          prep.close();
          rs.close();
        } catch (SQLException e) {
          return "ERROR: could not query database";
        }
      }
      start = nodes[0];
      end = nodes[1];
    } else {
      // for loop to make sure all other arguments are numbers not street names
      for (int i = 1; i < tokens.size(); i++) {
        if (tokens.get(i).contains("\"")) {
          return "ERROR: Invalid Inputs";
        }
      }
      //finds the node closest to the specified coordinates using the NearestCmd class
      NearestCmd nearestCmd = new NearestCmd(mapCmd);
      List<String> startArgs = new ArrayList<>(tokens.subList(0, 2));
      nearestCmd.run(startArgs);
      start = nearestCmd.getNearNode();
      List<String> endArgs = new ArrayList<>(tokens.subList(2, 4));
      nearestCmd.run(endArgs);
      end = nearestCmd.getNearNode();
    }
    // make sure nulls are not passed into dijsktra
    if (start == null) {
      return "ERROR: Street 1 and Cross street 1 do not intersect";
    } else if (end == null) {
      return "ERROR: Street 2 and Cross street 2 do not intersect";
    } else if (start == end) {
      return start.toString();
    }
    //finds the shortest path between the start and end node
    // wayList consist of allWays we need to be aware of
    dijkstra.shortestPath(start, end, true, wayList);
    String output = outputPath(start, end);
    dijkstra.resetEdge();
    // rough size of string
    int size = 4 * 2 * (int) ((((output.length()) * 2) + 6 * 5 + 3 * 5) / (4 * 2));
    cacheMap.setSizeOfVal(size);
    cacheMap.add(tokens, output); // add k,v pair to cacheMap
    return output;
  }

  /**
   * Method which, given a start and end node which has been put through Dijkstra's algorithm,
   * will produce a string representing the path between these nodes.
   *
   * @param start start node
   * @param end   end node
   * @return path between the nodes if one exists. If not, -/- will be outputted to indicate no path
   */
  public String outputPath(MapNode start, MapNode end) {
    List<String> outList = new ArrayList<>();
    String noPath = start.toString() + " -/- " + end.toString();
    //occurs if only path is through an non-traversable way
    if (end.getDistance() == Double.POSITIVE_INFINITY) {
      return noPath;
    }
    MapNode curr = end;
    List<Double> preList = new ArrayList<>();
    preList.add(end.getCoords()[0]);
    preList.add(end.getCoords()[1]);
    returnList.add(preList);
    while (curr != start) {
      MapNode prev = curr.getPrevNode();
      if (prev != null) {
        List<Double> loopList = new ArrayList<>();
        loopList.add(prev.getCoords()[0]);
        loopList.add(prev.getCoords()[1]);
        returnList.add(loopList);
      }
      //if prev is null but the start node has not been found, then there must be no valid path
      if (prev == null) {
        return noPath;
      }
      outList.add(prev.toString() + " -> " + curr.toString() + " : "
        + curr.getPrevEdge().toString());
      firstRouteList.add(0, curr.getPrevEdge()); // add edge to beginning of firstRouteList
      curr = prev;
    }
    StringBuilder out = new StringBuilder();
    //iterates through list backwards
    for (int i = outList.size() - 1; i >= 0; i--) {
      out.append(outList.get(i));
      out.append('\n');
    }

    return out.substring(0, out.length() - 1);
  }

  /**
   * set the wayList that is passed into Dijsktras to the inputted data
   * @param newWayList - list of ways
   */
  public void setWays(List<Way> newWayList) {
    wayList = newWayList;
  }

  /**
   * Get the ways that make up the route
   * @return - list of ways
   */
  public List<Way> getFirstRouteList() {
    return firstRouteList;
  }

  /**
   * Method used to clear the firstRouteList from RouteCreator
   * @param newWayList - list of ways
   */
  public void setFirstRouteList(List<Way> newWayList) {
    firstRouteList = newWayList;
  }

  /**
   * Method used to see whether we need to look for weather data in routeCreator
   * @param bool -- boolean
   */
}