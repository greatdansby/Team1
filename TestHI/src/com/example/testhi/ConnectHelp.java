package com.example.testhi;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class ConnectHelp extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.connect_help);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		
		getWindow().setLayout((int)(width * 0.8), (int)(height * 0.8));		
	}
}
