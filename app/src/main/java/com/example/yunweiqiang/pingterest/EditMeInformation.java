package com.example.yunweiqiang.pingterest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditMeInformation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;

    private TextView mUserEmailTextView;
    private EditText mUserNameEditText;
    private EditText mUserAgeEditText;
    private EditText mUserGenderEditText;
    private EditText mUserLevelEditText;
    private EditText mUserCityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_me_information);

        mAuth = FirebaseAuth.getInstance();

        mUserEmailTextView = findViewById(R.id.textViewUserEmailEdit);
        user = mAuth.getCurrentUser();

        mUserNameEditText = (EditText) findViewById(R.id.editTextNameEdit);
        mUserAgeEditText = (EditText) findViewById(R.id.editTextAgeEdit);
        mUserGenderEditText = (EditText) findViewById(R.id.editTextGenderEdit);
        mUserLevelEditText = (EditText) findViewById(R.id.editTextLevelEdit);
        mUserCityEditText = (EditText) findViewById(R.id.editTextCityEdit);

        if(user!=null) {
            userEmail = user.getEmail();
            mUserEmailTextView.setText("Hey, "+userEmail);
            userKey = userEmail.substring(0, userEmail.indexOf('@'));
            mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userKey);
        }
        else{
            Toast.makeText(this, "no user", Toast.LENGTH_LONG).show();
        }
    }

    public void confirm(View view){
        if(user==null){
            Toast.makeText(this, "please sign in and try again", Toast.LENGTH_LONG).show();
            return;
        }

        String name = mUserNameEditText.getText().toString();
        String age = mUserAgeEditText.getText().toString();
        String gender = mUserGenderEditText.getText().toString();
        String level = mUserLevelEditText.getText().toString();
        String city = mUserCityEditText.getText().toString();

        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(age)||TextUtils.isEmpty(gender)||TextUtils.isEmpty(level)||TextUtils.isEmpty(city)){
            Toast.makeText(EditMeInformation.this, "please fill in all blanks", Toast.LENGTH_LONG).show();
        }
        else{
            mDatabase.child("Name").setValue(name);
            mDatabase.child("Age").setValue(age);
            mDatabase.child("Gender").setValue(gender);
            mDatabase.child("Level").setValue(level);
            mDatabase.child("City").setValue(city);
            Toast.makeText(EditMeInformation.this, "Successfully submit", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
