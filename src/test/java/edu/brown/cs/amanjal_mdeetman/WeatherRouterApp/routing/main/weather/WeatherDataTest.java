package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.weather;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherData;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherDataStorage;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherObject;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class WeatherDataTest {
  private MapNode start;
  private MapNode end;
  private MapCmd ref;
  private WeatherObject wObj;
  private WeatherObject wObj1;
  private WeatherObject wObj2;
  private WeatherObject badObj;
  private WeatherData wRef;

  public void setUp() {
    ref = new MapCmd();
    start = new MapNode(41.8240, -71.4128, "id", ref);
    end = new MapNode(41.3776, -71.8273, "id2", ref);
    wObj = new WeatherObject(31, "35 mph", "Chance of showers", false);
    wObj1 = new WeatherObject(100, "10 mph", "Sunny", true);
    wObj2 = new WeatherObject(68, "14 mph", "Cloudy skies", false);
    badObj = new WeatherObject(0, "jslkdjf", "", true);
    wRef = new WeatherData();
  }

  @Test
  public void testCreateData() throws Exception {
    setUp();
    WeatherObject wObj = wRef.createData(start, end, "2021-04-16T14:00:00-04:00");
    WeatherObject wObj1 = new WeatherObject(53, "8 mph", "Partly Cloudy", true);
    assert (wObj1.isDayTime() == wObj.isDayTime());
    tearDown();
  }

  @Test
  public void testCreateHeuristic() throws Exception {
    setUp();
    double heuristic1 = WeatherData.createHeuristic(wObj);
    double heuristic2 = WeatherData.createHeuristic(wObj1);
    double heuristic3 = WeatherData.createHeuristic(wObj2);
    double one = 19;
    double two = 4;
    double three = 4;
    System.out.println(heuristic1);
    System.out.println(heuristic2);
    System.out.println(heuristic3);
    assertTrue(heuristic1 == one);
    assertTrue(heuristic2 == two);
    assertTrue(heuristic3 == three);
    tearDown();
  }

  @Test
  public void testGetSpeed() throws Exception {
    setUp();
    double speed1 = WeatherData.getSpeed(wObj.getWindSpeed());
    double speed2 = WeatherData.getSpeed(wObj1.getWindSpeed());
    double speed3 = WeatherData.getSpeed(wObj2.getWindSpeed());
    assert (speed1 == 35.0);
    assert (speed2 == 10.0);
    assert (speed3 == 14.0);
    tearDown();
  }

  @Test (expected = Exception.class)
  public void testBadSpeed() throws Exception {
    setUp();
    double speed = WeatherData.getSpeed(badObj.getWindSpeed());
  }

  public void tearDown() {
    ref = null;
    start = null;
    end = null;
    wObj = null;
    wObj1 = null;
    wObj2 = null;
    badObj = null;
    wRef = null;
  }

}
