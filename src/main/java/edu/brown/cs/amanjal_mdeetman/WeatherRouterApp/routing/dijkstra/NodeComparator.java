package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra;

import java.util.Comparator;

/**
 * Comparator used to compare two DirectedNodes.
 * @param <N> - Node
 * @param <E> - Edge of Node
 */
public class NodeComparator<N, E> implements Comparator<DirectedNode<N, E>> {
  private boolean estimator;

  /**
   * Empty constructor for GraphNodeComparator.
   */
  public NodeComparator() { }

  /**
   * Compares nodes by distance.
   * @param node1 first node which you want to compare.
   * @param node2 second node which you want to compare.
   * @return returns 1 if dist of first is bigger than second, 0 if they are equal,
   * and -1 otherwise.
   */
  @Override
  public int compare(DirectedNode node1, DirectedNode node2) {
    int output = Double.compare(node1.getDistance(), node2.getDistance());
    if (output == 0 && estimator) {
      output = Double.compare(node1.getHeuristic(), node2.getHeuristic());
    }
    return output;
  }

  /**
   * Method to see if we should check the estimator (A* portion of comparing).
   * @param b - a boolean
   */
  public void setEstimator(boolean b) {
    estimator = b;
  }
}
