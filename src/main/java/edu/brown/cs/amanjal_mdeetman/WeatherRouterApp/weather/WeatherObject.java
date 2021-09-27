package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.weather;

public class WeatherObject {
  private int temp;
  private String windSpeed;
  private String forecast;
  private boolean isDayTime;
  private String icon;

  public WeatherObject(int temp, String windSpeed, String forecast, boolean isDayTime) {
    this.temp = temp;
    this.windSpeed = windSpeed;
    this.forecast = forecast;
    this.isDayTime = isDayTime;
  }

  public int getTemp() {
    return temp;
  }

  public String getWindSpeed() {
    return windSpeed;
  }

  public String getForecast() {
    return forecast;
  }

  public boolean isDayTime() {
    return isDayTime;
  }

  public Object[] getProps() {
    Object[] obj = new Object[4];
    obj[0] = this.getTemp();
    obj[1] = this.getWindSpeed();
    obj[2] = this.getForecast();
    obj[3] = this.isDayTime();
    return obj;
  }
}