package com.sean.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;



public class GsonExample extends Activity {

    private static final String url = "http://search.twitter.com/search.json?q=javacodegeeks";
    protected InitTask _initTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)findViewById(R.id.go);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _initTask = new InitTask();
                _initTask.execute( getApplicationContext() );
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        _initTask.cancel(true);
    }

    protected class InitTask extends AsyncTask<Context, String, SearchResponse>
    {
        @Override
        protected SearchResponse doInBackground( Context... params ) 
        {
            InputStream source = retrieveStream(url);
            SearchResponse response = null;
            if (source != null) {
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(source);
                try {
                    response = gson.fromJson(reader, SearchResponse.class);
                    publishProgress( response.query );
                } catch (JsonSyntaxException e) {
                    Log.w(getClass().getSimpleName(), "Error: " + e.getMessage() + " for URL " + url); 
                    return null;
                } catch (JsonIOException e) {
                    Log.w(getClass().getSimpleName(), "Error: " + e.getMessage() + " for URL " + url);
                    return null;
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.w(getClass().getSimpleName(), "Error: " + e.getMessage() + " for URL " + url);
                    }
                }
            }
            if (!this.isCancelled()) {
                return response;
            } else {
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... s) 
        {
            super.onProgressUpdate(s);
            Toast.makeText(getApplicationContext(), s[0], Toast.LENGTH_SHORT).show();
            ImageView iv = (ImageView) findViewById(R.id.progressBar);
            iv.setImageResource(R.drawable.progress);
        }

        @Override
        protected void onPostExecute( SearchResponse response ) 
        {
        	ImageView iv = (ImageView) findViewById(R.id.progressBar);
            iv.setImageResource(R.drawable.ic_launcher);
            
        	super.onPostExecute(response);
            StringBuilder builder = new StringBuilder();
            if (response != null) {
                String delim = "* ";
                List<Result> results = response.results;
                for (Result result : results) {
                    builder.append(delim).append(result.fromUser);
                    delim="\n* ";
                }
            }
            if (builder.length() > 0) {
                Toast.makeText(getApplicationContext(), builder.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "The response was empty.", Toast.LENGTH_SHORT).show();
            }
            
        }
        
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getApplicationContext(), "The operation was cancelled.", 1).show();
        }

        private InputStream retrieveStream(String url) {
            DefaultHttpClient client = new DefaultHttpClient(); 
            HttpGet getRequest;
            try {
                getRequest = new HttpGet(url);
                try {
                    HttpResponse getResponse = client.execute(getRequest);
                    final int statusCode = getResponse.getStatusLine().getStatusCode();
                    if (statusCode != HttpStatus.SC_OK) { 
                        Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
                        return null;
                    }
                    HttpEntity getResponseEntity = getResponse.getEntity();
                    try {
                        return getResponseEntity.getContent();
                    } catch (IllegalStateException e) {
                        getRequest.abort();
                        Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
                        return null;
                    } catch (IOException e) {
                        getRequest.abort();
                        Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
                        return null;
                    }
                } catch (ClientProtocolException e) {
                    getRequest.abort();
                    Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
                } catch (IOException e) {
                    getRequest.abort();
                    Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
                }
            } catch (IllegalArgumentException e) {
                Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
            }
            return null;
        }

    }

}