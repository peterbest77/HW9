package com.example.hw9;

import androidx.annotation.NonNull;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultActivity extends AppCompatActivity {
    private String autoCity;
    private String autoState;
    private String randomInput;
    private Toolbar actionBar;
    private RequestQueue requestQueue;
    private String url;
    private String cityAndState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        actionBar = findViewById(R.id.myToolBar3);
        setSupportActionBar(actionBar);

        ProgressBar progressBar = findViewById(R.id.my_progress_bar2);
        View fragment = findViewById(R.id.fragment2);
        fragment.setVisibility(View.INVISIBLE);
        TextView textView = findViewById(R.id.text_view_result2);
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        requestQueue = Volley.newRequestQueue(this);
        if (getIntent().getStringExtra("city") != null && getIntent().getStringExtra("state") != null) {
            autoCity = getIntent().getStringExtra("city");
            autoState = getIntent().getStringExtra("state");
            System.out.println(autoCity + " + " + autoState);
            cityAndState = autoCity+", "+ autoState + ", USA";
            setTitle(autoCity+", "+ autoState + ", USA");
            url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + autoCity + "+" + autoState +"&key=AIzaSyCPX__rKKqlkJ_my9RfEbYFWQykPEB9SwU";
        }
        else {
            randomInput = getIntent().getStringExtra("random");
            System.out.println(randomInput);
            cityAndState = randomInput;
            setTitle(randomInput);
            url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + randomInput +"&key=AIzaSyCPX__rKKqlkJ_my9RfEbYFWQykPEB9SwU";

        }




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
                                bundle.putString("ip", cityAndState);
                                bundle.putString("floatButton", "disappear");
                                BlankFragment1 blankFragment2 = new BlankFragment1();
                                blankFragment2.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, blankFragment2).commit();
                                System.out.println("__________________________________________");
                                System.out.println(bundle.getString("data"));
                                System.out.println(bundle);
                                System.out.println("__________________________________________");

//                    BlankFragment1 blankFragment2 = new BlankFragment1();
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, blankFragment2).commit();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {


                                progressBar.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                                fragment.setVisibility(View.VISIBLE);

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


//        setTitle(cityAndState);
//        Toolbar detailActionBar = (Toolbar) findViewById(R.id.myToolBar3);
//        setSupportActionBar(detailActionBar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar

//        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(cityAndState);

        actionBar.setDisplayHomeAsUpEnabled(true);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu3, menu);
//
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////        switch (item.getItemId()) {
////
////            case R.id.myToolBar3:
////                this.finish();
////                return true;
////        }
//        return super.onOptionsItemSelected(item);
//    }
}