package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.weather;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherData;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherDataStorage;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherObject;
import org.junit.Test;

public class WeatherDataStorageTest {
  private MapNode start;
  private MapNode end;
  private MapCmd ref;
  private WeatherData wRef;

  public void setUp() {
    ref = new MapCmd();
    start = new MapNode(41.8240, -71.4128, "id", ref);
    end = new MapNode(41.3776, -71.8273, "id2", ref);
    wRef = new WeatherData();
  }

  @Test
  public void testCaching() throws Exception {
    setUp();
    wRef.createData(start, end, "2021-04-16T14:00:00-04:00");
    wRef.createData(start, end, "2021-04-16T18:00:00-04:00");
    wRef.createData(start, end, "2021-04-16T22:00:00-04:00");
    tearDown();
  }

  public void tearDown() {
    ref = null;
    start = null;
    end = null;
    wRef = null;
  }

}