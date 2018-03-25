package com.example.yunweiqiang.pingterest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEventActivity extends AppCompatActivity {

    private EditText mEventKey;
    private EditText mEventTime;
    private EditText mEventLocation;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    private String userKey;
    private DatabaseReference mDatabase;

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
}
