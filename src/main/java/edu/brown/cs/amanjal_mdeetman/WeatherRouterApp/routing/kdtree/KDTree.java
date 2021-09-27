package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  KDTree class which implements a k-d tree and includes methods to find
 *  poins close to each other.
 * @param <T> the type of KDPoint which will be added to the tree.
 */
public class KDTree<T extends KDPoint> {

  private final int dimension;
  private KDNode<T> root;
  private int numNodes;

  /**
   * Constructor for KDTree which initializes instance variables.
   * @param dim The dimension of the tree
   */
  public KDTree(int dim) {
    dimension = dim;
    root = null;
    numNodes = 0;
  }

  /**
   * Adds a point to the tree.
   * @param newPoint the point of type T which should be added.
   */
  public void insert(T newPoint) {
    double[] newCoords;
    if (newPoint == null) {
      throw new IllegalArgumentException("Error: cannot add null to KD tree");
    }
    newCoords = newPoint.getCoords();
    if (newCoords.length != dimension) {
      throw new IllegalArgumentException(
        "Error: cannot add point of the wrong dimension to KD tree");
    }
    numNodes++;
    if (root == null) {
      root = new KDNode<>(newPoint, 0);
      return;
    }
    int depth = 1;
    KDNode<T> curr = root;
    KDNode<T> next;
    while (true) {
      if (newCoords[curr.getKeyDimension()] < curr.getKeyCoordinate()) {
        next = curr.getLeft();
        if (next == null) {
          curr.setLeft(new KDNode<>(newPoint, depth));
          return;
        }
      } else {
        next = curr.getRight();
        if (next == null) {
          curr.setRight(new KDNode<>(newPoint, depth));
          return;
        }
      }
      curr = next;
      depth++;
    }
  }

  /**
   * Finds the k closest values from the target.
   * @param k radius around the target where we want to find nodes
   * @param target k-d array containing the coordinates of the target.
   * @param excludeID the id of the entry which you do not want to be returned
   * @return Sorted map containing the k closest elements.
   */
  public MultiTreeMap<Double, T> kNearestNeighbors(int k, double[] target, int excludeID) {
    MultiTreeMap<Double, T> neighbors = new MultiTreeMap<>();
    neighborsRecurse(k, root, target, neighbors, excludeID);
    return neighbors;
  }

  /**
   * Finds the k closest values from the target.
   * @param k radius around the target where we want to find nodes
   * @param target k-d array containing the coordinates of the target.
   * @return Sorted map containing the k closest elements.
   */
  public MultiTreeMap<Double, T> kNearestNeighbors(int k, double[] target) {
    MultiTreeMap<Double, T> neighbors = new MultiTreeMap<>();
    neighborsRecurse(k, root, target, neighbors, -1);
    return neighbors;
  }

  /**
   * Recursive method to determine the points closest to a target.
   * @param k number of neighbors which want to be found
   * @param curr current node which is being examined
   * @param target target point which will act as the origin
   * @param neighbors data structures which will contain the k closest neighbors
   * @param excludeID the id of any value which you don't want to be included map of near points.
   *                  -1 should be passed in to indicate that no points should be excluded.
   */
  private void neighborsRecurse(int k, KDNode<T> curr, double[] target,
                               MultiTreeMap<Double, T> neighbors, int excludeID) {
    double dist = curr.getPoint().getDist(target);
    if (curr.getPoint().getId() != excludeID) {
      if (neighbors.size() < k) {
        neighbors.add(dist, curr.getPoint());
      } else if (neighbors.size() != 0) {
        if (dist < neighbors.lastKey()) {
          List<T> lastList = neighbors.lastEntry().getValue();
          if (lastList.size() == 1) {
            neighbors.remove(neighbors.lastKey());
          } else {
            Collections.shuffle(lastList);
            lastList.remove(0);
          }
          neighbors.add(dist, curr.getPoint());
        } else if (dist == neighbors.lastKey()) {
          neighbors.add(dist, curr.getPoint());
          List<T> pointList = neighbors.lastEntry().getValue();
          Collections.shuffle(pointList);
          pointList.remove(0);
        }
      }
    }
    double currKeyCoordinate = curr.getKeyCoordinate();
    double targetKeyCoordinate = target[curr.getKeyDimension()];
    double singleDimDist = Math.abs(currKeyCoordinate - targetKeyCoordinate);
    if (neighbors.size() != 0) {
      if (Math.sqrt(neighbors.lastKey()) > singleDimDist || neighbors.size() < k) {
        if (curr.getRight() != null) {
          neighborsRecurse(k, curr.getRight(), target, neighbors, excludeID);
        }
        if (curr.getLeft() != null) {
          neighborsRecurse(k, curr.getLeft(), target, neighbors, excludeID);
        }
        return;
      }
    }
    if (currKeyCoordinate < targetKeyCoordinate) {
      if (curr.getRight() != null) {
        neighborsRecurse(k, curr.getRight(), target, neighbors, excludeID);
      }
    } else if (currKeyCoordinate > targetKeyCoordinate) {
      if (curr.getLeft() != null) {
        neighborsRecurse(k, curr.getLeft(), target, neighbors, excludeID);
      }
    }
  }

