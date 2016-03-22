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
	public void connectHealthData(View view){
		Intent intent = new Intent(this, ConnectActivity.class);
		
	  Bundle b = new Bundle();

	  b.putString("client_id", "659e9bd58ec4ee7fa01bc6b4627cb37e5c13ec21");
	  b.putString("auth_url", "http://10.0.2.2:3000/sessionToken");
	  b.putString("client_user_id", "test_user21@gmail.com");
	  b.putString("language", "en");

	  //PublicToken (mandatory for existing users)
	  //b.putString("public_token", "e56fa0350866bcf266da442cb974d84e");
	  intent.putExtras(b);
	  
	  startActivityForResult(intent, HUMANAPI_AUTH);
	  
	}
}
