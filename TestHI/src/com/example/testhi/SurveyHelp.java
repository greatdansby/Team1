package com.example.testhi;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class SurveyHelp extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.survey_help);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		
		getWindow().setLayout((int)(width * 0.8), (int)(height * 0.8));
		
		// get our html content
		String htmlAsString = getString(R.string.survey_help_html);
		Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView

		// set the html content on the TextView
		TextView textView = (TextView) findViewById(R.id.txtSurveyHelpMsg);
		textView.setText(htmlAsSpanned);
	}
}
