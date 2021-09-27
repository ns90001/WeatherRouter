package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.maps;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.Way;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.RouteCmd;
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

public class RouteTest {
    private REPL repl;

    @Before
    public void setUp() {
        HashMap<String, ICallable> commands = new HashMap<>();
        MapCmd mapCmd = new MapCmd();
        commands.put("map", mapCmd);
        commands.put("route", new RouteCmd(mapCmd));
        repl = new REPL(commands);
        repl.stringRun(new ArrayList<>(Arrays.asList("map", "data/maps/smallMaps.sqlite3")));
    }
    @After
    public void tearDown() {
        repl = null;
    }

    /**
     * Test checking simple Route commands.
     */
    @Test
    public void simpleRouteTest() {
        setUp();
        String out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "41.82", "-71.4", "41.8203", "-71.4")));
        assertEquals("/n/0 -> /n/1 : /w/0", out);
        out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "41.82", "-71.4", "41.8206", "-71.4")));
        assertEquals("/n/0 -> /n/1 : /w/0\n/n/1 -> /n/2 : /w/1", out);
        out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "41.8203", "-71.4", "41.82", "-71.4003")));
        assertEquals("/n/1 -/- /n/3", out);
        tearDown();
    }

    /**
     * Test making sure error is outputted with invalid input.
     */
    @Test
    public void errorRouteTest() {
        setUp();
        String out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "0")));
        assertTrue(out.contains("ERROR:"));
        out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "0", "0", "100", "100", "50")));
        assertTrue(out.contains("ERROR:"));
        out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "\"road 5\"", "0", "0")));
        assertTrue(out.contains("ERROR:"));
        out = repl.stringRun(new ArrayList<>(Arrays.asList("nearest", "0", "\"a\"")));
        assertTrue(out.contains("ERROR:"));
        out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "\"Chihiro Ave\"","\"Yubaba St\"", "\"Sootball Ln\"", "\"Yubaba St\"")));
        assertTrue(out.contains("ERROR:"));
        out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "\"Sootball Ln\"", "\"Yubaba St\"", "\"Chihiro Ave\"", "\"Yubaba St\"")));
        assertTrue(out.contains("ERROR:"));
        tearDown();
    }

    /**
     * Test to make sure outputPath works properly in Route.
     */
    @Test
    public void outputPathTest() {
        HashMap<String, ICallable> commands = new HashMap<>();
        MapCmd mapCmd = new MapCmd();
        commands.put("map", mapCmd);
        RouteCmd route = new RouteCmd(mapCmd);
        commands.put("route", route);
        repl = new REPL(commands);
        repl.stringRun(new ArrayList<>(Arrays.asList("map", "data/maps/smallMaps.sqlite3")));
        MapNode node1 = new MapNode(1, 1, "1", mapCmd);
        MapNode node2 = new MapNode(2, 2, "2", mapCmd);
        node2.setPrevNode(node1);
        assertEquals("1 -/- 2", route.outputPath(node1, node2));
        node2.setDistance(20);
        //Way edge = new Way(node1, node2, "1", true);
        //node2.setPrevEdge(edge);
        assertEquals("1 -> 2 : 1", route.outputPath(node1, node2));
    }

    /**
     * Test to make sure caching works on route command
     */
    @Test
    public void cacheRouteTest() {
        setUp();
        String out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "41.82", "-71.4", "41.8203", "-71.4")));
        assertEquals("/n/0 -> /n/1 : /w/0", out);
        out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "41.82", "-71.4", "41.8203", "-71.4")));
        assertEquals("/n/0 -> /n/1 : /w/0", out);
        out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "41.82", "-71.4", "41.8206", "-71.4")));
        assertEquals("/n/0 -> /n/1 : /w/0\n/n/1 -> /n/2 : /w/1", out);
        out = repl.stringRun(new ArrayList<>(Arrays.asList("route", "41.82", "-71.4", "41.8206", "-71.4")));
        assertEquals("/n/0 -> /n/1 : /w/0\n/n/1 -> /n/2 : /w/1", out);
        tearDown();
    }
}
