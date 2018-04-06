package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
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

public class FindCoach extends AppCompatActivity {
    private ArrayList<String> coachName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_coach);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final EditText coachGender = findViewById(R.id.find_coach_3);
        final EditText coachAge = findViewById(R.id.find_coach_5);
        EditText coachZip = findViewById(R.id.find_coach_7);
        final EditText coachSkill = findViewById(R.id.find_coach_9);
        EditText coachPrice = findViewById(R.id.find_coach_11);
        EditText coachTime = findViewById(R.id.find_coach_13);
        Button button = findViewById(R.id.find_coach_14);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String urlString = "https://us-central1-pingterest-ffca7.cloudfunctions.net/findCoach?targetAge="
                        + coachAge.getText().toString()
                        + "&userLong=0&userLat=0&targetRating=1550&targetLevel="
                        + coachSkill.getText().toString();

                // List<String> coachList = new ArrayList<>();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            coachName = new ArrayList<>();
                            JSONObject jsonObject = getJSONObjectFromURL(urlString);
                            JSONArray contacts = jsonObject.getJSONArray("coaches");
                            // List<String> coachList = new ArrayList<>();
                            for (int i = 0; i < contacts.length(); i++) {
                                String s = contacts.getString(i);
                                // coachList.add(s);
                                coachName.add(s);
                            }
                            // Toast.makeText(FindCoach.this, urlString, Toast.LENGTH_LONG).show();
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
                for (int i = 0; i < coachName.size(); i++) {
                    Toast.makeText(FindCoach.this, coachName.get(i), Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(FindCoach.this, findplayer2.class);
                intent.putStringArrayListExtra("name",coachName);
                startActivity(intent);
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
