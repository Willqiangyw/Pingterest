package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class checkOthersInformation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView mUserNameTextView;
    private TextView mUserGenderandAgeTextView;
    private TextView mUserScoreTextView;
    private TextView mUserLevelTextView;
    private TextView mUserAddrTextView;
    private TextView mUserDescTextView;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;

    private String otherKey;
    //information for opponent

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_others_information);

        Intent in = getIntent();
        otherKey = in.getStringExtra("key");
        Toast.makeText(this,otherKey,Toast.LENGTH_LONG).show();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOther);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
        mUserGenderandAgeTextView = findViewById(R.id.textViewOtherGenderAndAge);
        mUserScoreTextView = findViewById(R.id.textViewOtherScore);
        mUserAddrTextView = findViewById(R.id.textViewOtherAddr);
        mUserLevelTextView = findViewById(R.id.textViewOtherLevel2);
        mUserDescTextView = findViewById(R.id.textViewOtherDesc2);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map =  (Map) dataSnapshot.getValue();
//                Log.d("E_Value", "get from data! " + dataSnapshot.getValue());
                userName = map.get("name");
                String userAge = map.get("age");
                String userCity = map.get("city");
                String userState = "";
                String userZip = "";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    userState = map.getOrDefault("state", "");
                    userZip = map.getOrDefault("zip", "");
                }
                String userLevel = map.get("level");
                String userGender = map.get("gender");
                //add later
                String userScore = map.get("rate");
                String userDesc = map.get("description");

                mUserGenderandAgeTextView.setText(userGender + ", " + userAge);
                mUserAddrTextView.setText(userCity + "," + userState + "," + userZip);
                mUserNameTextView.setText(userName);
                mUserLevelTextView.setText(userLevel);
                mUserScoreTextView.setText(userScore);
                mUserDescTextView.setText(userDesc);

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
        intent.putExtra("otherkey",userName);
        startActivity(intent);
    }

    public void rate(View view){
        Intent intent = new Intent(this, RatingActivity.class);
        intent.putExtra("otherkey", otherKey);
        intent.putExtra("userKey", userKey);
        intent.putExtra("otherName", userName);
        startActivity(intent);
    }
}
