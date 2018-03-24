package com.example.yunweiqiang.pingterest;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView mUserEmailTextView;
    private EditText mUserNameEditText;
    private EditText mUserAgeEditText;
//    private EditText mUserGenderEditText;
//    private EditText mUserLevelEditText;
//    private EditText mUserCityEditText;
    private EditText mUserZipEditText;

    private String name;
    private String age;
    private String gender;
    private String level;
    private String city;
    private String state;
    private String zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_me_information);

        mAuth = FirebaseAuth.getInstance();

        mUserEmailTextView = findViewById(R.id.textViewUserEmailEdit);
        user = mAuth.getCurrentUser();

        mUserNameEditText = (EditText) findViewById(R.id.editTextNameEdit);
        mUserAgeEditText = (EditText) findViewById(R.id.editTextAgeEdit);
//        mUserGenderEditText = (EditText) findViewById(R.id.editTextGenderEdit);
//        mUserLevelEditText = (EditText) findViewById(R.id.editTextLevelEdit);
//        mUserCityEditText = (EditText) findViewById(R.id.editTextCityEdit);
        mUserZipEditText = (EditText) findViewById(R.id.editTextZipCode);

        //use spinner to replace old version of gender
        Spinner mGenderSpinner = (Spinner) findViewById(R.id.GenderSpinner);
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
        Spinner mLevelSpinner = (Spinner) findViewById(R.id.spinnerSkillLevel);
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this,
                R.array.skill_level, android.R.layout.simple_spinner_item);
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
            mUserEmailTextView.setText("Hey, " + userEmail);
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

        name = mUserNameEditText.getText().toString();
        age = mUserAgeEditText.getText().toString();
//        String gender = mUserGenderEditText.getText().toString();
//        level = mUserLevelEditText.getText().toString();
//        city = mUserCityEditText.getText().toString();
        zip = mUserZipEditText.getText().toString();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(zip, 1);
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            // Use the address as needed
//            String message = String.format("City: "+ address.getLocality() + ", State: " +
//                   address.getAdminArea());
//            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            city = address.getLocality();
            state = address.getAdminArea();
        } else {
            // Display appropriate message when Geocoder services are not available
            Toast.makeText(this, "please enter correct zip code", Toast.LENGTH_LONG).show();
            city = "";
            state = "";
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(gender)
                || TextUtils.isEmpty(level) || TextUtils.isEmpty(city) || TextUtils.isEmpty(state)) {
            Toast.makeText(EditMeInformation.this, "please fill in correct information", Toast.LENGTH_LONG).show();
        } else {
            mDatabase.child("name").setValue(name);
            mDatabase.child("age").setValue(age);
            mDatabase.child("gender").setValue(gender);
            mDatabase.child("level").setValue(level);
            mDatabase.child("city").setValue(city);
            mDatabase.child("key").setValue(userKey);
            mDatabase.child("zip").setValue(zip);
            mDatabase.child("state").setValue(state);
            Toast.makeText(EditMeInformation.this, "Successfully submit", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
