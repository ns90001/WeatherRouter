package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.Cache;

import java.util.HashMap;
import java.util.Map;
/**
 * Generic class for the Cache, used to help reduce time in all Map commands.
 * @param <K> - key
 * @param <V> - value
 */
public class Cache<K, V> {
  private Map<K, V> cacheMap;
  private int currSize;
  private static final int MAXPAIRS = 50;
  private static final int MAXVALUESIZE = 10000;
  private int sizeOfVal;

  /**
   * Cache Constructor.
   */
  public Cache() {
  }

  /**
   * Method builds the Cache as a new HashMap.
   */
  public void build() {
    cacheMap = new HashMap<>();
  }

  /**
   * Method adds a key value pair to the cacheMap. First checks if the
   * key is already contained, and then if not checks to make sure
   * size of object passed in is not too big, and if not adds to the
   * cache and then calls removeOld to remove items if cache has too many.
   * @param key - key for HashMap
   * @param value - value for HashMap
   */
  public void add(K key, V value) {
    if (!cacheMap.containsKey(key)) {
      if (sizeOfVal < MAXVALUESIZE) {
        cacheMap.put(key, value);
        currSize++;
        this.removeOld(); // remove old
      }
    }
  }


  /**
   * Method cleans the cacheMap out.
   */
  public void clean() {
    if (cacheMap.size() > 0) {
      cacheMap.clear();
    }
  }
  /**
   *  Method removes the oldest pairs in the cache as it starts to fill up.
   */
  private void removeOld() {
    if (currSize > MAXPAIRS) { // need to remove old objects
      Object[] set = cacheMap.keySet().toArray();
      Object k = set[0];
      cacheMap.remove(k);
      currSize--;
    }
  }
  /**
   * Returns the value of a specific key.
   * @param key - key for HashMap.
   * @return - value for HashMap.
   */
  public V returnValue(K key) {
    return cacheMap.get(key);
  }
  /**
   * Method checks to see if HashMap has a key.
   * @param key - key for HashMap
   * @return - a boolean on if key is present.
   */
  public boolean hasKey(K key) {
    return cacheMap.containsKey(key);
  }

  /**
   * Method sets the size of the value about to be passed into HashMap.
   * @param size - int memory size
   */
  public void setSizeOfVal(int size) {
    sizeOfVal = size;
  }

}
