package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Me extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView mUserEmailTextView;
    private TextView mUserNameTextView;
    private TextView mUserAgeTextView;
    private TextView mUserGenderTextView;
    private TextView mUserLevelTextView;
    private TextView mUserCityTextView;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;
//    private Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference("message");
//        mDatabase.setValue("Hello, World!");

        mUserEmailTextView = findViewById(R.id.textViewUserEmail);
        user = mAuth.getCurrentUser();

        if(user!=null) {
            userEmail = user.getEmail();
            mUserEmailTextView.setText("Hey, "+userEmail);
        }
        else{
            Toast.makeText(this, "no user", Toast.LENGTH_LONG).show();
        }

        userKey = userEmail.substring(0,userEmail.indexOf('@'));
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userKey);

        mUserNameTextView = findViewById(R.id.textViewUserName);
        mUserAgeTextView = findViewById(R.id.textViewUserAge);
        mUserCityTextView = findViewById(R.id.textViewUserCity);
        mUserLevelTextView = findViewById(R.id.textViewUserLevel);
        mUserGenderTextView = findViewById(R.id.textViewUserGender);
//        mUserNameTextView.setText(userKey);
        Log.d("E_VALUE", "The key is " + userKey);
//        mDatabase.child("Users").child(userKey).getRef("Key");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map =  (Map) dataSnapshot.getValue();
//                Log.d("E_Value", "get from data! " + dataSnapshot.getValue());
                String userName = map.get("Name");
                String userAge = map.get("Age");
                String userCity = map.get("City");
                String userLevel = map.get("Level");
                String userGender = map.get("Gender");

                mUserAgeTextView.setText(userAge);
                mUserCityTextView.setText(userCity);
                mUserNameTextView.setText(userName);
                mUserLevelTextView.setText(userLevel);
                mUserGenderTextView.setText(userGender);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("E_Value", "something went wrong", databaseError.toException());
            }
        });
    }

    public void edit(View view){
        Intent intent = new Intent(this, EditMeInformation.class);
        startActivity(intent);
    }
}
