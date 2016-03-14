package com.example.testhi;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

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
	    tab3.setContent(new Intent(this,ThirdpartyActivity.class));

	    tabHost.addTab(tab0);
	    tabHost.addTab(tab1);
	    tabHost.addTab(tab2);
	    tabHost.addTab(tab3);
	}

}
