package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.maps.MapComponents.MapNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class WeatherData {
  private WeatherDataStorage ref;
  private HashMap<JSONArray, WeatherObject> cache;
  public WeatherData() {
   ref = new WeatherDataStorage();
   cache = new HashMap<JSONArray, WeatherObject>();
  }

  public WeatherObject createData(MapNode start, MapNode end, String time) throws Exception {
    //given nodes from a way - the weatherObj will be set as attribute to way
    //need to find a better way of doing this
    //DecimalFormat df = new DecimalFormat("##.####");
    double lat = ((start.getLatitude() + end.getLatitude())/2);
    double lon = ((start.getLongitude() + end.getLongitude())/2);
    //lat = Double.parseDouble(df.format(lat));
    //lon = Double.parseDouble(df.format(lon));
    //time format thus far should be : "2021-04-06T16:00:00-04:00"
    //giving a data followed by the hour as "T(hour of day of military time)-(normal time)
    String wTime = time;
    WeatherObject wObj;
    JSONArray wData = null;
    try {
      wData = ref.hourlyForecastRequest(lat, lon);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (cache.containsKey(wData)) {
      return cache.get(wData);
    }
    int count = wData.length();
    for(int i=0 ; i< count; i++){
      JSONObject hourlyData = wData.getJSONObject(i);

      ZonedDateTime formattedStartTime = ZonedDateTime.parse(hourlyData.getString("startTime"),
          DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("UTC-04:00")));
      ZonedDateTime formattedTime = ZonedDateTime.parse(wTime,
          DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("UTC-04:00")));

      if (
          (formattedTime.getHour() == formattedStartTime.getHour())
      && formattedTime.getDayOfYear() == formattedStartTime.getDayOfYear()) {
        int temp;
        String windSpeed;
        String shortForecast;
        boolean isDayTime;
        String icon;
        temp = Integer.parseInt(hourlyData.getString("temperature"));
        windSpeed = hourlyData.getString("windSpeed");
        icon = hourlyData.getString("icon");
        shortForecast = hourlyData.getString("shortForecast");
        if (hourlyData.getString("isDaytime").equals("true")) {
          isDayTime = true;
        } else {
          isDayTime = false;
        }
        wObj = new WeatherObject(temp, windSpeed, shortForecast, isDayTime);
        cache.put(wData, wObj);
        return wObj;
      }
    }
    throw new Exception("wObj not found or created");
  }

  public static double createHeuristic(WeatherObject wObj) throws Exception {
    WeatherData ref = new WeatherData();
    int windSpeed = ref.getSpeed(wObj.getWindSpeed());
    double heuristic = 0;
    String[] forecast = wObj.getForecast().split(" ");
    //create a weather heuristic based on the given weatherObj
    //attributes have specified values
    //ie: if it is night then 5 points are added to the heuristic
    if (wObj.isDayTime() == false) {
      heuristic += Constants.nightInc;
    }
    if (windSpeed < 15 && windSpeed != 0) {
      heuristic += Constants.lowWindSpeedInc;
    } else if (windSpeed >= 15 && windSpeed < 30) {
      heuristic += Constants.medWindSpeedInc;
    } else if (windSpeed >= 30 && windSpeed < 60) {
      heuristic += Constants.highWindSpeedInc;
    } else if (windSpeed >= 60) {
      heuristic += Constants.dangerousWindSpeedInc;
    } else {
      throw new Exception("windSpeed could not be categorized properly");
    }
    if (wObj.getTemp() <= 32 || wObj.getTemp() >= 100) {
      heuristic += Constants.tempInc;
    }
    //need a better system
    for (String s: forecast) {
      if (s.equalsIgnoreCase("showers") || s.equalsIgnoreCase("rain")) {
        heuristic += Constants.showersInc;
      }
      if (s.equalsIgnoreCase("snow")) {
        heuristic += Constants.snowInc;
      }
      if (s.equalsIgnoreCase("hail")) {
        heuristic += Constants.hailInc;
      }
      if (s.equalsIgnoreCase("thunderstorms")) {
        heuristic += Constants.thunderstormsInc;
      }
    }
    return heuristic;
  }

  public static int getSpeed(String windSpeed) throws Exception {
    String[] string = windSpeed.split(" ");// \s regex not working
    if (string.length <= 1) {
      throw new Exception("");
    }
    int speed = Integer.parseInt(string[0]);
    return speed;
  }

}