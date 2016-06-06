package me.vogeldev.androidweather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Vogel on 6/3/2016.
 */
public class ForecastAdapter extends ArrayAdapter<Weather> {

    public static String[] daysOfTheWeek = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    private ArrayList<Weather> forecast;
    private static int curDayOfWeek;

    public ForecastAdapter(Context context, int resource) {
        super(context, resource);

        forecast = new ArrayList<>();
        curDayOfWeek = -1;
    }

    public void setForecast(ArrayList<Weather> forecast) {
        this.forecast = forecast;
    }

    @Override
    public int getCount() {
        return forecast.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Weather weather = forecast.get(position);
        ViewHolder viewHolder;

        // Check if this is the first time the user has seen this view
        if(convertView == null){
            // Create a new ViewHolder and set this view's tag to it
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            // Retrieve saved ViewHolder
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Get the day of the week from the view's weather information
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(weather.getTimeStamp() * 1000);
        int viewDate = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String dayText = daysOfTheWeek[viewDate];

        // As a challenge, I checked if the day was today or tomorrow and reported it
        // to the user instead of the day of the week
        cal.setTimeInMillis(System.currentTimeMillis());
        if(curDayOfWeek == -1){
            curDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        }
        if(curDayOfWeek == viewDate){
            dayText = "Today";
        }else if((curDayOfWeek + 1) % 7 == viewDate){
            dayText = "Tomorrow";
        }

        viewHolder.tvDay.setText(dayText);
        viewHolder.tvWeatherCondition.setText(weather.getWeatherCondition());
        viewHolder.tvHigh.setText(String.valueOf(weather.getHigh()));
        viewHolder.tvLow.setText(String.valueOf(weather.getLow()));

        return convertView;
    }

    // View Holder will hold information for a view in this adapter so resources can be held for later
    // when the user scrolls past the view
    private static class ViewHolder {
        public TextView tvDay, tvWeatherCondition, tvHigh, tvLow;

        public ViewHolder(View listItem){
            tvDay = (TextView)listItem.findViewById(R.id.tvDay);
            tvWeatherCondition = (TextView)listItem.findViewById(R.id.tvWeatherCondition);
            tvHigh = (TextView)listItem.findViewById(R.id.tvHigh);
            tvLow = (TextView)listItem.findViewById(R.id.tvLow);
        }
    }
}
