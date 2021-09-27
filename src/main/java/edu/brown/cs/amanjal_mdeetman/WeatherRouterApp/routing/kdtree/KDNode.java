package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree;

/**
 * KDNode class which is what will make up the k-d tree.
 * @param <T> The type of KDPoint which will be contained in the node
 */
public class KDNode<T extends KDPoint> {
  private final int dimension;
  private int keyDimension;
  private double keyCoordinate;
  private KDNode<T> left;
  private KDNode<T> right;
  private final T point;

  /**
   * Constructor for KDNode which initializes instance variables.
   * @param newPoint The 'value' contained in the node
   * @param depth Depth of this node in the tree.
   */
  public KDNode(T newPoint, int depth) {
    this.point = newPoint;
    this.dimension = point.getCoords().length;
    setKeyDimension(depth % dimension);
    this.left = null;
    this.right = null;
  }

  /**
   * Sets the left child of this node.
   * @param leftNode KDNode which will be it's left child
   * @return returns true if successful and false otherwise
   */
  public boolean setLeft(KDNode<T> leftNode) {
    if (leftNode.getDimension() == dimension) {
      this.left = leftNode;
      return true;
    }
    return false;
  }

  /**
   * Sets the right child of this node.
   * @param rightNode KDNode which will be it's right child
   * @return returns true if successful and false otherwise
   */
  public boolean setRight(KDNode<T> rightNode) {
    if (rightNode.getDimension() == dimension) {
      this.right = rightNode;
      return true;
    }
    return false;
  }

  /**
   * Gets the left child.
   * @return KDnode which is the left child
   */
  public KDNode<T> getLeft() {
    return this.left;
  }

  /**
   * Gets the right child.
   * @return KDNode which is the right child
   */
  public KDNode<T> getRight() {
    return this.right;
  }

  /**
   * Gets the value associated with this node.
   * @return the value of type T
   */
  public T getPoint() {
    return this.point;
  }

  /**
   * Sets the relevant dimension and coordinate for this point.
   * @param newKeyDim key dimension for this node.
   */
  public void setKeyDimension(int newKeyDim) {
    this.keyDimension = newKeyDim;
    setKeyCoordinate(point.getCoords()[newKeyDim]);
  }

  /**
   * returns the relevant dimension for this point.
   * @return integer representing the index of the relevant coordinate.
   */
  public int getKeyDimension() {
    return this.keyDimension;
  }

  /**
   * Sets the relevant coordinate for this point.
   * @param keyCoord relevant coordinate.
   */
  private void setKeyCoordinate(double keyCoord) {
    this.keyCoordinate = keyCoord;
  }

  /**
   * Gets the relevant coordinate for this point.
   * @return relevant coordinate
   */
  public double getKeyCoordinate() {
    return this.keyCoordinate;
  }

  /**
   * Gets the dimension of the node, equivalent to the number of coordinates which
   * represents its location.
   * @return dimension
   */
  public int getDimension() {
    return this.dimension;
  }
}
