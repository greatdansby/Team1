package com.example.testhi;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReviewEhrActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.review_ehr);
		
		ArrayList<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(4f, 0));
		entries.add(new BarEntry(8f, 1));
		entries.add(new BarEntry(6f, 2));
		entries.add(new BarEntry(12f, 3));
		entries.add(new BarEntry(18f, 4));
		entries.add(new BarEntry(9f, 5));
		
		BarDataSet dataset = new BarDataSet(entries, "# of Calls");
		
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("January"); 
		labels.add("February"); 
		labels.add("March"); 
		labels.add("April"); 
		labels.add("May");
		labels.add("June");
		
		BarChart chart = new BarChart(this);
		BarChart chart2 = new BarChart(this);
		LinearLayout myCharts = (LinearLayout) findViewById(R.id.chartView);
		myCharts.addView(chart,700,325);
		myCharts.addView(chart2,700,325);
		
		BarData data = new BarData(labels, dataset);
		chart.setData(data);
		chart2.setData(data);
		
		chart.setDescription("# of times Alice called Bob");
		dataset.setColors(ColorTemplate.COLORFUL_COLORS);
		chart.animateY(5000);
	}

}
