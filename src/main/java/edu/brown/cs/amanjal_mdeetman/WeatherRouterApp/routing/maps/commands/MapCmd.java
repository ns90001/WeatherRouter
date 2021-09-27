package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.Cache.Cache;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree.KDTree;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNodeXComparator;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Class for MapCmd, creates database connection used for other commands.
 */
public class MapCmd implements ICallable {
  private static Connection conn;
  private KDTree<MapNode> nodeTree;
  private Map<String, MapNode> nodeMap;
  private Cache<String, String> cacheMap;
  /**
   * Constructor for MapCmd, created cacheMap and new conn.
   */
  public MapCmd() {
    conn = null;
    cacheMap = new Cache<String, String>();
    cacheMap.build();
  }
  /**
   * Loads the database and stores the connection.
   * @param tokens arguments for the command, not including the command name
   * @return String to be outputted to the repl.
   */
  @Override
  public String run(List<String> tokens) {
    if (tokens.size() != 1) {
      return "ERROR: map command should have 1 argument: <path/to/database>";
    }
    if (cacheMap.hasKey(tokens.get(0))) {
      return cacheMap.returnValue(tokens.get(0));
    }
    String dbName = tokens.get(0);
    if (!Files.exists(Path.of(dbName))) {
      return "ERROR: cannot find a database with path " + dbName;
    }
    try {
      initDatabase(dbName);
      if (!this.checkTables()) { // make sure correct tables in file return error if not
        return "ERROR: Wrong Tables in Database";
      }
      if (!this.checkColumnsNodeTable()) { // make sure Node table has correct columns
        return "ERROR: Columns of node table are incorrect";
      }
      if (!this.checkColumnsWayTable()) { // make sure way table has correct columns
        return "ERROR: Columns of way table are incorrect";
      }
      cacheMap.clean();
      cacheMap.add(dbName, "Database already loaded in");
    } catch (Exception e) {
      return "ERROR: could not open the database " + dbName;
    }
    try {
      this.kdTreeSetUp();
    } catch (Exception e) {
      return "ERROR: could not add nodes to k-d tree";
    }
    return "map set to " + dbName;
  }

  /**
   * Initializes the database name.
   * @param dbName the path of the database
   * @throws ClassNotFoundException from the Class.forName method
   * @throws SQLException from the getConnection, createStatement and executeUpdate methods
   */
  private void initDatabase(String dbName) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + dbName;
    conn = DriverManager.getConnection(urlToDB);
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
  }

  /**
   * Method uses the node table to create MapNodes, with the node name
   * the latitude and longitude, and then adds these MapNodes to the
   * nodeList, and also creates a KDTree with the nodeList. Also makes sure
   * that the node is traversable (is either a start or an end in the ways table).
   * @throws SQLException from process in method.
   */
  private void kdTreeSetUp() throws SQLException {
    nodeMap = new HashMap<>();
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM node, way "
        + "WHERE ((node.id  = way.start OR node.id = way.end) AND (way.type != '' AND way.type != 'unclassified'))");
    ResultSet rs = prep.executeQuery();
    if (!rs.isClosed()) {
      while (rs.next()) { // loop through and get node lat and long
        MapNode newMapNode = new MapNode(rs.getDouble(2), rs.getDouble(3), rs.getString(1), this);
        nodeMap.put(newMapNode.toString(), newMapNode);
      }
    }
    prep.close();
    rs.close();
    // if more than 0 nodes in database, then add all nodes to kd tree
    if (nodeMap.size() > 0) {
      List<MapNode> nodeListCpy = new ArrayList<>(nodeMap.values());
      nodeTree = new KDTree<>(2); // lat and long = 2D
      Comparator<MapNode> mapNodeXComparator = new MapNodeXComparator();
      nodeListCpy.sort(mapNodeXComparator);
      int midPoint = (nodeListCpy.size() - 1) / 2;
      MapNode median = nodeListCpy.get(midPoint);
      nodeListCpy.remove(midPoint);
      nodeTree.clear();
      nodeTree.insert(median);
      for (MapNode mapNode : nodeListCpy) {
        nodeTree.insert(mapNode);
      }
    }
  }
  /**
   * Returns the database connection.
   * @return the connection to the database.
   */
  public Connection getDatabaseConn() {
    return conn;
  }

  /**
   * Returns k-d tree for nodes.
   * @return - a k-d tree.
   */
  public KDTree<MapNode> getNodeTree() {
    return nodeTree;
  }

  /**
   * Returns a list of the nodes from the database.
   * @return a list of MapNodes.
   */
  public Map<String, MapNode> getNodeMap() {
    return nodeMap;
  }

  /**
   * Method returns a specific node from the nodeMap.
   * @param id - index of nodeMap
   * @return - node from nodeMap
   */
  public MapNode getNode(String id) {
    return nodeMap.get(id);
  }


  /**
   * Method makes sure the tables of the sql file are Node and Way.
   * @return - boolean true if file is correct, false otherwise
   * @throws SQLException - exception
   */
  private boolean checkTables() throws SQLException {
    DatabaseMetaData md = conn.getMetaData();
    ResultSet rs = md.getTables(null, null, "%", null);
    while (rs.next()) {
      if (!rs.getString(3).equals("node") && !rs.getString(3).equals("way")) {
        rs.close();
        return false;
      }
    }
    return true;
  }

  /**
   * Method checks to make sure Node Table only has 3 columns.
   * @return boolean, true if table has 3 columns, false otherwise
   * @throws SQLException - exception
   */
  private boolean checkColumnsNodeTable() throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM node LIMIT 1");
    ResultSet rs = prep.executeQuery();
    ResultSetMetaData md = rs.getMetaData();
    int col = md.getColumnCount();
    prep.close();
    rs.close();
    if (col != 3) {
      return false;
    }
    return true;
  }

  /**
   * Method checks to make sure Way table has correct number of columns.
   * @return boolean, true if way has 5 columns, false otherwise
   * @throws SQLException - exception
   */
  private boolean checkColumnsWayTable() throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM way LIMIT 1");
    ResultSet rs = prep.executeQuery();
    ResultSetMetaData md = rs.getMetaData();
    int col = md.getColumnCount();
    prep.close();
    rs.close();
    if (col != 5) {
      return false;
    }
    return true;
  }
}
