package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.Way;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.MapCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.NearestCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.RouteCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.commands.WaysCmd;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.REPL;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather.WeatherObject;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {
  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  private static final int TRAFFIC_INTERVAL = 1000;

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;
  private static REPL repl;
  private static MapCmd mapCmd;


  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
    .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    HashMap<String, ICallable> commands = new HashMap<>();

    mapCmd = new MapCmd();
    commands.put("map", mapCmd);
    commands.put("ways", new WaysCmd(mapCmd));
    commands.put("nearest", new NearestCmd(mapCmd));
    commands.put("route", new RouteCmd(mapCmd));

    // Create RouteCreator
    RouteCreator routing = new RouteCreator(mapCmd);

    repl = new REPL(commands);
    repl.run();
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("frontend/");

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.post("/run", new AlgorithmHandler());

  }
  private static class AlgorithmHandler implements Route {

    public List<JSONObject> formatOutput(List<Way> wayList) throws JSONException {
      List<JSONObject> route = new ArrayList<>();
//      if (addToStart) {
//        route.add(start);
//      }
      double wayEndLat1 = wayList.get(0).getStart().getLatitude();
      double wayEndLon1 = wayList.get(0).getStart().getLongitude();
      JSONObject wayJson1 = new JSONObject();
      JSONObject weatherJson1 = new JSONObject();
      wayJson1.put("weather", weatherJson1);
      wayJson1.put("coords", new double[] {wayEndLat1, wayEndLon1});
      route.add(wayJson1);

      for (Way way : wayList) {
        //System.out.println(way.getStart().getNodeId() +  " " + way.getEnd().getNodeId());
        //System.out.println(way.getId() + " " + way.getWeight());
        double wayEndLat = way.getEnd().getLatitude();
        double wayEndLon = way.getEnd().getLongitude();

        JSONObject wayJson = new JSONObject();

        JSONObject weatherJson = new JSONObject();

        WeatherObject weatherObj = way.getWeatherObj();
        weatherJson.put("temp", weatherObj.getTemp());
        weatherJson.put("forecast", weatherObj.getForecast());
        weatherJson.put("wind", weatherObj.getWindSpeed());

        wayJson.put("weather", weatherJson);
        wayJson.put("coords", new double[] {wayEndLat, wayEndLon});

        route.add(wayJson);
      }

      System.out.println("breal");
      return route;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());

      System.out.println("1");

      double sLat = data.getDouble("startLat");
      double sLon = data.getDouble("startLon");
      double dLat = data.getDouble("destLat");
      double dLon = data.getDouble("destLon");

      // time should be formatted in ISO-8601 format

      //String time = "2021-04-19T06:00:00-04:00";
      ZonedDateTime time = ZonedDateTime.parse(data.getString("time"), DateTimeFormatter.ISO_OFFSET_DATE_TIME);


      JSONArray stops = data.getJSONArray("stops");
      int reRoutes = data.getInt("reRoutes")-1;

      ArrayList<JSONObject> stopsList = new ArrayList<>();

      System.out.println(stops);

      if (stops != null) {
        for (int i=0; i < stops.length(); i++){
          System.out.println(stops.getJSONObject(i));
          stopsList.add(stops.getJSONObject(i));
        }
      }

      RouteCreator routeCreator = new RouteCreator(mapCmd);

      // find nearest node to start in database
      NearestCmd nearest1 = new NearestCmd(mapCmd);
      List<String> input1 = new ArrayList<>();
      String startLat = "" + sLat;
      String startLon = "" + sLon;
      input1.add(startLat);
      input1.add(startLon);
      String outputString = nearest1.run(input1);

      // find nearest node to end in database
      NearestCmd nearest2 = new NearestCmd(mapCmd);
      List<String> input2 = new ArrayList<>();
      String destLat = "" + dLat;
      String destLon = "" + dLon;
      input2.add(destLat);
      input2.add(destLon);
      nearest2.run(input2);

      MapNode startNode = nearest1.getNearNode();
      MapNode endNode = nearest2.getNearNode();

      System.out.println("5.1");

      MapNode previousNode = startNode;
      ZonedDateTime currentTime = time;

      List<List<JSONObject>> formattedRoute = new ArrayList<>();
      formattedRoute.add(new ArrayList<>());

      System.out.println("5.2");

      JSONObject startWay = new JSONObject();
      startWay.put("weather", new JSONObject());
      System.out.println(startNode);
      System.out.println(startNode.getLatitude());
      System.out.println(startNode.getLongitude());
      System.out.println(endNode.getLatitude());
      System.out.println(endNode.getLongitude());
      startWay.put("coords", new double[] {
          startNode.getLatitude(),
          startNode.getLongitude(),
      });

      System.out.println("6");
      System.out.println(stopsList.size());

      for (int i = 0; i < stopsList.size(); i++) {

        JSONObject stop = stopsList.get(i);

        System.out.println("looping");

        double stopLat = stop.getDouble("lat");
        double stopLon = stop.getDouble("lng");
        double stopDuration = stop.getDouble("time");

        System.out.println("loop1");

        currentTime = currentTime.plusHours((long) stopDuration);

        //

        NearestCmd nearest3 = new NearestCmd(mapCmd);
        List<String> input3 = new ArrayList<>();
        String stopLatString = "" + stopLat;
        String stopLonString = "" + stopLon;
        input3.add(stopLatString);
        input3.add(stopLonString);
        nearest3.run(input3);

        System.out.println(stopLatString);
        System.out.println(stopLonString);
        System.out.println("loop2");

        MapNode currentNode = nearest3.getNearNode();

        System.out.println("loop2.5");

        System.out.println(previousNode.getNodeId());
        System.out.println(currentNode.getNodeId());
        System.out.println(currentTime.toString());


        List<List<Way>> rawOutput = routeCreator.createRoutes(
            previousNode,
            currentNode,
            currentTime.toString(),
            reRoutes
        );

        System.out.println("loop3");

        if (rawOutput.size() > 1 && formattedRoute.size() < 2) {
          formattedRoute.add(new ArrayList<>());
        }

//        System.out.println(rawOutput.size());
//        System.out.println(formattedRoute.size());

        if (i == 0) {
          for (int j = 0; j < rawOutput.size(); j++) {
            System.out.println(formattedRoute.get(j).size());
            formattedRoute.get(j).addAll(formatOutput(rawOutput.get(j)));
            System.out.println(formattedRoute.get(j).size());
          }
        } else {
          for (int j = 0; j < rawOutput.size(); j++) {
            System.out.println(formattedRoute.get(j).size());
            formattedRoute.get(j).addAll(formatOutput(rawOutput.get(j)));
            System.out.println(formattedRoute.get(j).size());
          }
        }

        System.out.println("loop4");

        previousNode = currentNode;

      }

      System.out.println("7");

      System.out.println(previousNode.getNodeId());
      System.out.println(endNode.getNodeId());
      // adds the route from the last stop to the destination
      List<List<Way>> rawOutput = routeCreator.createRoutes(
          previousNode,
          endNode,
          currentTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
          reRoutes
      );

      System.out.println("output size is " + rawOutput.size());
      System.out.println(formattedRoute.size());
      //List<Way> wayList = rawOutput.get(rawOutput.size() - 1);

      if (rawOutput.size() > 1 && formattedRoute.size() < 2) {
        formattedRoute.add(new ArrayList<>());
      }

      if (stopsList.size() == 0) {
        for (int j = 0; j < rawOutput.size(); j++) {
          formattedRoute.get(j).addAll(formatOutput(rawOutput.get(j)));
        }
      } else {
        for (int j = 0; j < rawOutput.size(); j++) {
          formattedRoute.get(j).addAll(formatOutput(rawOutput.get(j)));

        }
      }

      System.out.println("9");

      JSONObject output = new JSONObject();
      //System.out.println(formattedRoute.size());
      if (formattedRoute.size() == 2) {
//        System.out.println(formattedRoute.get(0));
        output.put("normal_route", formattedRoute.get(0));
        output.put("weather_route", formattedRoute.get(1));
      } else if (formattedRoute.size() == 1) {
        output.put("normal_route", formattedRoute.get(0));
      }

      //System.out.println("10");

      // maybe add this feature later?
      //output.put("travelTime", 0);

      System.out.println("OUTPUT: " + output);

      Map<String, Object> variables = ImmutableMap.of("route", output);
      return GSON.toJson(variables);

    }
  }
  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}

