package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Map which sorts by key and can have multiple values associated with each key.
 * @param <K> Type of the key
 * @param <V> Type of the value
 */
public class MultiTreeMap<K, V> extends TreeMap<K, List<V>> {
  private int size;

  /**
   * Constructor for MultiTreeMap. Initializes instance variables.
   */
  public MultiTreeMap() {
    size = 0;
  }

  /**
   * Adds an entry to the map.
   * @param key key associated with entry
   * @param value value associated with entry
   */
  public void add(K key, V value) {
    if (super.get(key) == null) {
      List<V> valueList = new ArrayList<>();
      valueList.add(value);
      super.put(key, valueList);
    } else {
      super.get(key).add(value);
    }
    size++;
  }

  /**
   * Gives the size of the map.
   *
   * @return number of values in the map
   */
  @Override
  public int size() {
    return size;
  }
}
