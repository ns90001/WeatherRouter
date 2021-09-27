package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.Way;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.RouteCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherData;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherObject;

import java.util.ArrayList;
import java.util.List;

public class RouteCreator {

  private MapCmd mapCmd;
  private List<Way> existingWays;
  private WeatherData wRef;
  public RouteCreator(MapCmd mapCmd) {
    this.mapCmd = mapCmd;
    this.existingWays = new ArrayList<>();
    wRef = new WeatherData();
  }


  public List<List<Way>> createRoutes(MapNode start, MapNode end, String startTime, int reRoutes)
      throws Exception {
    // find initial route (should be a list of ways)
    RouteCmd thisRouteCmd = new RouteCmd(mapCmd);
    // create tokens to pass into RouteCmd
    List<String> tokens = new ArrayList<>();
    String add1 = "" + start.getLatitude();
    String add2 = "" + start.getLongitude();
    String add3 = "" + end.getLatitude();
    String add4 = "" + end.getLongitude();
    tokens.add(add1);
    tokens.add(add2);
    tokens.add(add3);
    tokens.add(add4);
    thisRouteCmd.run(tokens);
    List<Way> firstRoute = thisRouteCmd.getFirstRouteList();
    for (Way way : firstRoute) {
      existingWays.add(way);
    }
    // Attach wayWeatherData to each way
    attachWeatherData(firstRoute, startTime);
    //  Use method to check whether weather threshold is too high
    Double routeDist = calculateRouteDistance(firstRoute);
    // Check whether the weather is bad enough a reroute is warranted
    boolean badWeather = (checkWeatherThreshold(firstRoute, routeDist) > 0); // edit this eventually
    if (firstRoute.isEmpty() || !badWeather) {
      List<List<Way>> retList = new ArrayList<>();
      retList.add(firstRoute);
      existingWays = new ArrayList<>();
      this.resetEdgeWeights(firstRoute);
      return retList;
    } else {
      // Do route calculation again with weather heuristics added
      List<Way> secondRoute = firstRoute;
      Double originalRouteDist = this.lengthOfRoute(firstRoute); // length of original route
      Double bestRoute = originalRouteDist;
      for(int i = 0; i < reRoutes; i++) { // loop for number of reRoutes to do
        RouteCmd newRouteCmd = new RouteCmd(mapCmd);
        newRouteCmd.setWays(existingWays);
        newRouteCmd.setFirstRouteList(new ArrayList<>());
        newRouteCmd.run(tokens); // run routing with original ways having weatherHeuristic added
        List<Way> newRoute = newRouteCmd.getFirstRouteList();
        attachWeatherData(newRoute, startTime); // attach weather data to new route
        Double length = this.lengthOfRoute(newRoute);
        if (length < bestRoute) { // check if new route is better then the current best route
          bestRoute = length;
          secondRoute = newRoute; // if so set the weather_adjusted route to new route
        }
        for (Way w : newRoute) { // add new ways from newroute to existing ways
          if (!existingWays.contains(w)) {
            existingWays.add(w);
          }
        }
      }
      // If the route did change, offer both routes
      if (!sameRoute(firstRoute, secondRoute) ) {
        List<List<Way>> retList = new ArrayList<>();
        retList.add(firstRoute);
        retList.add(secondRoute);
        existingWays = new ArrayList<>();
        this.resetEdgeWeights(firstRoute);
        this.resetEdgeWeights(secondRoute);
        return retList;

        // If the route didn't change, offer only the first route
      } else {
        List<List<Way>> retList = new ArrayList<>();
        retList.add(firstRoute);
        existingWays = new ArrayList<>();
        this.resetEdgeWeights(firstRoute);
        return retList;
      }
    }

  }

  public boolean sameRoute(List<Way> r1, List<Way> r2) {
    if (r1.size() != r2.size()) {
      return false;
    }
    for (int i=0; i<r1.size(); i++) {
      if (!(r1.equals(r2))) {
        return false;
      }
    }
    return true;
  }

  public void attachWeatherData(List<Way> ways, String time) throws Exception {
    for (Way way : ways) {
      WeatherObject newData = wRef.createData(way.getStart(), way.getEnd(), time);
      way.setWeatherData(newData);
      way.setWeatherHeuristic(WeatherData.createHeuristic(newData));
      Double dist = way.getHaversineDistance(way.getStart(), way.getEnd());
      way.setWeight(dist + dist*way.getWeatherHeuristic());
    }
  }

  public double calculateRouteDistance(List<Way> route) {
    double totalDist = 0;
    for (Way way : route) {
      totalDist = totalDist + way.getHaversineDistance(way.getStart(), way.getEnd());
    }
    return totalDist;
  }

  public double checkWeatherThreshold(List<Way> route, double routeDist) {
    double returnThreshold = 0;
    for (Way way : route) {
      returnThreshold = returnThreshold +
      (way.getWeatherHeuristic()) * way.getHaversineDistance(way.getStart(), way.getEnd()) / routeDist;
    }
    return returnThreshold;
  }

  public double lengthOfRoute(List<Way> route) {
    double total = 0;
    for(Way way: route) {
      total += way.getWeight();
    }
    return total;
  }

  public void resetEdgeWeights(List<Way> ways){
    for(Way w: ways) {
      w.setWeight(w.getHaversineDistance(w.getStart(), w.getEnd()));
      w.setWeatherHeuristic(0);
    }
  }

}
