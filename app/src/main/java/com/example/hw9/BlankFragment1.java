package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class BlankFragment1 extends Fragment {


    private View root;
    private TextView textView;
    private Button button;
    private Context context;
    CardView card1;
    private SharedPreferences sharedPreferences;
    private TextView temperature_view;
    private TextView status_view;
    private TextView city_state_view;

    private TextView hum;
    private TextView windSpeed;
    private TextView visibility;
    private TextView pressure;
    private static String val_hum;
    private static String val_windSpeed;
    private static String val_visibility;
    private static String val_pressure;

    private static int weatherCode;
    private static String temperature;
    private static String weatherStatus;
    private static String cityAndState;
    private ImageView imageView;
    private static int resID;
    private List<JSONObject> eightDayData;
    private RecyclerView weatherTable;
    private IFragmentCallback fragmentCallback;
    private JSONObject allData;
    private JSONObject data;
    private JSONObject toActivityData;
    private JSONObject test;
    private String cityId;
    private String test2;
    private String floatButton;
    public BlankFragment1() {

    }
    public void setFragmentCallback(IFragmentCallback callback) {
        fragmentCallback = callback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eightDayData = new ArrayList<>();

//
//        JSONObject jsonObject = getDataFromDetail();
//        System.out.println("get data from activity" + jsonObject);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        System.out.println("+++++++++++++++++++++++++++++++++++++++");
        System.out.println(bundle);
        if (bundle != null) {
            String jsonString =  bundle.getString("data");
            floatButton = bundle.getString("floatButton");
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                allData = jsonObject;
                setJsonData(allData);
                JSONArray fifteenDaysData = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals");
                System.out.println(fifteenDaysData);
                cityAndState = bundle.getString("ip");
                System.out.println("222222222" + cityAndState);
                System.out.println("222222222" + cityAndState);
                JSONObject currentDayData = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values");
                System.out.println(currentDayData);
                weatherCode = currentDayData.getInt("weatherCode");
                temperature = (int)currentDayData.getDouble("temperature") + "°F";
                System.out.println(weatherCode);
                System.out.println(temperature);
                System.out.println(cityAndState);

                String resName = getWeatherIcon(weatherCode, 1);
                System.out.println(resName);

                resID = getResId(resName, R.drawable.class);
                val_hum = currentDayData.getInt("humidity") + "%";
                System.out.println(val_hum);
                val_windSpeed = currentDayData.getDouble("windSpeed") + "mph";
                System.out.println(val_windSpeed);
                val_visibility = currentDayData.getDouble("visibility") + "mi";
                System.out.println(val_visibility);
                val_pressure = currentDayData.getDouble("pressureSeaLevel") + "inHg";
                System.out.println(val_pressure);


                for (int i = 0; i < 8; ++i) {
                    eightDayData.add(fifteenDaysData.getJSONObject(i));

                }








            } catch (JSONException e) {
                e.printStackTrace();
            }


        }




        System.out.println("+++++++++++++++++++++++++++++++++++++++");





        if (root == null) {
            root = inflater.inflate(R.layout.fragment_blank1, container, false);

            card1 = (CardView)root.findViewById(R.id.cardshow);
            card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentCallback == null) {
                        System.out.println("is Empty");

                    }

//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(), Detail.class);
//                    startActivity(intent);
                                    getData();
                }
            });
        }







        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        FloatingActionButton fab = getView().findViewById(R.id.fab);
        FloatingActionButton fab2 = getView().findViewById(R.id.fab2);

//        if (floatButton != null && floatButton.equals("disappear")) {
            fab.setVisibility(View.INVISIBLE);
            fab2.setVisibility(View.INVISIBLE);

            if (floatButton != null && floatButton.equals("disappear")) {
                fab.setVisibility(View.VISIBLE);

            }
//        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), cityAndState + " was added to favorites", Toast.LENGTH_SHORT).show();
                fab.setVisibility(View.INVISIBLE);
                fab2.setVisibility(View.VISIBLE);
                sharedPreferences =getActivity().getSharedPreferences("mySharePreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String[]cityAndState1 = cityAndState.split(",");
                editor.putString(cityAndState1[0], cityAndState1[1]);
                System.out.println(cityAndState1[0]);
                System.out.println(cityAndState1[1]);
                editor.commit();


            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), cityAndState + " was removed from favorites", Toast.LENGTH_SHORT).show();
                fab2.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
                sharedPreferences =getActivity().getSharedPreferences("mySharePreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String[]cityAndState1 = cityAndState.split(",");

                editor.remove(cityAndState1[0]);
                editor.commit();
            }
        });





        temperature_view = (TextView)getView().findViewById(R.id.card_1_temperature);
        temperature_view.setText(temperature);
        imageView = getView().findViewById(R.id.card_1_weather_icon);
        status_view = getView().findViewById(R.id.card_1_weather_status);
        city_state_view = getView().findViewById(R.id.card_1_city_state);


        String weatherStatue = getWeatherStatue(weatherCode, 1);
        System.out.println(weatherStatue);
        imageView.setImageResource(resID);
        status_view.setText(weatherStatue);
        city_state_view.setText(cityAndState);
        hum = getView().findViewById(R.id.card_2_humidity);
        windSpeed = getView().findViewById(R.id.card_2_wind_speed);
        visibility = getView().findViewById(R.id.card_2_visibility);
        pressure = getView().findViewById(R.id.card_2_pressure);
        hum.setText(val_hum);
        windSpeed.setText(val_windSpeed);
        visibility.setText(val_visibility);
        pressure.setText(val_pressure);
        weatherTable = getView().findViewById(R.id.card3_weather_table);
        weatherTable.setLayoutManager(new LinearLayoutManager(context));

        WeatherTableAdaptor adaptor = new WeatherTableAdaptor(context);
        adaptor.setEightDayData((ArrayList<JSONObject>) eightDayData);
        if (adaptor != null) {
            weatherTable.setAdapter(adaptor);
            weatherTable.setNestedScrollingEnabled(true);
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
    public void getData() {
        Intent intent = new Intent(getActivity(), Detail.class);

        System.out.println("11111" + cityAndState);
        intent.putExtra("cityAndState", cityAndState);
        intent.putExtra("allData", String.valueOf(allData));
        startActivity(intent);
    }
    public void setJsonData(JSONObject jsonObject) {
        this.toActivityData = jsonObject;
    }
    public JSONObject getJsonData() {
        return toActivityData;
    }
//    public List<JSONObject> getData() {
//        return eightDayData;
//    }
//    public String getCityAndState() {
//        return cityAndState;
//    }
//private JSONObject getDataFromDetail() {
//    data = ((DashBoard)getActivity()).getData();
//    return data;
//
//}

//    private String getId2() {
//        cityId = ((DashBoard)getActivity()).getId();
//        return cityId;
//
//    }

}