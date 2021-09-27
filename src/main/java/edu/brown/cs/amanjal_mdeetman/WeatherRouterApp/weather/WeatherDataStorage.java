package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class WeatherDataStorage  {
  private JSONArray wData;
  private HashMap<String, JSONArray> cache;
  private String url4BadData;
//  private static LoadingCache<String, JSONArray> cache;

  public WeatherDataStorage() {
    cache = new HashMap<>();
    wData = new JSONArray();
    url4BadData = new String();

//    cache = CacheBuilder.newBuilder()
//        .maximumSize(1000)
//        .expireAfterWrite(10, TimeUnit.MINUTES)
//        .build(
//            new CacheLoader<>() {
//              @Override
//              public JSONArray load(String url) throws Exception {
//                return null;
//              }
//            });
  }


  /**
   * Code for post request if we were intrested in using the NWS api
   */

  public JSONArray hourlyForecastRequest(Double lat, Double lon) throws IOException, JSONException {
    String urlString = gridPointsRequest(lat, lon);
    //System.out.println(cache);
    if (cache.containsKey(urlString)) {
      return cache.get(urlString);
    }
    URL urlForGetRequest = new URL(urlString);
    String readLine = null;
    HttpURLConnection conn = (HttpURLConnection) urlForGetRequest.openConnection();
    conn.setRequestMethod("GET");
    int responseCode = conn.getResponseCode();


    if (responseCode == HttpURLConnection.HTTP_OK) {
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuffer response = new StringBuffer();
      while ((readLine = in.readLine()) != null) {
        response.append(readLine);
      }
      in.close();

      try {
        JSONObject rootObject = new JSONObject(response.toString());
        JSONObject props = rootObject.getJSONObject("properties");
        JSONArray rows = props.getJSONArray("periods");
        wData = rows;
      } catch (JSONException e) {
        e.printStackTrace();
      }

    } else {
      System.out.println("GET NOT WORKED");
    }
    cache.put(urlString, wData);
    url4BadData = urlString;
    return wData;
  }

  public String getUrl4BadData() {
    return url4BadData;
  }


  private static String gridPointsRequest(Double lat, Double lon) throws IOException {
    String urlToBeReturned = null;
    URL urlForGetRequest = new URL("https://api.weather.gov/points/" + lat + "," + lon);
    String readLine = null;
    HttpURLConnection conn = (HttpURLConnection) urlForGetRequest.openConnection();
    conn.setRequestMethod("GET");
    int responseCode = conn.getResponseCode();


    if (responseCode == HttpURLConnection.HTTP_OK) {
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuffer response = new StringBuffer();
      while ((readLine = in.readLine()) != null) {
        response.append(readLine);
      }
      in.close();

      try {
        JSONObject rootObject = new JSONObject(response.toString());
        JSONObject props = rootObject.getJSONObject("properties");
        String url = props.getString("forecastHourly");
        urlToBeReturned = url;
      } catch (JSONException e) {
        e.printStackTrace();
      }

    } else {
      System.out.println("GET NOT WORKED");
    }
    return urlToBeReturned;
  }
  //implement caching

}