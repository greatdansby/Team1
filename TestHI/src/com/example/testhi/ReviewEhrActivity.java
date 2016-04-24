package com.example.testhi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ReviewEhrActivity extends Activity implements myInterface {
	
	fetchDataFromAppServer getAnalyticData = new fetchDataFromAppServer();
	fetchDataFromAppServer getObservationData = new fetchDataFromAppServer();
	fetchDataFromAppServer getConditionData = new fetchDataFromAppServer();
	fetchDataFromAppServer getVitalsData = new fetchDataFromAppServer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.review_ehr);
	}
	protected void onStart(){
		super.onStart();
		
		getAnalyticData.delegate = this;
		getAnalyticData.execute("a","Analytics/Risks/?patientId="+MainActivity.user.getTag());
		
		getVitalsData.delegate = this;
		getVitalsData.execute("d","Analytics/VitalsRadar/?patientId="+MainActivity.user.getTag());
		
		getObservationData.delegate = this;
		getObservationData.execute("b","GetObservationsFHIR_SQL/?patientId="+MainActivity.user.getTag());
		
		getConditionData.delegate = this;
		getConditionData.execute("c","GetConditionsFHIR_SQL/?patientId="+MainActivity.user.getTag());

	}
	
	@SuppressLint("NewApi")
	@Override
	public void processFinish(String output, String id) {

		JSONArray dataArray = null;
		JSONArray baselineArray = null;
		JSONArray resultsArray = null;
		LinearLayout parent;
		LinearLayout child;
		JSONObject allInfo;		
		JSONObject meta = null;
		int records = 0;
				
		switch(id) {
			case "a": //Add analytics charts
				
				
				//Parse JSON
				
				try {
					allInfo = new JSONObject(output);
					meta = allInfo.getJSONObject("chartMeta");
					dataArray = allInfo.getJSONArray("results");
					records = meta.getInt("recordCount");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Create data entries
				
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
		
				//Configure charts
				
				BarDataSet dataset = new BarDataSet(entries, "Risk Percentage");
				
				BarChart chart = new BarChart(this);
				LinearLayout myCharts = (LinearLayout) findViewById(R.id.chartView);
				myCharts.addView(chart,((View) myCharts.getParent()).getWidth(),myCharts.getHeight());
				
				BarData barData = new BarData(labels, dataset);
				chart.setData(barData);
				
				chart.setDescription("Disease Risk Assessment");
				dataset.setColors(ColorTemplate.COLORFUL_COLORS);
				chart.animateY(5000);
				break;
		
			case "b":
				
				//Add observation data
				JSONArray j = null;
				
				try {
					j = new JSONArray(output);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Capture observations (Body weight Measured, Body mass index (BMI) [Ratio], Body height, Body temperature, Diastolic blood pressure, Heart rate, Systolic blood pressure, Respiratory rate)
				
				JSONArray data1 = new JSONArray(); //Body weight Measured
				JSONArray data2 = new JSONArray(); //Body mass index (BMI) [Ratio]
				JSONArray data3 = new JSONArray(); //Body height
				JSONArray data4 = new JSONArray(); //Body temperature
				JSONArray data5 = new JSONArray(); //Diastolic blood pressure
				JSONArray data6 = new JSONArray(); //Heart rate
				JSONArray data7 = new JSONArray(); //Systolic blood pressure
				JSONArray data8 = new JSONArray(); //Respiratory rate
				
				//Create data entries
				
				for (int i=0; i < j.length(); i++){
					
					try {
						switch(((JSONObject) j.get(i)).getString("Observation")){
							case "Body weight Measured":
								data1.put(j.get(i));
								break;
							case "Body mass index (BMI) [Ratio]":
								data2.put(j.get(i));
								break;
							case "Body height":
								data3.put(j.get(i));
								break;
							case "Body temperature":
								data4.put(j.get(i));
								break;
							case "Diastolic blood pressure":
								data5.put(j.get(i));
								break;
							case "Heart rate":
								data6.put(j.get(i));
								break;
							case "Systolic blood pressure":
								data7.put(j.get(i));
								break;
							case "Respiratory rate":
								data8.put(j.get(i));
								break;							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
						
				//Sort arrays by date, populate views with latest record
				try {
					TextView OB1 = (TextView) this.findViewById(R.id.txtHealthInfo1);			
					OB1.setText(((JSONObject) data1.get(0)).getString("Observation") + ":");
					TextView OB1Value = (TextView) this.findViewById(R.id.txtHealthInfo1Value);
					OB1Value.setText(((JSONObject) data1.get(0)).getString("Value"));
					TextView OB1Units = (TextView) this.findViewById(R.id.txtHealthInfo1Units);
					OB1Units.setText(((JSONObject) data1.get(0)).getString("Unit"));
					TextView OB1Date = (TextView) this.findViewById(R.id.txtHealthInfo1Date);
					OB1Date.setText(((JSONObject) data1.get(0)).getString("Date"));
					
					TextView OB2 = (TextView) this.findViewById(R.id.txtHealthInfo2);
					OB2.setText(((JSONObject) data2.get(0)).getString("Observation") + ":");
					TextView OB2Value = (TextView) this.findViewById(R.id.txtHealthInfo2Value);
					OB2Value.setText(((JSONObject) data2.get(0)).getString("Value"));
					TextView OB2Units = (TextView) this.findViewById(R.id.txtHealthInfo2Units);
					OB2Units.setText(((JSONObject) data2.get(0)).getString("Unit"));
					TextView OB2Date = (TextView) this.findViewById(R.id.txtHealthInfo2Date);
					OB2Date.setText(((JSONObject) data2.get(0)).getString("Date"));
					
					TextView OB3 = (TextView) this.findViewById(R.id.txtHealthInfo3);
					OB3.setText(((JSONObject) data3.get(0)).getString("Observation") + ":");
					TextView OB3Value = (TextView) this.findViewById(R.id.txtHealthInfo3Value);
					OB3Value.setText(((JSONObject) data3.get(0)).getString("Value"));
					TextView OB3Units = (TextView) this.findViewById(R.id.txtHealthInfo3Units);
					OB3Units.setText(((JSONObject) data3.get(0)).getString("Unit"));
					TextView OB3Date = (TextView) this.findViewById(R.id.txtHealthInfo3Date);
					OB3Date.setText(((JSONObject) data3.get(0)).getString("Date"));
					
					TextView OB4 = (TextView) this.findViewById(R.id.txtHealthInfo4);
					OB4.setText(((JSONObject) data4.get(0)).getString("Observation") + ":");
					TextView OB4Value = (TextView) this.findViewById(R.id.txtHealthInfo4Value);
					OB4Value.setText(((JSONObject) data4.get(0)).getString("Value"));
					TextView OB4Units = (TextView) this.findViewById(R.id.txtHealthInfo4Units);
					OB4Units.setText(((JSONObject) data4.get(0)).getString("Unit"));
					TextView OB4Date = (TextView) this.findViewById(R.id.txtHealthInfo4Date);
					OB4Date.setText(((JSONObject) data4.get(0)).getString("Date"));
					
					TextView OB5 = (TextView) this.findViewById(R.id.txtHealthInfo5);
					OB5.setText(((JSONObject) data5.get(0)).getString("Observation") + ":");
					TextView OB5Value = (TextView) this.findViewById(R.id.txtHealthInfo5Value);
					OB5Value.setText(((JSONObject) data5.get(0)).getString("Value"));
					TextView OB5Units = (TextView) this.findViewById(R.id.txtHealthInfo5Units);
					OB5Units.setText(((JSONObject) data5.get(0)).getString("Unit"));
					TextView OB5Date = (TextView) this.findViewById(R.id.txtHealthInfo5Date);
					OB5Date.setText(((JSONObject) data5.get(0)).getString("Date"));
					
					TextView OB6 = (TextView) this.findViewById(R.id.txtHealthInfo6);
					OB6.setText(((JSONObject) data6.get(0)).getString("Observation") + ":");
					TextView OB6Value = (TextView) this.findViewById(R.id.txtHealthInfo6Value);
					OB6Value.setText(((JSONObject) data6.get(0)).getString("Value"));
					TextView OB6Units = (TextView) this.findViewById(R.id.txtHealthInfo6Units);
					OB6Units.setText(((JSONObject) data6.get(0)).getString("Unit"));
					TextView OB6Date = (TextView) this.findViewById(R.id.txtHealthInfo6Date);
					OB6Date.setText(((JSONObject) data6.get(0)).getString("Date"));
					
					TextView OB7 = (TextView) this.findViewById(R.id.txtHealthInfo7);
					OB7.setText(((JSONObject) data7.get(0)).getString("Observation") + ":");
					TextView OB7Value = (TextView) this.findViewById(R.id.txtHealthInfo7Value);
					OB7Value.setText(((JSONObject) data7.get(0)).getString("Value"));
					TextView OB7Units = (TextView) this.findViewById(R.id.txtHealthInfo7Units);
					OB7Units.setText(((JSONObject) data7.get(0)).getString("Unit"));
					TextView OB7Date = (TextView) this.findViewById(R.id.txtHealthInfo7Date);
					OB7Date.setText(((JSONObject) data7.get(0)).getString("Date"));
					
					TextView OB8 = (TextView) this.findViewById(R.id.txtHealthInfo8);
					OB8.setText(((JSONObject) data8.get(0)).getString("Observation") + ":");
					TextView OB8Value = (TextView) this.findViewById(R.id.txtHealthInfo8Value);
					OB8Value.setText(((JSONObject) data8.get(0)).getString("Value"));
					TextView OB8Units = (TextView) this.findViewById(R.id.txtHealthInfo8Units);
					OB8Units.setText(((JSONObject) data8.get(0)).getString("Unit"));
					TextView OB8Date = (TextView) this.findViewById(R.id.txtHealthInfo8Date);
					OB8Date.setText(((JSONObject) data8.get(0)).getString("Date"));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
						
				//Load chart data
				
				LinearLayout myOBCharts = (LinearLayout) findViewById(R.id.chartView);

				addChart(myOBCharts,data1,"Body Weight", "txtHealthInfo1Chart");
				addChart(myOBCharts,data2,"Body Mass Index (BMI)", "txtHealthInfo2Chart");
				addChart(myOBCharts,data3,"Body Height", "txtHealthInfo3Chart");
				addChart(myOBCharts,data4,"Body Temperature", "txtHealthInfo4Chart");
				addChart(myOBCharts,data5,"Diastolic Blood Pressure", "txtHealthInfo5Chart");
				addChart(myOBCharts,data6,"Heart Rate", "txtHealthInfo6Chart");
				addChart(myOBCharts,data7,"Systolic blood pressure", "txtHealthInfo7Chart");
				addChart(myOBCharts,data8,"Respiratory rate", "txtHealthInfo8Chart");				
				
				break;
			case "c":
				
				//Add condition data
				
				JSONArray c = null;
				
				try {
					c = new JSONArray(output);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Capture conditions (Essential hypertension, Dizzy spells, Lightheadedness, Morning headache)

				JSONArray dataC1 = new JSONArray(); //Essential hypertension
				JSONArray dataC2 = new JSONArray(); //Dizzy spells
				JSONArray dataC3 = new JSONArray(); //Lightheadedness
				JSONArray dataC4 = new JSONArray(); //Morning headache

				
				//Create data entries
				
				for (int i=0; i < c.length(); i++){
					
					try {
						switch(((JSONObject) c.get(i)).getString("Disease")){
							case " Essential hypertension ":
								dataC1.put(c.get(i));
								break;
							case " Dizzy spells ":
								dataC2.put(c.get(i));
								break;
							case " Lightheadedness ":
								dataC3.put(c.get(i));
								break;
							case " Morning headache ":
								dataC4.put(c.get(i));
								break;	
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
						
				//Sort arrays by date, populate views with latest record
				try {

					TextView C1 = (TextView) this.findViewById(R.id.txtConditions1);
					C1.setText(((JSONObject) dataC1.get(0)).getString("Disease"));
					TextView C1Value = (TextView) this.findViewById(R.id.txtConditions1Value);
					C1Value.setText("");
					TextView C1Units = (TextView) this.findViewById(R.id.txtConditions1Units);
					C1Units.setText("");
					TextView C1Date = (TextView) this.findViewById(R.id.txtConditions1Date);
					C1Date.setText(((JSONObject) dataC1.get(0)).getString("Date"));
					
					TextView C2 = (TextView) this.findViewById(R.id.txtConditions2);
					C2.setText(((JSONObject) dataC2.get(0)).getString("Disease"));
					TextView C2Value = (TextView) this.findViewById(R.id.txtConditions2Value);
					C2Value.setText("");
					TextView C2Units = (TextView) this.findViewById(R.id.txtConditions2Units);
					C2Units.setText("");
					TextView C2Date = (TextView) this.findViewById(R.id.txtConditions2Date);
					C2Date.setText(((JSONObject) dataC2.get(0)).getString("Date"));
					
					TextView C3 = (TextView) this.findViewById(R.id.txtConditions3);
					C3.setText(((JSONObject) dataC3.get(0)).getString("Disease"));
					TextView C3Value = (TextView) this.findViewById(R.id.txtConditions3Value);
					C3Value.setText("");
					TextView C3Units = (TextView) this.findViewById(R.id.txtConditions3Units);
					C3Units.setText("");
					TextView C3Date = (TextView) this.findViewById(R.id.txtConditions3Date);
					C3Date.setText(((JSONObject) dataC3.get(0)).getString("Date"));
					
					TextView C4 = (TextView) this.findViewById(R.id.txtConditions4);
					C4.setText(((JSONObject) dataC4.get(0)).getString("Disease"));
					TextView C4Value = (TextView) this.findViewById(R.id.txtConditions4Value);
					C4Value.setText("");
					TextView C4Units = (TextView) this.findViewById(R.id.txtConditions4Units);
					C4Units.setText("");
					TextView C4Date = (TextView) this.findViewById(R.id.txtConditions4Date);
					C4Date.setText(((JSONObject) dataC4.get(0)).getString("Date"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Load chart data
				
				LinearLayout mCCharts = (LinearLayout) findViewById(R.id.chartView);

				addChart(mCCharts,dataC1,"Essential hypertension", "txtConditions1Chart");
				addChart(mCCharts,dataC2,"Dizzy spells", "txtConditions2Chart");
				addChart(mCCharts,dataC3,"Lightheadedness", "txtConditions3Chart");
				addChart(mCCharts,dataC4,"Morning headache", "txtConditions4Chart");
				
				break;
			case "d": //Add analytics charts				
				
				//Parse JSON
				
				try {
					allInfo = new JSONObject(output);
					meta = allInfo.getJSONObject("chartMeta");
					resultsArray = allInfo.getJSONArray("results");
					baselineArray = allInfo.getJSONArray("baseline");
					records = meta.getInt("recordCount");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Create data entries
				
				ArrayList<Entry> baselineEntries = new ArrayList<>();
				ArrayList<Entry> resultsEntries = new ArrayList<>();
				ArrayList<String> labelList = new ArrayList<String>();
				for (int i=0; i < records; i++){
					JSONObject data;
					try {
						data = (JSONObject) baselineArray.get(i);
						baselineEntries.add(new Entry((float) data.getInt("rank"), i));
						labelList.add(data.getString("observation"));
						data = (JSONObject) resultsArray.get(i);
						resultsEntries.add(new Entry((float) data.getInt("rank"), i));
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
				//Configure charts
				
				RadarDataSet dataset_comp1 = new RadarDataSet(baselineEntries, "Baseline");
				RadarDataSet dataset_comp2 = new RadarDataSet(resultsEntries, "Me");
				dataset_comp1.setColor(Color.CYAN);				 
				dataset_comp2.setColor(Color.RED);
				dataset_comp1.setDrawFilled(true);				 
				dataset_comp2.setDrawFilled(true);
				
				ArrayList<RadarDataSet> dataSets = new ArrayList<>();
				dataSets.add(dataset_comp1);
				dataSets.add(dataset_comp2);
				
				RadarData radarDataset = new RadarData(labelList, dataSets);
				
				RadarChart chart2 = new RadarChart(this);
				LinearLayout myCharts2 = (LinearLayout) findViewById(R.id.chartView);
				myCharts2.addView(chart2,((View) myCharts2.getParent()).getWidth(),myCharts2.getHeight());
				
				chart2.setData(radarDataset);
				
				chart2.setDescription("Vitals Radar Chart");
				chart2.animateY(5000);
				break;
				
	}
	}
	
	public void addChart(LinearLayout myCharts, JSONArray chartData, String description, String tag){
		JSONObject data = null;
		List<Entry> entriesOB = new ArrayList<>();
		ArrayList<String> labelsOB = new ArrayList<String>();
		
		for (int i=0; i < chartData.length(); i++){
			try {
				data = (JSONObject) chartData.get(i);
				entriesOB.add(new BarEntry((float) data.getInt("Value"), i));
			labelsOB.add(data.getString("Date"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		LineDataSet dataset = new LineDataSet(entriesOB, "Value");
		
		LineChart chart1 = new LineChart(this);
		chart1.setTag(tag);				
		myCharts.addView(chart1,((View) myCharts.getParent()).getWidth(),((View) myCharts.getParent()).getHeight());			
		LineData barData = new LineData(labelsOB, dataset);
		chart1.setData(barData);				
		chart1.setDescription(description);
		dataset.setColors(ColorTemplate.COLORFUL_COLORS);
		chart1.animateY(5000);
	}
	
	public void flagToggle(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Record flagged for review with doctor")
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
		
		TextView t = (TextView) v;
		switch (v.getId()) {
	    case (R.id.txtHealthInfo1Flag):
	    	
	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtHealthInfo2Flag):
	    	
	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtHealthInfo3Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtHealthInfo4Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtHealthInfo5Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtHealthInfo6Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtHealthInfo7Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtHealthInfo8Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtConditions1Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtConditions2Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtConditions3Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
	    case (R.id.txtConditions4Flag):

	    	t.setBackgroundResource(R.drawable.bluebutton);
	    	t.setTextColor(Color.RED);
	    	break;
		}
	}
	public void scrollCharts(View v){
		final HorizontalScrollView s = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		switch (v.getId()) {
	    case (R.id.txtHealthInfo1Chart):
	    	
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtHealthInfo1Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtHealthInfo2Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtHealthInfo2Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtHealthInfo3Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtHealthInfo3Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtHealthInfo4Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtHealthInfo4Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtHealthInfo5Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtHealthInfo5Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtHealthInfo6Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtHealthInfo6Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtHealthInfo7Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtHealthInfo7Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtHealthInfo8Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtHealthInfo8Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtConditions1Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtConditions1Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtConditions2Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtConditions2Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtConditions3Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtConditions3Chart").getX(), 0);
	    	        } 
	    	});
    		break;
	    case (R.id.txtConditions4Chart):
	    	   s.post(new Runnable() { 
	    	        public void run() { 
	    	             s.smoothScrollTo((int) s.findViewWithTag("txtConditions4Chart").getX(), 0);
	    	        } 
	    	});
    		break;
    		
		}
	}
	public void scrollEHR(View v){
		switch (v.getId()) {
	    case (R.id.btnHealthInfo):
	    	findViewById(R.id.scrollView1).scrollTo(0, (int) findViewById(R.id.txtHealthInfo1).getY());
	    	break;
	    case (R.id.btnConditions):
	    	
	    	ScrollView s = (ScrollView) findViewById(R.id.scrollView1);	    	
	    	LinearLayout x1 = (LinearLayout) findViewById(R.id.txtConditions1).getParent();
	    	LinearLayout x2 = (LinearLayout) x1.getParent();
	    	LinearLayout x3 = (LinearLayout) x2.getParent();
	    	LinearLayout x4 = (LinearLayout) x3.getParent();
	    	s.smoothScrollTo(0, (int) x3.getTop());
	    	break;
	    	
	    case (R.id.btnDoctorsVisits):
	    	findViewById(R.id.scrollView1).scrollTo(0, 0);
	    	break;
	    case (R.id.btnProcedures):
	    	findViewById(R.id.scrollView1).scrollTo(0, 0);
	    	break;
	    case (R.id.btnMedications):
	    	findViewById(R.id.scrollView1).scrollTo(0, 0);
	    	break;
	    case (R.id.btnTestResults):
	    	findViewById(R.id.scrollView1).scrollTo(0, 0);
	    	break;
	    case (R.id.btn3rdParty):
	    	findViewById(R.id.scrollView1).scrollTo(0, 0);
	    	break;
		}
	}

}
