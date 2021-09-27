package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra;

/**
 * Interface representing a directed edge in a graph.
 * @param <N> generic type representing the node
 */
public interface DirectedEdge<N> {
  /**
   * Gets the node at the start of the edge.
   * @return the start node
   */
  N getStart();

  /**
   * Gets the node at the end of the edge.
   * @return the end node
   */
  N getEnd();

  /**
   * Gets the weight of the edge.
   * @return weight of the edge
   */
  double getWeight();

  /**
   * Gets the distance from end of node to node passed in.
   * @param node - of type N
   * @return - distance from end of edge to node
   */
  double getDistToEnd(N node);

  void setWeatherHeuristic(double num);

  double getWeatherHeuristic();

  void setWeight(double weight);
}
