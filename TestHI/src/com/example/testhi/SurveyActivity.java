package com.example.testhi;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
		JSONObject e;
		JSONArray jArray;
		LinearLayout parent;
		LinearLayout child;
		switch(id) {
			case "a": //Populate survey list
						
				try {
					
					jArray = new JSONArray(output);
					parent = (LinearLayout) findViewById(R.id.allSurveys);
					
					LayoutInflater inflater = (LayoutInflater)getBaseContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					
			        for(int i = 0; i < jArray.length(); i++) {
			            JSONObject p = (JSONObject) jArray.get(i);
			            child = (LinearLayout) inflater.inflate(R.layout.survey, null);
			            TextView txt = (TextView) child.findViewById(R.id.survey_name);
			            txt.setText(p.getString("survey_name"));
			            TextView btn = (TextView) child.findViewById(R.id.survey_button);
			            btn.setTag(p.getInt("id"));
			            btn.setOnClickListener(new OnClickListener(){
			            	public void onClick(View v){
			            		fillSurvey(v);
			            	}
			            });
			            child.setTag(p.getInt("id"));
			            parent.addView(child);
			        }
				
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			case "b": //Populate survey questions

				try {
					
					jArray = new JSONArray(output);
					parent = (LinearLayout) findViewById(R.id.surveyQuestions);
					parent.removeAllViews();
					LayoutInflater inflater = (LayoutInflater)getBaseContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					
			        for(int i = 0; i < jArray.length(); i++) {
			            JSONObject p = (JSONObject) jArray.get(i);
			            child = (LinearLayout) inflater.inflate(R.layout.survey_question, null);
			            TextView txt = (TextView) child.findViewById(R.id.questionText);
			            txt.setText(p.getString("surveyQuestion_text"));
			            Spinner dropdown = (Spinner) child.findViewById(R.id.answerDropdown);
			            ArrayList<String> spinnerArray = new ArrayList<String>();
			            spinnerArray.add("Yes");
			            spinnerArray.add("No");
			            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
			            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			            dropdown.setAdapter(spinnerArrayAdapter);
			            child.setTag(p.getInt("id"));
			            parent.addView(child);
			        }
				
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			}		
	}

	public void fillSurvey(View v) {
		
		fetchDataFromAppServer fetchSurveyQuestions = new fetchDataFromAppServer();
		fetchSurveyQuestions.delegate = this;
		fetchSurveyQuestions.execute("b","GetSurveyQuestions/?surveyId="+v.getTag());
		
	}
	
}
