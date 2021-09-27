package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.maps;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.WaysCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.REPL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WaysTest {
  private REPL repl;
  @Before
  public void setUp() {
    HashMap<String, ICallable> commands = new HashMap<>();
    MapCmd mapCmd = new MapCmd();
    commands.put("map", mapCmd);
    commands.put("ways", new WaysCmd(mapCmd));
    repl = new REPL(commands);
    repl.stringRun(new ArrayList<>(Arrays.asList("map", "data/maps/smallMaps.sqlite3")));
  }
  @After
  public void tearDown() {
    repl = null;
  }

  /**
   * Test to make sure Ways prints an error when invalid input is passed in.
   */
  @Test
  public void waysErrorTest() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "0")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "0", "0", "100", "100", "50")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "0", "0", "100", "WORD")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "1", "1", "0", "0")));
    assertTrue(out.contains("ERROR:"));
    tearDown();
  }

  /**
   * Specifically test when ways is called and database is not set
   */
  @Test
  public void waysErrorNoDatabase() {
    HashMap<String, ICallable> commands = new HashMap<>();
    MapCmd mapCmd = new MapCmd();
    commands.put("map", mapCmd);
    commands.put("ways", new WaysCmd(mapCmd));
    repl = new REPL(commands);
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "0", "0", "100", "100")));
    assertTrue(out.contains("ERROR:"));
  }

  /**
   * Simple tests checking ways works properly.
   */
  @Test
  public void simpleWaysTest() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "41.82", "-71.4", "41.8203", "-71.4")));
    assertEquals("/w/0\n/w/1\n/w/2\n/w/3", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "41.82", "-71.4", "41.82", "-71.4")));
    assertEquals("/w/0\n/w/2", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "0", "-0", "100", "-100")));
    assertEquals("/w/0\n/w/1\n/w/2\n/w/3\n/w/4\n/w/5\n/w/6", out);
    tearDown();
  }

  /**
   * Tests to make sure the caching part of Ways does not break the program.
   */
  @Test
  public void waysCacheTest() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "41.82", "-71.4", "41.8203", "-71.4")));
    assertEquals("/w/0\n/w/1\n/w/2\n/w/3", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "41.82", "-71.4", "41.8203", "-71.4")));
    assertEquals("/w/0\n/w/1\n/w/2\n/w/3", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "0", "-0", "100", "-100")));
    assertEquals("/w/0\n/w/1\n/w/2\n/w/3\n/w/4\n/w/5\n/w/6", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("ways", "0", "-0", "100", "-100")));
    assertEquals("/w/0\n/w/1\n/w/2\n/w/3\n/w/4\n/w/5\n/w/6", out);
    tearDown();
  }
}
