package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.Way;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * The Dijkstra class which can be used to find the shortest path between two nodes in a graph.
 *
 * @param <N> A generic type representing the node
 * @param <E> A generic type representing the edge
 */
public class Dijkstra<N extends DirectedNode<N, E>, E extends DirectedEdge<N>> {

  //graph represented using a priority queue
  private final PriorityQueue<N> graph;
  private final NodeComparator<N, E> nodeComparator;
  private List<E> resetEdges = new ArrayList<>();


  /**
   * Constructor for Dijkstra which initializes the graph as null.
   */
  public Dijkstra() {
    nodeComparator = new NodeComparator();
    graph = new PriorityQueue<>(nodeComparator);
  }

  /**
   * Finds the shortest path between two nodes in the graph.
   * The prevEdge and prevNode methods can be called on the endNode to find the path.
   * @param startNode starting node
   * @param endNode ending node
   * @param extension boolean value on whether to use A* method or not
   */
  public void shortestPath(N startNode, N endNode, boolean extension, List<Way> edgeList) {
    nodeComparator.setEstimator(extension);
    //all other nodes have distance infinity
    startNode.setDistance(0);
    Set<N> visited = new HashSet<>();
    graph.add(startNode);
    int counter = 0;
    //creates a priority queue which sorts based on the distance of the node
    while (!graph.isEmpty()) {
      counter++;
      //removes and returns node with min value from priority queue
      N curr = graph.poll();
      visited.add(curr);
      //if the node with the shortest distance is the end node, then we must have found the shortest
      //path to the end node
      if (curr == endNode) {
        break;
      }
      //iterates through every outgoing edge from the node
      for (E edge: curr.getOutgoingEdges(edgeList)) {
        if(!resetEdges.contains(edge)) { // if not contained then add
          resetEdges.add(edge);
        }
        // edge returned from getOutgoingEdge
        N targetNode = edge.getEnd();
        // dist = distance at current node + weight of edge
        // to targetNode plus the distance from targetNode to the endNode
        double heuristicAddition = 0;
        if (extension) {
          // take into account A* and weather heuristic
          heuristicAddition = edge.getDistToEnd(endNode);
          targetNode.setHeuristic(heuristicAddition);
        }
        double dist = curr.getDistance() + edge.getWeight() + heuristicAddition;
        // if dist is less then dist in node, reset targetNode dist and prev
          if (dist < targetNode.getDistance()) {
            targetNode.setDistance(dist);
            targetNode.setPrevNode(curr);
            targetNode.setPrevEdge(edge);
          }
        //removes and re-adds because dist may have changed so order of priority queue
        //needs to update.
        graph.remove(targetNode);
        if (!visited.contains(targetNode)) {
          graph.add(targetNode);
        }
      }
    }
    System.out.println("finished" + counter);
  }

  public void resetEdge() {
    for(E edge : resetEdges) {
      edge.getStart().setDistance(Double.POSITIVE_INFINITY);
      edge.getStart().setPrevEdge(null);
      edge.getStart().setPrevNode(null);
      edge.getEnd().setDistance(Double.POSITIVE_INFINITY);
      edge.getEnd().setPrevEdge(null);
      edge.getEnd().setPrevNode(null);
    }
  }
}
