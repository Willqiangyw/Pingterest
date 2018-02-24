package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signIn (View view){
        //FOR SignIn Button, click and open another activity for signing in
        Intent intent = new Intent (this, LOGINPAGE.class);
        startActivity(intent);
    }

}
