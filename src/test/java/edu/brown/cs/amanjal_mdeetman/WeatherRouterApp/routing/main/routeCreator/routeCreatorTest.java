package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.routeCreator;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.RouteCreator;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.Way;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.REPL;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class routeCreatorTest {

  MapCmd mapCmd;
  REPL repl;
  RouteCreator routing;

  public routeCreatorTest() {
    HashMap<String, ICallable> commands = new HashMap<>();
    mapCmd = new MapCmd();
    commands.put("map", mapCmd);

    // Create RouteCreator
    routing = new RouteCreator(mapCmd);

    repl = new REPL(commands);

  }

  @Test
  public void algorithmTest() throws Exception {

    List<String> list = new ArrayList<>();
    list.add("data/maps/maps.sqlite3");
    mapCmd.run(list);
    MapNode start = new MapNode( 41.828527, -71.422115, "/n/4182.7142.201329758", mapCmd);
    MapNode end = new MapNode(41.864799, -71.392677, "/n/4186.7139.201646904", mapCmd);
    String time = "2021-04-21T19:00:00-04:00";

    List<List<Way>> output = routing.createRoutes(start, end, time,1);

    // checks if output returned a non-null value
    assertNotNull(output);

//    //checks if rerouted
//    assertEquals(2, output.size());

  }

  @Test
  public void sameRouteTest() {

    List<Way> wayList1 = new ArrayList<>();
    List<Way> wayList2 = new ArrayList<>();
    List<Way> wayList3 = new ArrayList<>();
    List<Way> wayList4 = new ArrayList<>();

    Way way1 = new Way(
        new MapNode(0, 0, "", mapCmd),
        new MapNode(0, 0, "", mapCmd),
        "1",
        0);
    Way way2 = new Way(
        new MapNode(0, 0, "", mapCmd),
        new MapNode(0, 0, "", mapCmd),
        "2",
        0);

    wayList1.add(way1);
    wayList1.add(way2);
    wayList2.add(way1);
    wayList2.add(way2);
    wayList3.add(way2);
    wayList3.add(way1);
    wayList4.add(way2);

    // normal test
    assertEquals(true, routing.sameRoute(wayList1, wayList2));

    // different order
    assertEquals(false, routing.sameRoute(wayList1, wayList3));

    // different number of ways
    assertEquals(false, routing.sameRoute(wayList3, wayList4));

  }

  @Test
  public void routeChecksTest() {

    List<Way> input = new ArrayList<>();

    Way way1 = new Way(
        new MapNode( 41.828527, -71.422115, "", mapCmd),
        new MapNode(41.864799, -71.392677, "", mapCmd),
        "way1", 10);
    Way way2 = new Way(
        new MapNode( 41.838527, -71.422115, "", mapCmd),
        new MapNode(41.844799, -71.392677, "", mapCmd),
        "way2", 10);

    input.add(way1);
    input.add(way2);

    // checks route distance
    assertEquals(5.689E-4, routing.calculateRouteDistance(input), 0.00001);

   // checks route weight
    assertEquals(5.689E-5, routing.lengthOfRoute(input), 0.00001);

    // checks weather threshold
    assertEquals(0, routing.checkWeatherThreshold(input, 5.689E-4), 0.00001);

  }

  @Test
  public void attachWeatherDataTest() throws Exception {

    List<Way> input = new ArrayList<>();

    Way way1 = new Way(
        new MapNode( 41.828527, -71.422115, "", mapCmd),
        new MapNode(41.864799, -71.392677, "", mapCmd),
        "way1", 10);
    Way way2 = new Way(
        new MapNode( 41.838527, -71.422115, "", mapCmd),
        new MapNode(41.844799, -71.392677, "", mapCmd),
        "way2", 10);

    input.add(way1);
    input.add(way2);

    String time = "2021-04-21T19:00:00-04:00";

    routing.attachWeatherData(input, time);

    // checks if weather data objects were attached to each way in the input
    assertNotNull(input.get(0).getWeatherObj());
    assertNotNull(input.get(1).getWeatherObj());

  }

}
