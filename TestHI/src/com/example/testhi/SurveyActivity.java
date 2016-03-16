package com.example.testhi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
        fetchSurveyNames.execute("GetAllSurveys/");
        
	}

	@Override
	public void processFinish(String output, Integer id) {
		JSONObject e;
		JSONArray jArray;
		LinearLayout ll;
		try {
			
			e = new JSONObject(output);
			jArray = e.getJSONArray("surveys");
			ll = (LinearLayout) findViewById(R.id.allSurveys);

	        for(int i = 0; i < jArray.length(); i++) {
	            TextView txt = new TextView(this);
	            JSONObject p = (JSONObject) jArray.get(i);
	            txt.setText(p.getString("survey_name"));
	            ll.addView(txt);
	            int idx = ll.indexOfChild(txt);
	            txt.setTag(Integer.toString(idx));
	        }
			
			TextView t = new TextView(this); 
		    t=(TextView)findViewById(R.id.TextView07); 
		    
			t.setText(e.getString("survey_name"));
		
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	public void setSurvey(){
		Log.d("info","Setting Survey");
		
	}
		
}
