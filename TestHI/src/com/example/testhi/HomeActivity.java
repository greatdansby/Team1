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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class HomeActivity extends Activity implements myInterface {
	
	fetchDataFromAppServer fetchLogin = new fetchDataFromAppServer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

	}

	protected void onStart(){
		super.onStart();
		TextView t = (TextView) MainActivity.user;
		t.setTag(1);
		t.setText("Current user: ");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void login(View view){
		fetchLogin.delegate = this;
		
		EditText email = (EditText) this.findViewById(R.id.editText1);
		EditText pwd = (EditText) this.findViewById(R.id.editText2);
		
        fetchLogin.execute("a","Login?userEmail="+email.getText()+"&userPassword="+pwd.getText());
	}
	
	public void processFinish(String output, String id) {

		JSONArray jArray;
		
		switch(id) {
			case "a": //Receive login confirmation
						
				try {
					
					//jArray = new JSONArray(output);
					JSONObject p = new JSONObject(output);
								        
					TextView t = (TextView) MainActivity.user;
					t.setTag(p.getInt("id"));
					t.setText("Current user: "+p.getString("user_emailAddress"));
					LinearLayout l = (LinearLayout) this.findViewById(R.id.loginSection);
					l.setVisibility(View.INVISIBLE);
				
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
		}
	}

}
