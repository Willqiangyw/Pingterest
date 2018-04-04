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

import org.w3c.dom.Text;

import java.util.Map;

public class Me extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView mMyNameTextView;
    private TextView mMyGenderAndAgeTextView;
    private TextView mMyScoreTextView;
    private TextView mMyLevelTextView;
    private TextView mMyAddrTextView;
    private TextView mMyDescTextView;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;
//    private Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMe);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mAuth = FirebaseAuth.getInstance();

        mMyNameTextView = (TextView) findViewById(R.id.textViewMyName);
        mMyGenderAndAgeTextView = (TextView) findViewById(R.id.textViewMyGenderAndAge);
        mMyScoreTextView = (TextView) findViewById(R.id.textViewMyScore);
        mMyLevelTextView = (TextView) findViewById(R.id.textViewMyLevel2);
        mMyAddrTextView = (TextView) findViewById(R.id.textViewMyAddr);
        mMyDescTextView = (TextView) findViewById(R.id.textViewMyDesc2);

        user = mAuth.getCurrentUser();

        if(user!=null) {
            userEmail = user.getEmail();
        }
        else{
            Toast.makeText(this, "no user", Toast.LENGTH_LONG).show();
        }
        userKey = userEmail.substring(0,userEmail.indexOf('@'));
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userKey);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map =  (Map) dataSnapshot.getValue();
//                Log.d("E_Value", "get from data! " + dataSnapshot.getValue());
                String userName = map.get("name");
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
//                String userScore = map.get("rate");
//                String userDesc = map.get("description");

                mMyGenderAndAgeTextView.setText(userGender + ", " + userAge);
                mMyAddrTextView.setText(userCity + "," + userState + "," + userZip);
                mMyNameTextView.setText(userName);
                mMyLevelTextView.setText(userLevel);
//                mMyScoreTextView.setText(userScore);
//                mMyDescTextView.setText(userDesc);
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
