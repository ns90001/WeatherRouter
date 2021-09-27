package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.maps;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.NearestCmd;
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

public class NearestTest {
  private REPL repl;

  @Before
  public void setUp() {
    HashMap<String, ICallable> commands = new HashMap<>();
    MapCmd mapCmd = new MapCmd();
    commands.put("map", mapCmd);
    commands.put("nearest", new NearestCmd(mapCmd));
    repl = new REPL(commands);
    repl.stringRun(new ArrayList<>(Arrays.asList("map", "data/maps/smallMaps.sqlite3")));
  }

  @After
  public void tearDown() {
    repl = null;
  }

  @Test
  public void simpleNearestTest() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "41.82", "-71.4")));
    assertEquals("/n/0", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "42", "-72")));
    assertEquals("/n/5", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "40", "-70")));
    assertEquals("/n/0", out);
    tearDown();
  }

  @Test
  public void edgeNearestTest() {
    setUp();
    //tests coordinates far from any point
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "0", "0")));
    assertEquals("/n/0", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "300", "-300")));
    assertEquals("/n/5", out);
    //tests precision
    out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "41.8203", "-71.4003")));
    assertEquals("/n/4", out);
    tearDown();
  }

  @Test
  public void errorNearestTest() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "0")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "0", "0", "0")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "0", "a")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "", "")));
    assertTrue(out.contains("ERROR:"));
    tearDown();
  }

  @Test
  public void bigMapNearestTest() {
    setUp();
    repl.stringRun(new ArrayList<>(Arrays.asList("map", "data/maps/maps.sqlite3")));
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "40", "-75")));
    assertEquals("/n/4015.7374.527767659", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "41.7112793", "-71.4804453")));
    assertEquals("/n/4171.7148.1544938909", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "41.8167", "-71.4303")));
    assertEquals("/n/4181.7143.201319725", out);
    tearDown();
  }
}
