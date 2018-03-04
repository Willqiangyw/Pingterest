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

import java.util.Arrays;
import java.util.Map;

public class checkOthersInformation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView mUserNameTextView;
    private TextView mUserAgeTextView;
    private TextView mUserGenderTextView;
    private TextView mUserLevelTextView;
    private TextView mUserCityTextView;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;

    private String otherKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_others_information);

        Intent in = getIntent();
        otherKey = in.getStringExtra("key");
        Toast.makeText(this,otherKey,Toast.LENGTH_LONG).show();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user!=null) {
            userEmail = user.getEmail();
            userKey = userEmail.substring(0,userEmail.indexOf('@'));
        }
        else{
            Toast.makeText(this, "no user", Toast.LENGTH_LONG).show();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(otherKey);

        mUserNameTextView = findViewById(R.id.textViewOtherName);
        mUserAgeTextView = findViewById(R.id.textViewOtherAge);
        mUserCityTextView = findViewById(R.id.textViewOtherCity);
        mUserLevelTextView = findViewById(R.id.textViewOtherLevel);
        mUserGenderTextView = findViewById(R.id.textViewOtherGender);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map =  (Map) dataSnapshot.getValue();
//                Log.d("E_Value", "get from data! " + dataSnapshot.getValue());
                String userName = map.get("name");
                String userAge = map.get("age");
                String userCity = map.get("city");
                String userLevel = map.get("level");
                String userGender = map.get("gender");

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

    public void chat(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        String[] temp = {userKey, otherKey};
        Arrays.sort(temp);
        String pass = temp[0] +"+"+ temp[1];
        intent.putExtra("key", pass);
        startActivity(intent);
    }
}
