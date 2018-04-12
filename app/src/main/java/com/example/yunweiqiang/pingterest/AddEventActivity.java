package com.example.yunweiqiang.pingterest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    private EditText mEventName;
    private EditText mEventZip;
    private EditText mEventDesc;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    private String userKey;
    private DatabaseReference mDatabase;

    public int hour, minute, year, month, day;
    private String eventName, eventTime, eventZip, eventTime2, eventDesc, eventLongitude, eventLatitude, eventState, eventCity;
    private Button chooseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddEvent);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        userEmail = user.getEmail();
        assert userEmail != null;
        userKey = userEmail.substring(0,userEmail.indexOf('@'));

        mDatabase = FirebaseDatabase.getInstance().getReference("Events");

        mEventName = (EditText) findViewById(R.id.editTextAddEventName);
        mEventZip = (EditText) findViewById(R.id.editTextAddEventAddr);
        mEventDesc = (EditText) findViewById(R.id.editTextAddEventDesc);
        chooseTime = (Button) findViewById(R.id.buttonChooseTime);
    }

    public void submit(View view) throws IOException{
        eventName = mEventName.getText().toString();
        eventZip = mEventZip.getText().toString();
        eventDesc = mEventDesc.getText().toString();

        //calculate address based on zip code
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(eventZip, 1);
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            // Use the address as needed
            eventCity = address.getLocality();
            eventState = address.getAdminArea();
            eventLongitude = String.valueOf(address.getLongitude());
            eventLatitude = String.valueOf(address.getLatitude());
        } else {
            // Display appropriate message when Geocoder services are not available
            Toast.makeText(this, "please enter correct zip code", Toast.LENGTH_LONG).show();
            eventCity = "";
            eventState = "";
            eventLongitude = "";
            eventLatitude = "";
        }

        if(TextUtils.isEmpty(eventName) || TextUtils.isEmpty(eventZip) || TextUtils.isEmpty(eventTime)  || TextUtils.isEmpty(eventDesc)){
            Toast.makeText(this,"please fill in all blanks", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference tempDatabase = mDatabase.child(eventName);
            tempDatabase.child("key").setValue(eventName);
            tempDatabase.child("holder").setValue(Main2Activity.CURRENT_USER_NAME);
            tempDatabase.child("time").setValue(eventTime);
            tempDatabase.child("time2").setValue(eventTime2);
            tempDatabase.child("location").setValue(eventCity +","+eventState);
            tempDatabase.child("participant").setValue("");
            tempDatabase.child("longitude").setValue(eventLongitude);
            tempDatabase.child("latitude").setValue(eventLatitude);
            tempDatabase.child("description").setValue(eventDesc);
            Toast.makeText(this,"post success", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void showTimePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog d = new DatePickerDialog(AddEventActivity.this, this, year, month, day);
        d.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        Toast.makeText(this,year+","+month+","+day+","+this.hour+", "+this.minute, Toast.LENGTH_LONG).show();
        eventTime = day+"/"+month+"/"+year+"   "+this.hour+":"+this.minute;
        eventTime2 =  day+"/"+month+"/"+year+"/"+this.hour+"/"+this.minute;
        chooseTime.setText(eventTime);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month+1;
        this.day = dayOfMonth;
        TimePickerDialog t = new TimePickerDialog(this, this, hour, minute,
                DateFormat.is24HourFormat(AddEventActivity.this));
        t.show();
    }
}
