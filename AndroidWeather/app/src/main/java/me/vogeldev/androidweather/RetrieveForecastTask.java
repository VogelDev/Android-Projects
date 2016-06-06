package me.vogeldev.androidweather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Vogel on 6/3/2016.
 */
public class RetrieveForecastTask extends AsyncTask<Double, Void, ArrayList<Weather>> {

    private ForecastAdapter adapter;

    public RetrieveForecastTask(ForecastAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<Weather> doInBackground(Double... params) {
        ArrayList<Weather> list = new ArrayList<>();

        // URL for OpenWeatherMap and parameters
        try {
            // actual URL for this API is:
            // http://api.openweathermap.org/data/2.5/forecast/daily?lat={lat}&lon={lon}&units=imperial&cnt=7&appid=816635d14c3b74af191bd9bd5494f1b6
            // where lat is latitude and lon is longitude
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat="
                    + params[0]
                    + "&lon="
                    + params[1]
                    + "&units=imperial&cnt=7&appid=816635d14c3b74af191bd9bd5494f1b6");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set timeouts so AsynkTask won't run forever
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            // This is specific to the OWM API
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            // Get the HTTP response code:
            // 200 = OK
            // 404 = Not Found
            // 500 = Generic Error
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream is = conn.getInputStream();
                String json = convertStreamToString(is);
                is.close();
                list.addAll(parseJSON(json));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        super.onPostExecute(weathers);

        // AsyncTask is done, update adapter with weather information
        adapter.setForecast(weathers);
        adapter.notifyDataSetChanged();
    }

    // Helper method to parse the JSON that OpenWeatherMap returns
    // Written by mohitd, the Udemy video creator
    private ArrayList<Weather> parseJSON(String json) throws JSONException {
        ArrayList<Weather> forecast = new ArrayList<>();
        JSONArray jsonArray = new JSONObject(json).getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            Weather weather = new Weather();
            JSONObject jsonDay = jsonArray.getJSONObject(i);
            weather.setTimeStamp(jsonDay.getInt("dt"));
            weather.setHigh(jsonDay.getJSONObject("temp").getDouble("max"));
            weather.setLow(jsonDay.getJSONObject("temp").getDouble("min"));
            JSONObject jsonWeather = jsonDay.getJSONArray("weather").getJSONObject(0);
            weather.setWeathCondition(jsonWeather.getString("main"));
            forecast.add(weather);
        }
        return forecast;
    }

    // Helper method to convert the output from OpenWeatherMap to a String
    // Written by mohitd, the Udemy video creator
    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
}
