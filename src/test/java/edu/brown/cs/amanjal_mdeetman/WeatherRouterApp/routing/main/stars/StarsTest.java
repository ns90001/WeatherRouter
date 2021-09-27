package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.stars;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.REPL;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.Star;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands.MockCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands.NaiveNeighborsCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands.NaiveRadiusCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands.StarsCmd;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class StarsTest {
  private REPL repl;
  private StarsCmd starsCmd;

  @Before
  public void setUp() {
    starsCmd = new StarsCmd();
    HashMap<String, ICallable> commands = new HashMap<>();
    commands.put("stars", starsCmd);
    commands.put("naive_neighbors", new NaiveNeighborsCmd(starsCmd));
    commands.put("naive_radius", new NaiveRadiusCmd(starsCmd));
    commands.put("mock", new MockCmd());
    repl = new REPL(commands);
    List<Star> starsList = new ArrayList<>();
    starsList.add(starsCmd.addStar(new String[]{"1", "Sol", "0", "0", "0"}));
    starsList.add(starsCmd.addStar(new String[]{"2", "Star1", "1", "0", "0"}));
    starsList.add(starsCmd.addStar(new String[]{"3", "Star2", "0", "1", "0"}));
    starsList.add(starsCmd.addStar(new String[]{"4", "Star3", "0", "0", "1"}));
    starsList.add(starsCmd.addStar(new String[]{"5", "Star4", "1", "2", "0"}));
    starsList.add(starsCmd.addStar(new String[]{"6", "Star7", "-1.5", "-3", "-4.5"}));
    starsList.add(starsCmd.addStar(new String[]{"7", "Star6", "3", "-2", "1"}));
    starsList.add(starsCmd.addStar(new String[]{"8", "Star5", "1", "2", "-3"}));
    starsList.add(starsCmd.addStar(new String[]{"9", "Star8", "0.5", "0.5", "0.5"}));
    starsList.add(starsCmd.addStar(new String[]{"10", "Star9", "-10", "-10", "-10"}));
    starsCmd.setStars(starsList);
  }

  @Before
  public void setUpEmpty() {
    starsCmd = new StarsCmd();
    HashMap<String, ICallable> commands = new HashMap<>();
    commands.put("stars", starsCmd);
    commands.put("naive_neighbors", new NaiveNeighborsCmd(starsCmd));
    commands.put("naive_radius", new NaiveRadiusCmd(starsCmd));
    commands.put("mock", new MockCmd());
    repl = new REPL(commands);
  }

  @After
  public void tearDown() {
    repl = null;
    starsCmd = null;
  }

  @Test
  public void testNumStars() {
    setUp();
    assertEquals(starsCmd.getNumStars(), 10);
    tearDown();
  }

  @Test
  public void testNaiveNeighborsName() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "4", "\"Sol\"")));
    assertEquals("9\n2\n3\n4", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "1", "\"Star9\"")));
    assertEquals(out, "6");
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "9", "\"Sol\"")));
    assertEquals(out, "9\n2\n3\n4\n5\n7\n8\n6\n10");
    tearDown();
  }

  @Test
  public void testNaiveNeighborsCoords() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "5", "0", "0", "0")));
    assertEquals("1\n9\n2\n3\n4", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "2", "-10", "-10", "-10")));
    assertEquals(out, "10\n6");
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "2", "0", "2", "0")));
    assertEquals(out, "3\n5");
    tearDown();
  }

  @Test
  public void testNaiveNeighborsTie() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "3", "\"Sol\"")));
    assertEquals(out.length(), 5);
    for (String farStar : new String[]{"1", "5", "6", "7", "8"}) {
      assertFalse(out.contains(farStar));
    }
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "1", "0", "2", "0")));
    assertEquals(out.length(), 1);
    assertTrue(out.contains("3") || out.contains("5"));
    tearDown();
  }

  @Test
  public void testNaiveNeighborsEdge() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "0", "\"Sol\"")));
    assertEquals(out.length(), 0);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "10", "0", "0", "0")));
    String outCompare = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "100", "0", "0", "0")));
    assertEquals(out, outCompare);
    tearDown();
  }

  @Test
  public void testNaiveNeighborsError() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "1", "1", "1", "1", "1")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "-1", "0", "0", "0")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "5", "Sol")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_neighbors", "5", "\"Fake Star\"")));
    assertTrue(out.contains("ERROR:"));
    tearDown();
  }

  @Test
  public void testNaiveRadiusName() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "1", "\"Sol\"")));
    assertEquals("9\n2\n3\n4", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "12.5", "\"Star9\"")));
    assertEquals("6", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "500", "\"Sol\"")));
    assertEquals("9\n2\n3\n4\n5\n7\n8\n6\n10", out);
    tearDown();
  }

  @Test
  public void testNaiveRadiusCoords() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "1", "0", "0", "0")));
    assertEquals("1\n9\n2\n3\n4", out);
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "1", "0", "2", "0")));
    assertEquals("3\n5", out);
    tearDown();
  }

  @Test
  public void testNaiveRadiusEdge() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "4", "10", "10", "10")));
    assertEquals(0, out.length());
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "0", "\"Sol\"")));
    assertEquals(0, out.length());
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "0", "-1.5", "-3", "-4.5")));
    assertEquals("6", out);
    tearDown();
  }

  @Test
  public void testNaiveRadiusError() {
    setUp();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "1", "1", "1", "1", "1")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "-1", "0", "0", "0")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "5", "Sol")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("naive_radius", "5", "\"Fake Star\"")));
    assertTrue(out.contains("ERROR:"));
    tearDown();
  }

  @Test
  public void starsTest() {
    setUpEmpty();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/stars/ten-star.csv")));
    assertEquals("Read 10 stars from data/stars/ten-star.csv", out);
    assertEquals(10,starsCmd.getNumStars());
    out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/stars/three-star.csv")));
    assertEquals("Read 3 stars from data/stars/three-star.csv", out);
    List<Star> stars = starsCmd.getStars();
    assertEquals(3,stars.size());
    assertEquals("ID: 1,Name: Star One", stars.get(0).toString());
    assertEquals("ID: 2,Name: Star Two", stars.get(1).toString());
    assertEquals("ID: 3,Name: Star Three", stars.get(2).toString());
    tearDown();
  }

  @Test
  public void starsEdgeTest() {
    setUpEmpty();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/stars_test/no-star.csv")));
    assertEquals("Read 0 stars from data/stars_test/no-star.csv", out);
    assertEquals(0,starsCmd.getNumStars());
    out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/stars_test/empty-entries.csv")));
    assertEquals("Read 3 stars from data/stars_test/empty-entries.csv", out);
    assertEquals(3,starsCmd.getNumStars());
    tearDown();
  }

  @Test
  public void starsErrorTest() {
    setUpEmpty();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/README.md")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/MOCK_DATA.csv")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/fake-file.csv")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/stars_test/invalid-id.csv")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/stars_test/invalid-coords.csv")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/stars_test/invalid-format.csv")));
    assertTrue(out.contains("ERROR:"));
    out = repl.stringRun(new ArrayList<>(Arrays.asList("stars", "data/stars_test/empty-file.csv")));
    assertTrue(out.contains("ERROR:"));
    tearDown();
  }

  @Test
  public void mockTest() {
    setUpEmpty();
    String out = repl.stringRun(new ArrayList<>(Arrays.asList("mock", "data/mock_test/mock-sample.csv")));
    String expectedOut =
      "Name: Othelia Wonfar,Email: owonfar0@hibu.com,Gender: Genderqueer,Address: 63 Service Junction\n"
      + "Name: Bethany,Date: 9/30/2020,Email: bhogsden1@cargocollective.com,Gender: Male\n"
      + "Name: Rhys Donnersberg,Email: rdonnersberg2@smh.com.au,Address: 98963 New Castle Center";
    assertEquals(expectedOut, out);
    tearDown();
  }
}
