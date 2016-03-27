package com.example.testhi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class ThirdpartyActivity extends Activity {
	static final int HUMANAPI_AUTH = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thirdparty_activity);
	}
	protected void onStart(){
		super.onStart();
		Intent intent = new Intent(this, ConnectActivity.class);
		
	  Bundle b = new Bundle();

	  b.putString("client_id", "532188568c08e0c786e821746d15edb240096d50");
	  //b.putString("auth_url", "http://10.0.2.2:3000/sessionToken");
	  //b.putString("client_user_id", "1");
	  //b.putString("language", "en");

	  //PublicToken (mandatory for existing users)
	  //b.putString("public_token", "e56fa0350866bcf266da442cb974d84e");
	  intent.putExtras(b);
	  
	  startActivityForResult(intent, HUMANAPI_AUTH);
	  
	}
}