  /**
   * Finds all the nodes within a certain radius of the target.
   * @param r radius around the target where we want to find nodes
   * @param target k-d array containing the coordinates of the target.
   * @param excludeID the id of the entry which you do not want to be returned
   * @return Sorted map containing all the elements within the radius.
   */
  public MultiTreeMap<Double, KDPoint> radiusSearch(double r, double[] target, int excludeID) {
    MultiTreeMap<Double, KDPoint> nearPoints = new MultiTreeMap<>();
    radiusRecurse(r, root, target, nearPoints, excludeID);
    return nearPoints;
  }

  /**
   * Finds all the nodes within a certain radius of the target.
   * @param r radius around the target where we want to find nodes
   * @param target k-d array containing the coordinates of the target.
   * @return Sorted map containing all the elements within the radius.
   */
  public MultiTreeMap<Double, KDPoint> radiusSearch(double r, double[] target) {
    MultiTreeMap<Double, KDPoint> nearPoints = new MultiTreeMap<>();
    radiusRecurse(r, root, target, nearPoints, -1);
    return nearPoints;
  }

  /**
   * Recursive method to determine the points within a given radius of a target.
   * @param r radius around the target where nodes must be found
   * @param curr current node which is being examined
   * @param target target point which will act as the origin
   * @param nearPoints data structures which will contain all the values
   *                   within r radius of the target
   * @param excludeID the id of any value which you don't want to be included map of near points.
   *                  -1 should be passed in to indicate that no points should be excluded.
   */
  public void radiusRecurse(double r, KDNode<T> curr, double[] target,
                            MultiTreeMap<Double, KDPoint> nearPoints, int excludeID) {
    double dist = Math.sqrt(curr.getPoint().getDist(target));
    if (curr.getPoint().getId() != excludeID && dist <= r) {
      nearPoints.add(dist, curr.getPoint());
    }
    double currKeyCoordinate = curr.getKeyCoordinate();
    double targetKeyCoordinate = target[curr.getKeyDimension()];
    double singleDimDist = Math.abs(currKeyCoordinate - targetKeyCoordinate);
    if (r > singleDimDist) {
      if (curr.getRight() != null) {
        radiusRecurse(r, curr.getRight(), target, nearPoints, excludeID);
      }
      if (curr.getLeft() != null) {
        radiusRecurse(r, curr.getLeft(), target, nearPoints, excludeID);
      }
      return;
    }
    if (currKeyCoordinate < targetKeyCoordinate) {
      if (curr.getRight() != null) {
        radiusRecurse(r, curr.getRight(), target, nearPoints, excludeID);
      }
    } else if (currKeyCoordinate > targetKeyCoordinate) {
      if (curr.getLeft() != null) {
        radiusRecurse(r, curr.getLeft(), target, nearPoints, excludeID);
      }
    }
  }

  /**
   * Clears the tree.
   */
  public void clear() {
    this.root = null;
    this.numNodes = 0;
  }

  /**
   * Returns the number of nodes in the k-d tree.
   * @return number of nodes in the tree
   */
  public int size() {
    return this.numNodes;
  }

  /**
   * toString method for the k-d tree.
   * @return returns a string containing all the nodes in the form x : y [coords]
   * where x is the depth of the node and y is its position along the tree at that depth,
   * assuming the tree is complete.
   */
  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    buildString(root, 0, 1, out);
    return out.substring(0, out.length() - 1);
  }

  /**
   * Recursive helper method to build the string.
   * @param curr current node being printed
   * @param depth depth of the node
   * @param nodeNum number of the node along the depth assuming tree is complete
   * @param out StringBuilder where the information about the node is being appended
   */
  private void buildString(KDNode<T> curr, int depth, int nodeNum, StringBuilder out) {
    out.append(depth).append(" : ").append(nodeNum)
      .append(Arrays.toString(curr.getPoint().getCoords())).append('\n');
    if (curr.getLeft() != null) {
      buildString(curr.getLeft(), depth + 1, (nodeNum * 2) - 1, out);
    }
    if (curr.getRight() != null) {
      buildString(curr.getRight(), depth + 1, nodeNum * 2, out);
    }
  }
}
