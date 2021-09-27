package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.maps;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.REPL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapTest {
  private REPL repl;
  private MapCmd mapCmd;

  @Before
  public void setUp() {
    HashMap<String, ICallable> commands = new HashMap<>();
    mapCmd = new MapCmd();
    commands.put("map", mapCmd);
    repl = new REPL(commands);
  }

  @After
  public void tearDown() {
    repl = null;
    mapCmd = null;
  }

  @Test
  public void smallDbTest() {
    setUp();
    String dbPath = "data/maps/smallMaps.sqlite3";
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("map", dbPath)));
    assertEquals("map set to " + dbPath, out);
    assertNotNull(mapCmd.getDatabaseConn());
    assertEquals(6, mapCmd.getNodeMap().size());
    assertEquals(6, mapCmd.getNodeTree().size());
    assertNotNull(mapCmd.getNode("/n/5"));
    tearDown();
  }

  @Test
  public void largeDbTest() {
    setUp();
    String dbPath = "data/maps/maps.sqlite3";
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("map", dbPath)));
    assertEquals("map set to " + dbPath, out);
    assertNotNull(mapCmd.getDatabaseConn());
    assertTrue(639498 >= mapCmd.getNodeMap().size());
    assertTrue(639498 >= mapCmd.getNodeTree().size());
    assertNotNull(mapCmd.getNode("/n/4173.7138.1559636388"));
    tearDown();
  }

  @Test
  public void errorTest() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("map", "fakeDB.sqlite3")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("map", "data/stars/stardata.csv")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("map")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("map", "data/maps/smallMaps.sqlite3", "another argument")));
    assertTrue(out.contains("ERROR:"));
  }
}
