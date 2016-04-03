package com.example.testhi;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class SpinnerItemSelectedListener implements OnItemSelectedListener {
	
	JSONObject params=null;
	Integer userId;
	
    public SpinnerItemSelectedListener(JSONObject params, Integer userId) {
         this.params = params;
         this.userId = userId;
    }

  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		Integer surveyId=null;
		Integer surveyQuestionId=null;

		try {
			surveyId = params.getInt("survey_id");
			surveyQuestionId = params.getInt("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	  	sendDataToAppServer sendSurveyAnswers = new sendDataToAppServer();
	  	sendSurveyAnswers.execute("b","SaveSurveyAnswer?surveyId="+surveyId+"&userId="+userId+"&surveyQuestionId="+surveyQuestionId+"&surveyAnswer="+parent.getItemAtPosition(pos).toString());
	
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
  }
  
  public void processFinish(String output, String id) {
  }
}