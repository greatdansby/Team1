package com.example.testhi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SurveyActivity extends Activity implements myInterface {
	
	
	Integer survey = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity);
        
	}
	
	protected void onStart(){
		super.onStart();

		populateSurveyList();
		
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
					parent.removeAllViews();
					LayoutInflater inflater = (LayoutInflater)getBaseContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					
			        for(int i = 0; i < jArray.length(); i++) {
			            JSONObject p = (JSONObject) jArray.get(i);
			            if(p.getInt("survey_enabled")==1){
				            child = (LinearLayout) inflater.inflate(R.layout.survey, null);
				            TextView txt = (TextView) child.findViewById(R.id.survey_name);
				            txt.setText(p.getString("survey_name"));
				            TextView btn = (TextView) child.findViewById(R.id.txtHealthInfo1);
				            btn.setTag(p.getInt("id"));
				            switch(p.getInt("survey_taken")){
				            	case 0:
				            		btn.setText("View Survey".toString());
				            		btn.setOnClickListener(new OnClickListener(){
						            	public void onClick(View v){
						            		survey = (Integer) v.getTag();						     			            	
						            		fillSurvey(v);
						            		populateAnswers(v);
						            	}
						            });
				            		break;
				            	case 1:
				            		btn.setText("View Results".toString());
				            		btn.setOnClickListener(new OnClickListener(){
						            	public void onClick(View v){
						            		survey = (Integer) v.getTag();
						            		getSurveyResults(v);			            	
						            		fillSurvey(v);
						            		populateAnswers(v);
						            	}
						            });
				            		break;
				            }
				            child.setTag(p.getInt("id"));
				            parent.addView(child);
					    }
			        }
				
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "b": //Populate survey questions

				try {
					
					jArray = new JSONArray(output);
					parent = (LinearLayout) findViewById(R.id.surveyQuestions);
					LayoutInflater inflater = (LayoutInflater)getBaseContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					JSONObject p = (JSONObject) jArray.get(0);
					
			        for(int i = 0; i < jArray.length(); i++) {
			            p = (JSONObject) jArray.get(i);
			            child = (LinearLayout) inflater.inflate(R.layout.survey_question, null);
			            TextView txt = (TextView) child.findViewById(R.id.questionText);
			            txt.setText(p.getString("surveyQuestion_text"));
			            Spinner dropdown = (Spinner) child.findViewById(R.id.answerDropdown);
			            ArrayList<String> spinnerArray = new ArrayList<String>();
			            spinnerArray.add("(select answer)");
			            List<String> answers = Arrays.asList(p.getString("surveyQuestion_choices").split("\\s*,\\s*"));
			            spinnerArray.addAll(answers);			            
			            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
			            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			            dropdown.setAdapter(spinnerArrayAdapter);
			            if(p.getString("surveyQuestion_defaultChoice") != null && !p.getString("surveyQuestion_defaultChoice").isEmpty()){
			            	dropdown.setSelection(spinnerArrayAdapter.getPosition(p.getString("surveyQuestion_defaultChoice")));			            	
			            	}
			            dropdown.setOnItemSelectedListener(new SpinnerItemSelectedListener(p,(Integer) MainActivity.user.getTag()));
			            child.setTag(p.getInt("id"));
			            parent.addView(child);
			        }
			        TextView t = new TextView(this);
					t.setGravity(Gravity.CENTER);
					t.setTextSize(30f);
					t.setText("Get Results");
					t.setTag(survey);
					t.setBackgroundColor(3);
					t.setBackgroundResource(R.drawable.yellowbutton);
					t.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							getSurveyResults(v);			
							populateSurveyList();
						}
					});
					parent.addView(t);
			       
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "c": //Populate survey results
				if(output != "null\t\t"){
					TextView t = new TextView(this);
					TextView s = new TextView(this);
					s.setTextSize(30f);
					s.setText("SURVEY RESULTS:");
					t.setTextSize(18f);
					t.setText(android.text.Html.fromHtml(output));
					LinearLayout surveyResults = (LinearLayout) findViewById(R.id.surveyQuestions);
					surveyResults.removeAllViews();
					surveyResults.addView(s);
					surveyResults.addView(t);					
					//sendDataToAppServer sendSurveyResults = new sendDataToAppServer();
					//sendSurveyResults.execute("e","SaveSurveyResults?surveyId="+survey+"&userId="+MainActivity.user.getTag());
				}
				break;	
			case "d": //Populate survey questions
		
				try {
					
					jArray = new JSONArray(output);
					parent = (LinearLayout) findViewById(R.id.surveyQuestions);					
					
			        for(int i = 0; i < jArray.length(); i++) {
			            JSONObject p = (JSONObject) jArray.get(i);
			            LinearLayout childDropdown = (LinearLayout) parent.findViewWithTag(p.getInt("surveyQuestion_id"));
			            for (int x =0 ;x <childDropdown.getChildCount(); x++){
			                if(childDropdown.getChildAt(x) instanceof Spinner){
			                        Spinner spinner = (Spinner) childDropdown.getChildAt(x);
			                        spinner.setSelection(getIndex(spinner, p.getString("surveyAnswer_text")));
			                }
			            }
			            
			        }
				
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}
	}

	public void fillSurvey(View v) {
		
		fetchDataFromAppServer fetchSurveyQuestions = new fetchDataFromAppServer();
		fetchSurveyQuestions.delegate = this;
		fetchSurveyQuestions.execute("b","GetSurveyQuestions?surveyId="+v.getTag()+"&userId="+MainActivity.user.getTag());
	
	}
	
	public void populateAnswers(View v) {
		
		fetchDataFromAppServer fetchSurveyAnswers = new fetchDataFromAppServer();
		fetchSurveyAnswers.delegate = this;
		fetchSurveyAnswers.execute("d","GetSurveyAnswers?surveyId="+v.getTag()+"&userId="+MainActivity.user.getTag());
		
	}
	
	public void populateSurveyList() {
		fetchDataFromAppServer fetchSurveyNames = new fetchDataFromAppServer();
		fetchSurveyNames.delegate = this;
	    fetchSurveyNames.execute("a","GetAllSurveys?userId="+MainActivity.user.getTag());
	}
	
	public void getSurveyResults(View v) {
		
		fetchDataFromAppServer fetchSurveyResults = new fetchDataFromAppServer();
		fetchSurveyResults.delegate = this;
		fetchSurveyResults.execute("c","GetSurveyResults?surveyId="+v.getTag()+"&userId="+MainActivity.user.getTag());
		
	}
	
	public void setSurvey(View v){
		
	}
	
	private int getIndex(Spinner spinner, String myString)
    {
     int index = 0;

     for (int i=0;i<spinner.getCount();i++){
      if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
       index = i;
       break;
      }
     }
     return index;
    } 
	
}
