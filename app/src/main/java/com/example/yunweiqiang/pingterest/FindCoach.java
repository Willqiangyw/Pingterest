package com.example.yunweiqiang.pingterest;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Locale;


/**
 * Created by jcheng.
 */

public class FindCoach extends AppCompatActivity {

    private String gender;
    private String level;
    private String longitude;
    private String latitude;
    private String zip,age,rating;

    private EditText coachZip, coachAge, coachRating;
    private ArrayList<String> coachName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_coach);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearchCoach);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Spinner mLevelSpinner = (Spinner) findViewById(R.id.find_coach_9);
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this,
                R.array.skill_level_num, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevelSpinner.setAdapter(levelAdapter);
        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    level = "";
                else
                    level = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getBaseContext(),gender,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                level = "";
//                Toast.makeText(getBaseContext(),gender + "nothing",Toast.LENGTH_LONG).show();
            }
        });

        Spinner mGenderSpinner = (Spinner) findViewById(R.id.find_coach_3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(adapter);
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    gender = "";
                else
                    gender = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getBaseContext(),gender,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = "";
//                Toast.makeText(getBaseContext(),gender + "nothing",Toast.LENGTH_LONG).show();
            }
        });

        coachAge = findViewById(R.id.find_coach_5);
        coachRating = findViewById(R.id.find_coach_11);
        coachZip = findViewById(R.id.find_coach_7);

        Button button = findViewById(R.id.find_coach_14);
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

    public void confirm(View view)  throws IOException {
        zip = coachZip.getText().toString();
        age = coachAge.getText().toString();
        rating = coachRating.getText().toString();
        //calculate longitude and altitude
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(zip, 1);
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            // Use the address as needed
            longitude = String.valueOf(address.getLongitude());
            latitude = String.valueOf(address.getLatitude());
        } else {
            // Display appropriate message when Geocoder services are not available
            Toast.makeText(FindCoach.this, "please enter correct zip code", Toast.LENGTH_LONG).show();
            longitude = "";
            latitude = "";
        }
        if(TextUtils.isEmpty(age)&&TextUtils.isEmpty(rating)&&TextUtils.isEmpty(level)&&TextUtils.isEmpty(gender)
                &&TextUtils.isEmpty(longitude)&&TextUtils.isEmpty(latitude)){
            Toast.makeText(this,"Please choose or fill at least one field",Toast.LENGTH_SHORT).show();
        }
        else {
            final String urlString = "https://us-central1-pingterest-ffca7.cloudfunctions.net/findCoach?targetAge="
                    + age
                    + "&userLong="
                    + longitude
                    + "&userLat="
                    + latitude
                    + "&targetRating="
                    + rating
                    + "&targetLevel="
                    + level
                    + "&userGender="
                    + gender;

            // List<String> coachList = new ArrayList<>();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        for (int i = 0; i < coachName.size(); i++) {
//            Toast.makeText(FindCoach.this, coachName.toString(), Toast.LENGTH_LONG).show();
//        }

//        Intent intent = new Intent(FindCoach.this, findplayer2.class);
//        intent.putStringArrayListExtra("name",coachName);
//        startActivity(intent);
            Intent returnIntent = new Intent();
            returnIntent.putStringArrayListExtra("result", coachName);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
