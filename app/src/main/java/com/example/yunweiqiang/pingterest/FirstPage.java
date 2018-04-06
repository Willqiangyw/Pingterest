package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
    }

    public void me(View view){
        Intent intent = new Intent(this, Me.class);
        startActivity(intent);
    }

    public void findPlayer(View view){
        Intent intent = new Intent(this, FindPlyer.class);
        startActivity(intent);
    }

    public void findCoach(View view){
        Intent intent = new Intent(this, FindCoach.class);
        startActivity(intent);
    }

    public void Equipment(View view){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
