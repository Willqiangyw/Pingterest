package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private String userKey;
    private String otherKey;
    private TextView mUserNameTextView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Intent in = getIntent();
        otherKey = in.getStringExtra("otherkey");
        userKey = in.getStringExtra("userKey");
        ratingBar  = (RatingBar) findViewById(R.id.ratingBar);
        mUserNameTextView = (TextView) findViewById(R.id.textViewUserName);
        mUserNameTextView.setText(otherKey);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(otherKey).child("rate");
    }

    public void submit (View view){
        float score = ratingBar.getRating();
        Toast.makeText(this, "rating is " + score, Toast.LENGTH_SHORT).show();
        mDatabase.child(userKey).setValue(score);
        finish();
    }
}
