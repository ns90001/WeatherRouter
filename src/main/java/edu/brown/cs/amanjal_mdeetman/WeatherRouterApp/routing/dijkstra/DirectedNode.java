package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.Way;

import java.util.List;
import java.util.Set;

/**
 * Abstract class representing a node in a graph with the
 * information necessary to perform algorithms like Dijsktra's.
 * @param <N> generic type representing the node
 * @param <E> generic type representing the edge
 */
public abstract class DirectedNode<N, E> {
  private N prevNode = null;
  private E prevEdge = null;
  private double distance = Double.POSITIVE_INFINITY;
  private double heuristic = 0;
  private double weatherHeuristic = 0;
  private String nodeId;

  /**
   * Sets the distance.
   * @param distance double representing distance
   */
  public void setDistance(double distance) {
    this.distance = distance;
  }

  /**
   * Gets the distance.
   * @return double representing distance
   */
  public double getDistance() {
    return this.distance;
  }

  /**
   * Set the heuristic distance.
   * @param dist - double representing heuristic distance
   */
  public void setHeuristic(double dist) {
    this.heuristic = dist;
  }

  /**
   * Set the heuristic distance.
   * @return - double representing heuristic distance
   */
  public double getHeuristic() {
    return this.heuristic;
  }
  /**
   * Sets the previous node in the path.
   * @param node the previous node
   */
  public void setPrevNode(N node) {
    this.prevNode = node;
  }

  /**
   * Gets the previous node in the path.
   * @return the previous node
   */
  public N getPrevNode() {
    return this.prevNode;
  }

  /**
   * Sets the previous edge in the path.
   * @param edge the previous edge
   */
  public void setPrevEdge(E edge) {
    this.prevEdge = edge;
  }

  /**
   * Gets the previous edge in the path.
   * @return the previous edge
   */
  public E getPrevEdge() {
    return this.prevEdge;
  }

  /**
   * Gets a set of all the outgoing edges from this node in the graph.
   * @return set of outgoing edges
   * @param edgeList
   */
  public abstract Set<E> getOutgoingEdges(List<Way> edgeList);

  public String getNodeId() {
    return nodeId;
  }

  public abstract void setWeatherHeuristic(double weatherHeuristic);
}
