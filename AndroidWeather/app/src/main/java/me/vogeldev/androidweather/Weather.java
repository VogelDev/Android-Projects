package me.vogeldev.androidweather;

/**
 * Created by Vogel on 6/3/2016.
 *
 * Simple Java object for holding weather information
 */
public class Weather {
    private double high, low;
    private String weatherCondition;
    private long timeStamp;

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeathCondition(String weathCondition) {
        this.weatherCondition = weathCondition;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
