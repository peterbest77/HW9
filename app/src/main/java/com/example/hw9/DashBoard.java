package com.example.hw9;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashBoard extends AppCompatActivity {
    private List<BlankFragment1> blankFragment1List;
    private Toolbar toolbar;
    private RequestQueue requestQueue;
    private TextView temperature_view;
    private TextView status_view;
    private TextView city_state_view;
    private static double lat;
    private static double lng;
    private static String city;
    private static String state;
    private static int weatherCode;
    private static String temperature;
    private static String weatherStatus;
    private static String cityAndState;
    private ImageView imageView;
    private JSONObject data;
    private String cityId;
    private String autoCity;
    private String autoState;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        toolbar = findViewById(R.id.myToolBar);
        FloatingActionButton button6 = findViewById(R.id.bottom6);
        button6.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar);
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpaper);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewpager, true);
        Adapter myAdapter = new Adapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewpager.setAdapter(myAdapter);
        viewpager.setOffscreenPageLimit(10);
        ProgressBar progressBar = findViewById(R.id.my_progress_bar);
//        View fragment = findViewById(R.id.fragment1);
//        fragment.setVisibility(View.INVISIBLE);
        viewpager.setVisibility(View.INVISIBLE);
        TextView textView = findViewById(R.id.text_view_result);
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getApplicationContext().getSharedPreferences("mySharePreferences", Context.MODE_PRIVATE);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewpager.setCurrentItem(tab.getPosition());
                System.out.println(viewpager.getCurrentItem());
                FloatingActionButton button6 = findViewById(R.id.bottom6);

                int index = viewpager.getCurrentItem();
                if (index == 0) {
                    button6.setVisibility(View.INVISIBLE);
                }
                else {
                    button6.setVisibility(View.VISIBLE);
                }
                Fragment fragment = myAdapter.getItem(index);
                String cityname = null;
                if (fragment.getArguments() != null) {
                    String city = fragment.getArguments().getString("ip");
                    String[] cityandStateName = city.split(",");

                    System.out.println(cityandStateName[0]);
                    cityname = cityandStateName[0];
                }


                String finalCityname = cityname;
                button6.setOnClickListener(new View.OnClickListener() {



                    @Override
                    public void onClick(View v) {

                        Toast.makeText(DashBoard.this, finalCityname + " was removed from favorites", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(finalCityname);
//                        editor.clear();
                        editor.commit();


                        myAdapter.removeFragment(viewpager.getCurrentItem());
                        viewpager.setCurrentItem(tab.getPosition() - 1);

                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        String url_autoIp = "https://ipinfo.io/json?token=a0cca1bb9680cf";
        JsonObjectRequest autoIp = new JsonObjectRequest(Request.Method.GET, url_autoIp, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String[] location = response.get("loc").toString().split(",");
                    lat = Double.parseDouble(location[0]);
                    lng = Double.parseDouble(location[1]);
                    city = response.getString("city");
                    state = response.getString("region");
                    System.out.println("1");
                    System.out.println(lat);
                    System.out.println(lng);
                    System.out.println(city);
                    System.out.println(state);


                    String url = "https://csci571hw8-001.wl.r.appspot.com/tomorrow?lat="+ lat +"&lng=" + lng;

                    System.out.println("2" + url);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                setData(response);
                                JSONArray fifteenDaysData = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals");
                                System.out.println(fifteenDaysData);
                                JSONObject currentDayData = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values");
                                System.out.println(currentDayData);
                                weatherCode = currentDayData.getInt("weatherCode");
                                temperature = (int)currentDayData.getDouble("temperature") + "°F";
                                cityAndState = city + ", " + state;
                                System.out.println(weatherCode);
                                System.out.println(temperature);
                                System.out.println(cityAndState);
                                setId(cityAndState);
//                                temperature_view = findViewById(R.id.card_1_temperature);
//                                temperature_view.setText(temperature);
//                                imageView = findViewById(R.id.card_1_weather_icon);
//                                status_view = findViewById(R.id.card_1_weather_status);
//                                city_state_view = findViewById(R.id.card_1_city_state);
//                                String resName = getWeatherIcon(weatherCode, 1);
//                                String weatherStatue = getWeatherStatue(weatherCode, 1);
//                                int resID = getResId(resName, R.drawable.class);
//                                imageView.setImageResource(resID);
//                                status_view.setText(weatherStatue);
//                                city_state_view.setText(cityAndState);

                                Bundle bundle = new Bundle();
                                bundle.putString("data", String.valueOf(response));
                                bundle.putString("ip", cityAndState);

                                BlankFragment1 blankFragment1 = new BlankFragment1();
                                blankFragment1.setArguments(bundle);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.commit();
                                myAdapter.addFragment(blankFragment1);
//                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, blankFragment1).commit();
//                                System.out.println("__________________________________________");
                                System.out.println(bundle.getString("data"));
                                System.out.println(bundle);
                                System.out.println("__________________________________________");

                                sharedPreferences = getApplicationContext().getSharedPreferences("mySharePreferences", Context.MODE_PRIVATE);
                                Map<String,String> allData = (Map<String, String>) sharedPreferences.getAll();
                                System.out.println(allData);
                                for (Map.Entry<String, String> entry : allData.entrySet()) {
                                    String city = entry.getKey();
                                    System.out.println(city);
                                    String state = entry.getValue();
                                    System.out.println(state);
                                    String cityAndState1 = city + "," +state;
                                    String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + city + "+" + state +"key";

                                    String url1 = url;
                                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                double lat = (int)response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                                double lng = (int)response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                                System.out.println(lat +","+ lng);



                                                String url2 = "https://csci571hw8-001.wl.r.appspot.com/tomorrow?lat="+ lat +"&lng=" + lng;


                                                JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {

                                                            JSONArray fifteenDaysData = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals");
                                                            System.out.println(fifteenDaysData);
                                                            System.out.println("fangfucxirioqingyunzhibiyuepiaoyaoxiruoliufengzhihuixue");
                                                            JSONObject currentDayData = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values");
                                                            System.out.println(currentDayData);






                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("data", String.valueOf(response));
                                                            bundle.putString("ip", cityAndState1);

                                                            BlankFragment1 blankFragment2 = new BlankFragment1();
                                                            blankFragment2.setArguments(bundle);
                                                            FragmentManager fragmentManager1 = getSupportFragmentManager();
                                                             FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                                                             fragmentTransaction1.commit();
                                                             myAdapter.addFragment(blankFragment2);
                                                            System.out.println("__________________________________________");
                                                            System.out.println(bundle.getString("data"));
                                                            System.out.println(bundle);
                                                            System.out.println("__________________________________________");

//                                                           BlankFragment1 fragment1 = new BlankFragment1();
//                                    Bundle bundle1 = new Bundle();
//
//                                    fragment1.setArguments(bundle1);//数据传递到fragment中
//                                    FragmentManager fragmentManager1 = getSupportFragmentManager();
//                                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
//                                    fragmentTransaction1.commit();
//                                    myAdapter.addFragment(fragment1);


                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }finally {




                                                        }

                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                    }
                                                });
                                                requestQueue.add(request2);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                                    requestQueue.add(request);








                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {


                                progressBar.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
//                                fragment.setVisibility(View.VISIBLE);
                                viewpager.setVisibility(View.VISIBLE);

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    requestQueue.add(request);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(autoIp);



//        String url = "https://csci571hw8-001.wl.r.appspot.com/tomorrow?lat="+ lat +"&lng=" + lng;
//
//        System.out.println("2" + url);
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//
//                    JSONArray fifteenDaysData = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals");
//                    System.out.println(fifteenDaysData);
//                    JSONObject currentDayData = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values");
//                    System.out.println(currentDayData);
//                    weatherCode = currentDayData.getInt("weatherCode");
//                    temperature = (int)currentDayData.getDouble("temperature") + "°F";
//                    cityAndState = city + ", " + state;
//                    System.out.println(weatherCode);
//                    System.out.println(temperature);
//                    System.out.println(cityAndState);
//                    temperature_view = findViewById(R.id.card_1_temperature);
//                    temperature_view.setText(temperature);
//                    imageView = findViewById(R.id.card_1_weather_icon);
//                    status_view = findViewById(R.id.card_1_weather_status);
//                    city_state_view = findViewById(R.id.card_1_city_state);
//                    String resName = getWeatherIcon(weatherCode, 1);
//                    String weatherStatue = getWeatherStatue(weatherCode, 1);
//                    int resID = getResId(resName, R.drawable.class);
//                    imageView.setImageResource(resID);
//                    status_view.setText(weatherStatue);
//                    city_state_view.setText(cityAndState);
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("data", String.valueOf(response));
//                    bundle.putString("ip", cityAndState);
//                    BlankFragment1 blankFragment1 = new BlankFragment1();
//                    blankFragment1.setArguments(bundle);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, blankFragment1).commit();
//                    System.out.println("__________________________________________");
//                    System.out.println(bundle.getString("data"));
//                    System.out.println(bundle);
//                    System.out.println("__________________________________________");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }finally {
//                    progressBar.setVisibility(View.GONE);
//                    textView.setVisibility(View.GONE);
//                    fragment.setVisibility(View.VISIBLE);
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(request);


//        fragment.setVisibility(View.VISIBLE);



//
//        progressBar.setVisibility(View.GONE);
//        textView.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

//                Toast.makeText(DashBoard.this, "Search is Expanded", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Toast.makeText(DashBoard.this, "Search is collapse", Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        androidx.appcompat.widget.SearchView  searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.search).getActionView();
        androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete  =  searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setThreshold(2);

        searchView.setIconified(false);
        searchView.setQueryHint("Search");
        Context context = this;
        ArrayList<String> autoCompleteData = new ArrayList<>();
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                searchAutoComplete.setText("" + str);
//                Toast.makeText(DashBoard.this, "you clicked " + str, Toast.LENGTH_LONG).show();
                String[] cityAndState = str.split(",");
                    autoCity = cityAndState[0];
                    autoState = cityAndState[1];
//                Intent intent = new Intent(DashBoard.this, ResultActivity.class);
//                intent.putExtra("city", city);
//                intent.putExtra("state",state);
//                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String str = query;
//                searchAutoComplete.setText("" + str);
//                Toast.makeText(DashBoard.this, "you clicked " + str, Toast.LENGTH_LONG).show();



                Intent intent = new Intent(DashBoard.this, ResultActivity.class);
                if (autoCity != null && autoState != null) {
                    intent.putExtra("city", autoCity);
                    intent.putExtra("state",autoState);
                }
                else {
                    intent.putExtra("random", str);
                }
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ newText+ "&types=(cities)&key";

        System.out.println("3" + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                autoCompleteData.clear();
                try {

                    JSONArray datas = response.getJSONArray("predictions");
                    for (int i = 0; i < datas.length(); i++) {
                        String[] data = datas.getJSONObject(i).getString("description").split(",");
                        autoCompleteData.add(data[0] + "," + data[1]);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                context, R.layout.simple_dropdown_item_2line, autoCompleteData
                        );
                        searchAutoComplete.setAdapter(adapter);
                        adapter.getFilter().filter(newText);


                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
                return false;
            }
        });

        return true;
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

    public JSONObject getData() {
        return data;
    }
    public void setData(JSONObject jsonObject) {
        this.data = jsonObject;
    }
    public String getId() {
        return cityId;
    }
    public void setId(String cityAndState) {
        this.cityId = cityAndState;
    }
}


