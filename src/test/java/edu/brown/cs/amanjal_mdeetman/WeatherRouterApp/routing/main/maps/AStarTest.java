package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.main.maps;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.Way;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.RouteCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.dijkstra.Dijkstra;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class AStarTest {
  private Dijkstra<MapNode, Way> dijkstra;
  private MapCmd mapCmd;
  private RouteCmd routeCmd;
  private MapNode node1;
  private MapNode node2;
  private MapNode node3;
  private MapNode node4;
  private MapNode node5;
//
//  @Before
//  public void setUp() {
//    dijkstra = new Dijkstra<>();
//    mapCmd = new MapCmd();
//    routeCmd = new RouteCmd(mapCmd);
//    node1 = new MapNode(100, 100, "/n/1", mapCmd);
//    node2 = new MapNode(98, 99.5, "/n/2", mapCmd);
//    node3 = new MapNode(100.5, 100, "/n/3", mapCmd);
//    node4 = new MapNode(102, 100, "/n/4", mapCmd);
//    node5 = new MapNode( 98, 101, "/n/5", mapCmd);
//    Set<Way> ways1 = new HashSet<>();
//    ways1.add(new Way(node1, node2, "/w/1", true));
//    ways1.add(new Way(node1, node3, "/w/2", true));
//    ways1.add(new Way(node1, node4, "/w/3", false));
//    Set<Way> ways2 = new HashSet<>();
//    ways2.add(new Way(node2, node3, "/w/4", true));
//    ways2.add(new Way(node2, node4, "/w/5", true));
//    ways2.add(new Way(node2, node5, "/w/6", true));
//    Set<Way> ways3 = new HashSet<>();
//    ways3.add(new Way(node3, node2, "/w/7", true));
//    ways3.add(new Way(node3, node4, "/w/8", true));
//    ways3.add(new Way(node3, node5, "/w/9", true));
//    Set<Way> ways4 = new HashSet<>();
//    ways4.add(new Way(node4, node5, "/w/10", true));
//    ways4.add(new Way(node4, node1, "/w/11", false));
//    node1.setOutgoingEdges(ways1);
//    node2.setOutgoingEdges(ways2);
//    node3.setOutgoingEdges(ways3);
//    node4.setOutgoingEdges(ways4);
//    node5.setOutgoingEdges(new HashSet<>());
//  }
//
//  @After
//  public void tearDown() {
//    dijkstra = null;
//    mapCmd = null;
//    routeCmd = null;
//    node1 = null;
//    node2 = null;
//    node3 = null;
//    node4 = null;
//    node5 = null;
//  }
//
//  @Test
//  public void testShortestPath() {
//    setUp();
//    dijkstra.shortestPath(node1, node5, false);
//    String outDijkstra = routeCmd.outputPath(node1, node5);
//    dijkstra.shortestPath(node1, node5, true);
//    String outAStar = routeCmd.outputPath(node1, node5);
//    assertEquals(outDijkstra, outAStar);
//    String expectedOut = "/n/1 -> /n/2 : /w/1\n"
//      + "/n/2 -> /n/5 : /w/6";
//    assertEquals(expectedOut, outAStar);
//    dijkstra.shortestPath(node1, node4, false);
//    outDijkstra = routeCmd.outputPath(node1, node4);
//    dijkstra.shortestPath(node1, node4, true);
//    outAStar = routeCmd.outputPath(node1, node4);
//    assertEquals(outDijkstra, outAStar);
//    expectedOut = "/n/1 -> /n/3 : /w/2\n"
//      + "/n/3 -> /n/4 : /w/8";
//    assertEquals(expectedOut, outAStar);
//    tearDown();
//  }
//
//  @Test
//  public void testNoPath() {
//    setUp();
//    dijkstra.shortestPath(node4, node2, false);
//    String outDijkstra = routeCmd.outputPath(node4, node2);
//    dijkstra.shortestPath(node4, node2, true);
//    String outAStar = routeCmd.outputPath(node4, node2);
//    assertEquals(outDijkstra, outAStar);
//    String expectedOut = "/n/4 -/- /n/2";
//    assertEquals(expectedOut, outAStar);
//    dijkstra.shortestPath(node4, node1, false);
//    outDijkstra = routeCmd.outputPath(node4, node1);
//    dijkstra.shortestPath(node4, node1, true);
//    outAStar = routeCmd.outputPath(node4, node1);
//    assertEquals(outDijkstra, outAStar);
//    expectedOut = "/n/4 -/- /n/1";
//    assertEquals(expectedOut, outAStar);
//    tearDown();
//  }
}
