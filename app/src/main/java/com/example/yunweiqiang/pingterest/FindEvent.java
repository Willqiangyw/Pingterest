package com.example.yunweiqiang.pingterest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by jcheng.
 */

public class FindEvent extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    private EditText eventZip, eventDist, eventFrame;
    private String zip, dist, time, frame, longitude, latitude;
    private ArrayList<String> searchRes;
    private Button chooseTime;
    public int hour, minute, year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearchEvent);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        eventZip = findViewById(R.id.find_event_3);
        eventDist = findViewById(R.id.find_event_5);
        eventFrame = findViewById(R.id.find_event_9);
        chooseTime = findViewById(R.id.find_event_7);

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

    public void showTimePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog d = new DatePickerDialog(FindEvent.this, this, year, month, day);
        d.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        Toast.makeText(this,year+","+month+","+day+","+this.hour+", "+this.minute, Toast.LENGTH_LONG).show();
        time =  year+"/"+month+"/"+day+"/"+this.hour+"/"+this.minute;
        chooseTime.setText(time);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month+1;
        this.day = dayOfMonth;
        TimePickerDialog t = new TimePickerDialog(this, this, hour, minute,
                DateFormat.is24HourFormat(FindEvent.this));
        t.show();
    }

    public void confirm(View view)  throws IOException {

        zip = eventZip.getText().toString();
        dist = eventDist.getText().toString();
        frame = eventFrame.getText().toString();

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
            Toast.makeText(FindEvent.this, "please enter correct zip code", Toast.LENGTH_LONG).show();
            longitude = "";
            latitude = "";
        }

        if(TextUtils.isEmpty(dist)||TextUtils.isEmpty(time)||TextUtils.isEmpty(frame)
                ||TextUtils.isEmpty(longitude)||TextUtils.isEmpty(latitude)){
            Toast.makeText(this,"Please choose or fill all fields",Toast.LENGTH_SHORT).show();
        }
        else {
            final String urlString = "https://us-central1-pingterest-ffca7.cloudfunctions.net/findEvent?userLong="
                    + longitude
//                    +"84.3901"
                    +"&userLat="
//                    +"33.7812"
                    + latitude
                    + "&targetDist="
                    + dist
//                    +"50"
                    + "&targetDate="
                    + time
//                    +"2018/03/17/11/7"
                    + "&targetTime="
                    + frame
//                    +"72"
                    ;
            Log.d("MyURL", urlString);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        searchRes = new ArrayList<>();
                        JSONObject jsonObject = getJSONObjectFromURL(urlString);
                        JSONArray contacts = jsonObject.getJSONArray("Events");
                        for (int i = 0; i < contacts.length(); i++) {
                            String s = contacts.getString(i);
                            // coachList.add(s);
                            searchRes.add(s);
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < searchRes.size(); i++) {
            Toast.makeText(this, searchRes.toString(), Toast.LENGTH_LONG).show();
        }

            Intent returnIntent = new Intent();
            returnIntent.putStringArrayListExtra("result", searchRes);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}

