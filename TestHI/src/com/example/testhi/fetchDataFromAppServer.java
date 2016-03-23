package com.example.testhi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class fetchDataFromAppServer extends AsyncTask<String, Void, String>{
	
	public myInterface delegate = null;
	Integer i = 0;
	
	@Override
	protected void onPostExecute(String v) {
		
		delegate.processFinish(v.substring(1),(String) v.subSequence(0, 1));
		
	}
	String result = "";
	
	protected String doInBackground(String... params) {
		
		try{
            String link = "http://192.185.170.105/~fhir/"+(String)params[1];
            
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
           
            BufferedReader in = new BufferedReader
           (new InputStreamReader(response.getEntity().getContent()));

           StringBuffer sb = new StringBuffer("");
           String line="";
           while ((line = in.readLine()) != null) {
              sb.append(line);
              break;
            }
            in.close();
            
            result = sb.toString();
            return (String)params[0] + result;
		}
		catch(Exception e){
			return result;
		}
		
	}
}
