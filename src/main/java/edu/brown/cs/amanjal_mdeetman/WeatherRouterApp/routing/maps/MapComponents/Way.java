package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra.DirectedEdge;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherObject;

/**
 * Class representing a way.
 */
public class Way implements DirectedEdge<MapNode> {

  private final MapNode start;
  private final MapNode end;
  private final String id;
  private double weight;
  private double distToEnd = -1;
  private double weather = 0;
  private double speed;
  private WeatherObject weatherObj;

  /**
   * Constructor for Way which initializes instance variables.
   * @param startNode start node of way
   * @param endNode end node of way
   * @param wayId id of way (including /w/)
   * @param traversable indicates if the way is traversable
   */
  public Way(MapNode startNode, MapNode endNode, String wayId, double traversable) {
    start = startNode;
    end = endNode;
    id = wayId;
    if (traversable != 0) {
      this.speed = traversable;
      //haversine formula not calculated immediately as it may slow down program
      weight = -1;
    } else {
      //represents a non-traversable way
      weight = Double.POSITIVE_INFINITY;
    }
  }

  /**
   * Gets the node at the start of the way.
   * @return start node
   */
  @Override
  public MapNode getStart() {
    return this.start;
  }

  /**
   * Gets the node at the end of the way.
   * @return end node.
   */
  @Override
  public MapNode getEnd() {
    return this.end;
  }

  /**
   * Gets weight of the edge. In this case, it is the distance between the start and end node.
   * @return Haversine distance between the start and end node
   */
  @Override
  public double getWeight() {
    //weight initialized to be -1 so this is only calculated when necessary
    if (weight < 0) {
      weight = getHaversineDistance(this.start, this.end)/this.speed;
    }
    return weight;
  }

  /**
   * toString method for Way.
   * @return the id of the way
   */
  @Override
  public String toString() {
    return this.id;
  }

  /**
   * Calculates the Haversine distance between the end node of the way to the node passed in.
   * @param node - of type N
   * @return Haversine distance from end node of edge to node passed in
   */
  @Override
  public double getDistToEnd(MapNode node) {
    if (distToEnd < 0) {
      distToEnd = getHaversineDistance(this.getEnd(), node);
    }
    return distToEnd;
  }

  /**
   * Gets the haversine distance between two MapNode objects.
   * @param node1 a MapNode with latitude and longitude.
   * @param node2 a MapNode with latitude and longitude.
   * @return haversine distance between the two nodes.
   */
  public double getHaversineDistance(MapNode node1, MapNode node2) {
    double lat1 = Math.toRadians(node1.getLatitude());
    double long1 = Math.toRadians(node1.getLongitude());
    double lat2 = Math.toRadians(node2.getLatitude());
    double long2 = Math.toRadians(node2.getLongitude());
    //Haversine distance formula
    double latDiff = (lat2 - lat1) / 2;
    double longDiff = (long2 - long1) / 2;
    double a = Math.pow(Math.sin(latDiff), 2);
    double b = a + (Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(longDiff), 2));
    //no need to multiply by 2r because it is a constant
    return Math.asin(Math.sqrt(b));
  }

  @Override
  public void setWeatherHeuristic(double num) {
    this.weather = num;
  }

  @Override
  public double getWeatherHeuristic() {
    return this.weather;
  }

  @Override
  public void setWeight(double weightNew) {
    weight = weightNew;
  }

  /**
   * Set the weather data of the way
   * @param newData - weatherData object
   */
  public void setWeatherData(WeatherObject newData) {
    weatherObj = newData;
  }

  /**
   * Getters for weatehr object
   * @return - weather object
   */
  public WeatherObject getWeatherObj() {
    return weatherObj;
  }

  /**
   * Returns id of the way
   * @return - string
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the speed of the way
   * @return -- double
   */
  public double getSpeed() {
    return this.speed;
  }
}
