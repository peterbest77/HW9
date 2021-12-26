package com.example.hw9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class WeatherTableAdaptor extends RecyclerView.Adapter<WeatherTableAdaptor.ViewHolder> {
    private ArrayList<JSONObject> eightDayData = new ArrayList<>();
    private final Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_table, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            System.out.println("!!!!!!!!!!");
            System.out.println(eightDayData);
            System.out.println("!!!!!!!!!");
            String data= eightDayData.get(position).get("startTime").toString().substring(0, 10);
            holder.data.setText(data);
            int low_temperature = (int)eightDayData.get(position).getJSONObject("values").getDouble("temperatureMin");
            holder.low_temperature.setText(String.valueOf(low_temperature));
            int high_temperature = (int)eightDayData.get(position).getJSONObject("values").getDouble("temperatureMax");
            holder.high_temperature.setText(String.valueOf(high_temperature));
            int weather_code = eightDayData.get(position).getJSONObject("values").getInt("weatherCode");
            String weather_icon = getWeatherIcon(weather_code, 1);
            int weather_icon_id = getResId(weather_icon, R.drawable.class);
            holder.weather_img.setImageResource(weather_icon_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return eightDayData.size();
    }

    public void setEightDayData(ArrayList<JSONObject> eightDayData) {
        this.eightDayData = eightDayData;
        notifyDataSetChanged();
    }

    public WeatherTableAdaptor(Context context){
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView data;
        private TextView low_temperature;
        private TextView high_temperature;
        private ImageView weather_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.weather_table_data);
            low_temperature = itemView.findViewById(R.id.weather_table_temperature_low);
            high_temperature = itemView.findViewById(R.id.weather_table_temperature_high);
            weather_img = itemView.findViewById(R.id.weather_table_img);

        }
    }
    public String getWeatherIcon(int weatherCode1, int day) {
        String src = null;
        String weatherStatus1 = null;
        if (weatherCode1 == 1000) {

            if (day == 1) {
                src = "ic_clear_day";

            }
            else {
                src = "ic_clear_night";
            }

            weatherStatus1 ="Clear";
        }
        else if (weatherCode1 == 1100) {
            if (day == 1) {
                src = "ic_mostly_clear_day";
            }
            else{
                src = "ic_mostly_clear_night";
            }
            weatherStatus1="Mostly Clear";

        }
        else if (weatherCode1 == 1101) {
            if (day == 1) {
                src = "ic_partly_cloudy_day";
            }
            else{
                src = "ic_partly_cloudy_night";
            }
            weatherStatus1="Partly Clear";
        }

        else if (weatherCode1 == 1001) {

            src = "ic_cloudy";
            weatherStatus1="Cloudy";

        }
        else if (weatherCode1 == 1102) {

            src = "ic_mostly_cloudy";
            weatherStatus1="Mostly Cloudy";

        }


        else if (weatherCode1 == 2000) {

            src = "ic_fog";
            weatherStatus1="Fog";
        }
        else if (weatherCode1 == 2100) {

            src = "ic_fog_light";
            weatherStatus1 ="Light Fog";
        }
        else if (weatherCode1 == 8000) {

            src = "ic_tstorm";
            weatherStatus1 = "ThunderStorm";

        }
        else if (weatherCode1 == 5001) {

            src = "ic_flurries";
            weatherStatus1 = "Flurries";
        }
        else if (weatherCode1 == 5100) {

            src = "ic_snow_light";
            weatherStatus1 ="Light Snow";

        }
        else if (weatherCode1 == 5000) {

            src = "ic_snow";
            weatherStatus1 ="Snow";

        }
        else if (weatherCode1 == 5101) {

            src = "ic_rain_heavy";
            weatherStatus1 ="Heavy Snow";

        }
        else if (weatherCode1 == 7102) {

            src = "ic_ice_pellets_light";
            weatherStatus1 = "Light Ice Pellets";

        }
        else if (weatherCode1 == 7000) {

            src = "ic_ice_pellets";
            weatherStatus1 = "Ice Pellets";

        }
        else if (weatherCode1 == 7101) {

            src = "ic_ice_pellets_heavy";
            weatherStatus1 = "Heavy Ice Pellets";

        }
        else if (weatherCode1 == 4000) {

            src = "ic_drizzle";
            weatherStatus1 = "Drizzle";

        }
        else if (weatherCode1 == 6000) {

            src = "ic_freezing_drizzle";
            weatherStatus1 = "Freezing Drizzle";

        }
        else if (weatherCode1 == 6200) {

            src = "ic_freezing_rain_light";
            weatherStatus1 = "Light Freezing Rain";

        }
        else if (weatherCode1 == 6001) {

            src = "ic_freezing_rain";
            weatherStatus1 = "Freezing Rain";

        }
        else if (weatherCode1 == 6201) {

            src = "ic_freezing_rain_heavy";
            weatherStatus1 = "Heavy Freezing Rain";

        }
        else if (weatherCode1 == 4200) {

            src = "ic_rain_light";
            weatherStatus1 = "Light Rain";

        }
        else if (weatherCode1 == 4001) {

            src = "ic_rain";
            weatherStatus1 = "Rain";

        }
        else if (weatherCode1 == 4201) {

            src = "ic_rain_heavy";
            weatherStatus1 = "Heavy Rain";

        }   else if (weatherCode1 == 3000) {
            src = "ic_light_wind";
            weatherStatus1 = "Light Wind";

        }
        else if (weatherCode1 == 3001) {
            src = "ic_wind";
            weatherStatus1 = "Wind";

        }
        else if (weatherCode1 == 3002) {
            src = "ic_strong_wind";
            weatherStatus1 = "Strong Wind";

        }

        return src;
    }
    public int getResId(String resName, Class<?> c) {


        try {
            Field idField = c.getDeclaredField(resName);
            int anInt = 0;
            try {
                anInt = idField.getInt(idField);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return anInt;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        }



    }
}
