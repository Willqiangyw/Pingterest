package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by jcheng.
 */

public class FindEvent extends AppCompatActivity {
    private List<String> eventName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_event);

        final EditText eventZip = findViewById(R.id.find_event_3);
        final EditText eventDist = findViewById(R.id.find_event_5);
        final EditText eventDate = findViewById(R.id.find_event_7);
        final EditText eventTime = findViewById(R.id.find_event_9);
        Button button = findViewById(R.id.find_event_10);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String urlString = "https://us-central1-pingterest-ffca7.cloudfunctions.net/findEvent?userLong=84.3901" +
                        "&userLat=33.7812&targetDist=" + eventDist.getText().toString() +
                        "&targetDate=" + eventDate.getText().toString() +"&targetTime="
                        + eventTime.getText().toString();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            eventName = new ArrayList<>();
                            JSONObject jsonObject = getJSONObjectFromURL(urlString);
                            JSONArray contacts = jsonObject.getJSONArray("Events");
                            for (int i = 0; i < contacts.length(); i++) {
                                String s = contacts.getString(i);
                                // coachList.add(s);
                                eventName.add(s);
                            }
                            
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                for (int i = 0; i < eventName.size(); i++) {
                    Toast.makeText(FindEvent.this, eventName.get(i), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);
        return new JSONObject(jsonString);
    }
}

