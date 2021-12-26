package com.example.hw9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//

//

public class Detail extends AppCompatActivity {
    private Toolbar actionBar;
    private ArrayList eightDaysData;
    private String cityAndState;
    private JSONObject jsonObject;
    private JSONObject data;
    private String temperature;
    private JSONObject fromFragmentData;
    String b;

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdaptor adaptor;
    private String windSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);
        actionBar = findViewById(R.id.myToolBar2);
        setSupportActionBar(actionBar);
        FragmentManager fm = getSupportFragmentManager();
        adaptor = new FragmentAdaptor(fm, getLifecycle());
        pager2.setAdapter(adaptor);

        TabLayout.Tab tab1 = tabLayout.newTab();
        TabLayout.Tab tab2 = tabLayout.newTab();
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab1.setIcon(R.drawable.calendar_today);
        tab2.setIcon(R.drawable.trending_up);
        tab3.setIcon(R.drawable.thermometer_low);
        tab1.setText("TODAY");
        tab2.setText("WEEKLY");
        tab3.setText("WEATHER DATA");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);


//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.calendar_today));
//        tabLayout.addTab(tabLayout.newTab().setText("TODAY"));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.trending_up));
//        tabLayout.addTab(tabLayout.newTab().setText("WEEKLY"));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.thermometer_low));
//        tabLayout.addTab(tabLayout.newTab().setText("WEATHER DATA"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



//        ActionBar actionBar1 = getSupportActionBar();
        String data = getIntent().getStringExtra("allData");
        String city = getIntent().getStringExtra("cityAndState");
        try {
           jsonObject = new JSONObject(data);
           cityAndState = city;
            JSONArray fifteenDaysData = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals");
            System.out.println(fifteenDaysData);
            JSONObject currentDayData = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values");
            temperature = (int)currentDayData.getDouble("temperature") + "°F";
            setData(jsonObject);
            TextView text = findViewById(R.id.detail_today_wind_speed);

            windSpeed = currentDayData.getDouble("windSpeed") + "mph";

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("1111232342342424242424");
        System.out.println(jsonObject);
        System.out.println(cityAndState);
        System.out.println("1111232342342424242424");

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar

//        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(cityAndState);

        actionBar.setDisplayHomeAsUpEnabled(true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(Detail.this, DashBoard.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 1000);

        //

        //




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.twitter:
                StringBuilder builder = new StringBuilder();
                String url = "https://twitter.com/intent/tweet?text=";
                builder.append("Check out " + cityAndState +"'s weather! It is "
                        + temperature + "!&hashtags=CSCI571WeatherSearch");
                url += builder.toString();
                Intent tweet = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(tweet);
                return true;
            case R.id.myToolBar2:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public JSONObject getData() {
        return data;
    }
    public void setData(JSONObject jsonObject) {
        this.data = jsonObject;
    }

    public void drawWeekly() {


    }

}