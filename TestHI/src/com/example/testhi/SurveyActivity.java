package com.example.testhi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SurveyActivity extends Activity implements myInterface {
	
	fetchDataFromAppServer fetchSurveyNames = new fetchDataFromAppServer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity);
        
        fetchSurveyNames.delegate = this;
        fetchSurveyNames.execute("a","GetAllSurveys/");
        
	}

	@Override
	public void processFinish(String output, String id) {
		switch(id) {
			case "a":
				
				JSONObject e;
				JSONArray jArray;
				LinearLayout parent;
				LinearLayout child;
				try {
					
					jArray = new JSONArray(output);
					parent = (LinearLayout) findViewById(R.id.allSurveys);
					
					LayoutInflater inflater = (LayoutInflater)getBaseContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					
			        for(int i = 0; i < jArray.length(); i++) {
			            JSONObject p = (JSONObject) jArray.get(i);
			            child = (LinearLayout) inflater.inflate(R.layout.survey, null);
			            TextView txt = (TextView) child.findViewById(R.id.survey_name);
			            txt.setText(p.getString("survey_name"));
			            child.setTag(p.getInt("id"));
			            parent.addView(child);
			        }
				
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}		
	}
	public void setSurvey(){
		Log.d("info","Setting Survey");
		
	}
		
}
