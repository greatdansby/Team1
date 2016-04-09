package com.example.testhi;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReviewEhrActivity extends Activity implements myInterface {
	
	fetchDataFromAppServer getChartData = new fetchDataFromAppServer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.review_ehr);
	}
	protected void onStart(){
		super.onStart();
		
		getChartData.delegate = this;
		getChartData.execute("a","Analytics/Risks/?patientId="+MainActivity.user.getTag());
	}
	
	@SuppressLint("NewApi")
	@Override
	public void processFinish(String output, String id) {

		JSONArray dataArray = null;
		LinearLayout parent;
		LinearLayout child;
		JSONObject allInfo;		
		JSONObject meta = null;
		int records = 0;
				
		switch(id) {
			case "a": //Add charts
				
				try {
					allInfo = new JSONObject(output);
					meta = allInfo.getJSONObject("chartMeta");
					dataArray = allInfo.getJSONArray("results");
					records = meta.getInt("recordCount");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//data = allInfo.getJSONObject("results");
				
		ArrayList<BarEntry> entries = new ArrayList<>();
		ArrayList<String> labels = new ArrayList<String>();
		for (int i=0; i < records; i++){
			JSONObject data;
			try {
				data = (JSONObject) dataArray.get(i);
				entries.add(new BarEntry((float) data.getInt("riskPercentage"), i));
				labels.add(data.getString("disease"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		BarDataSet dataset = new BarDataSet(entries, "Risk Percentage");
		
		BarChart chart = new BarChart(this);
		BarChart chart2 = new BarChart(this);
		LinearLayout myCharts = (LinearLayout) findViewById(R.id.chartView);
		myCharts.addView(chart,700,325);
		myCharts.addView(chart2,700,325);
		
		BarData barData = new BarData(labels, dataset);
		chart.setData(barData);
		chart2.setData(barData);
		
		chart.setDescription("Disease Risk Assessment");
		dataset.setColors(ColorTemplate.COLORFUL_COLORS);
		chart.animateY(5000);
		break;
	}
	}
	public void flagToggle(View v, Integer i){
		
	}
	public void scrollCharts(View v, Integer i){
		
	}
	public void scrollEHR(View v, Integer i){
		
	}

}
