package com.example.testhi;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	
	static TextView user; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    getActionBar().setTitle("WaitingRoom App");
	    
	    user = (TextView) findViewById(R.id.userLogin);
	    
	    TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
	    tabHost.setup();

	    TabSpec tab0 = tabHost.newTabSpec("Home");
	    TabSpec tab1 = tabHost.newTabSpec("My Records & Stats");
	    TabSpec tab2 = tabHost.newTabSpec("Surveys & Quizzes");
	    TabSpec tab3 = tabHost.newTabSpec("Third Party Connections");

	    tab0.setIndicator("Home");
	    tab0.setContent(new Intent(this,HomeActivity.class));
		
	    
	    tab1.setIndicator("My Records & Stats");
	    tab1.setContent(new Intent(this,ReviewEhrActivity.class));
		
	    tab2.setIndicator("Surveys & Quizzes");
	    tab2.setContent(new Intent(this,SurveyActivity.class));
		
	    tab3.setIndicator("Third Party Connections");	    
		Intent intent = new Intent(this, ConnectActivity.class);
		
		  Bundle b = new Bundle();

		  b.putString("client_id", "532188568c08e0c786e821746d15edb240096d50");
		  //b.putString("auth_url", "http://10.0.2.2:3000/sessionToken");
		  //b.putString("client_user_id", "1");
		  //b.putString("language", "en");

		  //PublicToken (mandatory for existing users)
		  //b.putString("public_token", "e56fa0350866bcf266da442cb974d84e");
		  intent.putExtras(b);
		  
	    tab3.setContent(intent);

	    tabHost.addTab(tab0);
	    tabHost.addTab(tab1);
	    tabHost.addTab(tab2);
	    tabHost.addTab(tab3);
	    
	}
	
	public void callForHelp(View v){
		
	}

}
