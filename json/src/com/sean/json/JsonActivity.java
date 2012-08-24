package com.sean.json;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class JsonActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button setButton = (Button)findViewById(R.id.go);
        setButton.setOnClickListener(mAddListener);
    	
    }
    
    private OnClickListener mAddListener = new OnClickListener(){
    	public void onClick(View v){
    		switch(v.getId()){
	    		case R.id.go:
	    			startAct();
	    			/*getAjaxRequest();
	    			
	    			int duration = Toast.LENGTH_LONG;
	    			Context context = getApplicationContext();
	    			String text = "You have clicked on the butotn";
	    			Toast toast = Toast.makeText(context, text, duration);
    				toast.show();*/
	    			break;
    		}
    	}
    };
    
    private void startAct(){
		Intent launchAct = new Intent(this, GsonExample.class);
		startActivity(launchAct);
		
	}
    
    private void getAjaxRequest(){
    	/*int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
    	HttpParams httpParams = new BasicHttpParams();
    	HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
    	HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
    	HttpClient client = new DefaultHttpClient(httpParams);
    	String serverUrl = "http://isdev.byu.edu";
    	HttpPost request = new HttpPost(serverUrl);
    	request.setEntity((HttpEntity) new ByteArrayEntity(
    	//postMessage.toString().getBytes("UTF8")));
    	//HttpResponse response = client.execute(request);

*/
    }
    
    
}