package com.example.hw9;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;










/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private JSONObject data;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String temperature;
    private String windSpeed;
    private String pressure;
    private String precipitation;
    private String weatherStatue;
    private String humidity;
    private String visibility;
    private String cloudCover;
    private String ozone;
    private TextView windSpeedTextView;
    private TextView pressureTextView;
    private TextView precipitationTextView;
    private TextView temperatureTextView;
    private TextView weatherStatusTextView;
    private ImageView weatherImageView;
    private TextView humidityTextView;
    private TextView visibilityTextView;
    private TextView cloudCoverTextView;
    private TextView ozoneTextView;
    private int weatherCode;
    private String weatherIconName;
    private int weatherIconId;
    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONObject test = getDataFromDetail();
        System.out.println(test);

        JSONObject currentDayData = null;
        try {
            currentDayData = test.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values");

            temperature = (int)currentDayData.getDouble("temperature") + "°F";
            windSpeed = currentDayData.getDouble("windSpeed") + "mph";
            pressure = currentDayData.getDouble("pressureSeaLevel") + " inHg";
            precipitation = currentDayData.getInt("precipitationProbability") + ".00%";
            humidity = currentDayData.getInt("humidity") + "%";
            visibility = currentDayData.getDouble("visibility") + " mi";
            cloudCover = (int)currentDayData.getDouble("cloudCover") + "%";
            ozone = currentDayData.getInt("uvIndex") + ".00";
            weatherCode = currentDayData.getInt("weatherCode");
            weatherStatue = getWeatherStatue(weatherCode, 1);
            weatherIconName = getWeatherIcon(weatherCode, 1);
            weatherIconId = getResId(weatherIconName, R.drawable.class);
            System.out.println("fragment1" + windSpeed);
            System.out.println("fragment2" + temperature);


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View root =  inflater.inflate(R.layout.fragment_first, container, false);

//        windSpeedTextView = (TextView).findViewById(R.id.detail_today_wind_speed);
//
//        windSpeedTextView.setText(windSpeed);
        return root;
    }
    private JSONObject getDataFromDetail() {
        data = ((Detail)getActivity()).getData();
        return data;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        windSpeedTextView = getView().findViewById(R.id.detail_today_wind_speed);
        pressureTextView = getView().findViewById(R.id.detail_today_pressure);
        precipitationTextView = getView().findViewById(R.id.detail_today_precipitation);
        temperatureTextView = getView().findViewById(R.id.detail_today_temperature);
        weatherImageView = getView().findViewById(R.id.detail_today_weather_icon);
        weatherStatusTextView = getView().findViewById(R.id.detail_today_weather_code);
        humidityTextView = getView().findViewById(R.id.detail_today_humidity);
        visibilityTextView = getView().findViewById(R.id.detail_today_visibility);
        cloudCoverTextView = getView().findViewById(R.id.detail_today_cloud_cover);
        ozoneTextView = getView().findViewById(R.id.detail_today_ozone);
        ozoneTextView.setText(ozone);
        cloudCoverTextView.setText(cloudCover);
        visibilityTextView.setText(visibility);
        humidityTextView.setText(humidity);
        weatherStatusTextView.setText(weatherStatue);
        weatherImageView.setImageResource(weatherIconId);
        temperatureTextView.setText(temperature);
        precipitationTextView.setText(precipitation);
        pressureTextView.setText(pressure);
        windSpeedTextView.setText(windSpeed);
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
    public String getWeatherStatue(int weatherCode1, int day) {
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

        return weatherStatus1;
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