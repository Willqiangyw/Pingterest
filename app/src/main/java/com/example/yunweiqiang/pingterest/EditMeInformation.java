package com.example.yunweiqiang.pingterest;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditMeInformation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;

    private EditText mUserFirstNameEditText;
    private EditText mUserAgeEditText;
    private EditText mUserLastNameEditText;
    private EditText mUserZipEditText;
    private EditText mUserDescEditText;
    private EditText mUserRatingEditText;

    private String firstName;
    private String lastName;
    private String age;
    private String gender;
    private String level;
    private String city;
    private String state;
    private String zip;
    private String longitude;
    private String latitude;
    private String description;
    private String rating;
    private String isCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_me_information);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        mUserFirstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
        mUserLastNameEditText = (EditText) findViewById(R.id.editTextLastName);
        mUserAgeEditText = (EditText) findViewById(R.id.editTextAge);
        mUserZipEditText = (EditText) findViewById(R.id.editTextUserZipCode);
        mUserDescEditText = (EditText) findViewById(R.id.editTextUserDesc);
        mUserRatingEditText = (EditText) findViewById(R.id.editTextUserRating);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEditMe);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        isCoach = "no";
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButtonBeCoach);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    isCoach = "yes";
                } else {
                    // The toggle is disabled
                    isCoach = "no";
                }
            }
        });

        //use spinner to replace old version of gender
        Spinner mGenderSpinner = (Spinner) findViewById(R.id.spinnerGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(adapter);
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
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

        //another spinner for level
        Spinner mLevelSpinner = (Spinner) findViewById(R.id.spinnerLevel);
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this,
                R.array.skill_level_num, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevelSpinner.setAdapter(levelAdapter);
        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
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

        if (user != null) {
            userEmail = user.getEmail();
            userKey = userEmail.substring(0, userEmail.indexOf('@'));
            mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userKey);
        } else {
            Toast.makeText(this, "no user", Toast.LENGTH_LONG).show();
        }
    }

    public void confirm(View view) throws IOException {
        if (user == null) {
            Toast.makeText(this, "please sign in and try again", Toast.LENGTH_LONG).show();
            return;
        }

        firstName = mUserFirstNameEditText.getText().toString();
        lastName = mUserLastNameEditText.getText().toString();
        age = mUserAgeEditText.getText().toString();
        zip = mUserZipEditText.getText().toString();
        description = mUserDescEditText.getText().toString();
        rating = mUserRatingEditText.getText().toString();
        //calculate address based on zip code
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(zip, 1);
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            // Use the address as needed
            city = address.getLocality();
            state = address.getAdminArea();
            longitude = String.valueOf(address.getLongitude());
            latitude = String.valueOf(address.getLatitude());
        } else {
            // Display appropriate message when Geocoder services are not available
            Toast.makeText(this, "please enter correct zip code", Toast.LENGTH_LONG).show();
            city = "";
            state = "";
            longitude = "";
            latitude = "";
        }

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(age) || TextUtils.isEmpty(gender)
                || TextUtils.isEmpty(level) || TextUtils.isEmpty(zip)  || TextUtils.isEmpty(city) || TextUtils.isEmpty(state)) {
            Toast.makeText(EditMeInformation.this, "please fill in correct information", Toast.LENGTH_LONG).show();
        } else {
            mDatabase.child("name").setValue(firstName+" "+lastName);
            mDatabase.child("age").setValue(age);
            mDatabase.child("gender").setValue(gender);
            mDatabase.child("level").setValue(level);
            mDatabase.child("city").setValue(city);
            mDatabase.child("key").setValue(userKey);
            mDatabase.child("zip").setValue(zip);
            mDatabase.child("state").setValue(state);
            mDatabase.child("longitude").setValue(longitude);
            mDatabase.child("latitude").setValue(latitude);
            mDatabase.child("description").setValue(description);
            mDatabase.child("rating").setValue(rating);
            mDatabase.child("isCoach").setValue(isCoach);

            Toast.makeText(EditMeInformation.this, "Successfully submit", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
