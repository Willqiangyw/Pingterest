package com.example.yunweiqiang.pingterest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class LOGINPAGE extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = (EditText) findViewById(R.id.editTextEmailSignIn);
        mPasswordEditText = (EditText) findViewById(R.id.editTextPasswordSignIn);
    }

    //method for submit button, click and submit email & password
    public void submit(View view){
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        
    }
}
