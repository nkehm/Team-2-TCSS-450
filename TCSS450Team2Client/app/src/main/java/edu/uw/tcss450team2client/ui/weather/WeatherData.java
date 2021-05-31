package edu.uw.tcss450team2client.ui.weather;

import java.io.Serializable;


public class WeatherData implements Serializable {

    /**
     *  String of daily, current, or hour.
     */
    private String type;

    /**
     * Incrementing counter. Increments past 0; defaults to 0.
     */ 
    private int increment;
    /**
     * Double of minimum temperature. 
     */
    private double tempMin;
    /**
     * Double of maximum temperature.
     */
    private double tempMax;
    /**
     * Double of current temperature.
     */
    private double temp;
    /**
     * String of weather status. 
     */
    private String weather;

    /**
     * Constructor that instantiates fields. 
     * @param theType
     * @param theIncrement
     * @param tempMin
     * @param tempMax
     * @param temp
     * @param theWeather
     */
    public WeatherData(String theType, int theIncrement, double tempMin, double tempMax, double temp, String theWeather) {
        this.type = theType;
        this.increment = theIncrement;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.temp = temp;
        this.weather = theWeather;
    }

    /**
     * Daily weather constructor.
     * @param theIncrement
     * @param temp
     * @param theWeather
     */
    public WeatherData(String theType, int theIncrement, double temp, String theWeather) {
        this(theType, theIncrement, -1, -1, temp, theWeather);
    }

    /**
     * Hourly weather constructor. 
     * @param theIncrement
     * @param tempMin
     * @param tempMax
     * @param theWeather
     */
    public WeatherData(int theIncrement, double tempMin, double tempMax, String theWeather) {
        this("daily", theIncrement, tempMin, tempMax, -1, theWeather);

    }
    /**
     * Returns type.
     * @return String of type. 
     */
    public String getType() {
        return type;
    }
    /**
     * Returns increment counter. 
     */
    public int getIncrement() {
        return increment;
    }

    /**
     * Returns minimum temperature. 
     */
    public double getTempMin() {
        return kelvinToFahrenheit(tempMin);
    }

    /**
     * Returns maximum temperature. 
     */
    public double getTempMax() {
        return kelvinToFahrenheit(tempMax);
    }

    /**
     * Returns current temperature. 
     * @return double of temperature. 
     */
    public double getTemp() {
        return kelvinToFahrenheit(temp);
    }

    /**
     * Returns weather status. 
     * @return String of weather. 
     */
    public String getWeather() {
        return weather;
    }

    public double kelvinToFahrenheit(double kTemp){

        return ((kTemp - 273) * 9/5 + 32);
    }

}
