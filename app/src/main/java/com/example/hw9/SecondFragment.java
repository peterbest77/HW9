package com.example.hw9;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.HIGradient;
import com.highsoft.highcharts.common.HIStop;
import com.highsoft.highcharts.core.*;
import com.highsoft.highcharts.common.hichartsclasses.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private JSONObject data;
    private Object[][] objects;
    private List<JSONObject> fifteenDayDataList;
    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fifteenDayDataList = new ArrayList<>();
        JSONObject jsonObject = getDataFromDetail();
        System.out.println(jsonObject);
        try {
            JSONArray fifteenDaysData = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals");
            for (int i = 0; i < 15; ++i) {

                    fifteenDayDataList.add(fifteenDaysData.getJSONObject(i));

            }
            objects = new Object[15][3];
            for (int i = 0; i < 15; ++i) {

                    objects[i][0] = fifteenDayDataList.get(i).get("startTime").toString().substring(0, 10);
                    objects[i][1] = fifteenDayDataList.get(i).getJSONObject("values").getDouble("temperatureMin");
                    objects[i][2] = fifteenDayDataList.get(i).getJSONObject("values").getDouble("temperatureMax");


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_second, container, false);
    }
    private JSONObject getDataFromDetail() {
        data = ((Detail)getActivity()).getData();
        return data;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HIChartView chartView = getView().findViewById(R.id.chart1);

        HIOptions options = new HIOptions();

        HIChart chart = new HIChart();
        chart.setType("arearange");
        chart.setZoomType("x");
        options.setChart(chart);

        HITitle title = new HITitle();
        title.setText("Temperature variation by day");
        options.setTitle(title);

        HIXAxis xaxis = new HIXAxis();
        xaxis.setType("time");
        xaxis.setTickInterval(2);
        options.setXAxis(new ArrayList<HIXAxis>(){{add(xaxis);}});

        HIYAxis yaxis = new HIYAxis();
        yaxis.setTitle(new HITitle());
        options.setYAxis(new ArrayList<HIYAxis>(){{add(yaxis);}});

        HITooltip tooltip = new HITooltip();
        tooltip.setShadow(true);
        tooltip.setValueSuffix("°F");
        options.setTooltip(tooltip);

        HILegend legend = new HILegend();
        legend.setEnabled(false);
        options.setLegend(legend);

        HIArearange series = new HIArearange();
        series.setName("Temperatures");

        Object[][] seriesData = objects;
        series.setData(new ArrayList<>(Arrays.asList(seriesData)));
        options.setSeries(new ArrayList<>(Arrays.asList(series)));
        HIPlotOptions plotOptions = new HIPlotOptions();
        HISeries plot = new HISeries();
        HIGradient gradient = new HIGradient(0,0.3f, 0.6f,1);
        LinkedList<HIStop> stops = new LinkedList<>();
        stops.add(new HIStop(0, HIColor.initWithRGB(247, 180, 80)));
        stops.add(new HIStop(1, HIColor.initWithRGB(120, 190, 240)));
        plot.setColor(HIColor.initWithLinearGradient(gradient, stops));
        plotOptions.setSeries(plot);
        options.setPlotOptions(plotOptions);
        chartView.setOptions(options);
    }
}