package com.example.yunweiqiang.pingterest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    private EditText mEventKey;
    private EditText mEventTime;
    private EditText mEventLocation;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    private String userKey;
    private DatabaseReference mDatabase;

    public int hour, minute, year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        userEmail = user.getEmail();
        assert userEmail != null;
        userKey = userEmail.substring(0,userEmail.indexOf('@'));

        mDatabase = FirebaseDatabase.getInstance().getReference("Events");

        mEventKey = (EditText) findViewById(R.id.editTextAddEventKey);
        mEventTime = (EditText) findViewById(R.id.editTextAddEventTime);
        mEventLocation = (EditText) findViewById(R.id.editTextAddEventLocation);
    }

    public void submit(View view){
        String key = mEventKey.getText().toString();
        String time = mEventTime.getText().toString();
        String location = mEventLocation.getText().toString();

        if(TextUtils.isEmpty(key) || TextUtils.isEmpty(time) || TextUtils.isEmpty(location)){
            Toast.makeText(this,"please fill in all blanks", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference tempDatabase = mDatabase.child(key);
            tempDatabase.child("key").setValue(key);
            tempDatabase.child("holder").setValue(userKey);
            tempDatabase.child("time").setValue(time);
            tempDatabase.child("location").setValue(location);
            tempDatabase.child("participant").setValue("");
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
        // Create a new instance of TimePickerDialog and return it

    }

//    public void pickPlace(View v){
//        int PLACE_PICKER_REQUEST = 1;
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        Toast.makeText(this,year+","+month+","+day+","+this.hour+", "+this.minute, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        TimePickerDialog t = new TimePickerDialog(this, this, hour, minute,
                DateFormat.is24HourFormat(AddEventActivity.this));
        t.show();
    }
}
