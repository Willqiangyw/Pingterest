package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LOGINPAGE extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = (EditText) findViewById(R.id.editTextEmailSignUp);
        mPasswordEditText = (EditText) findViewById(R.id.editTextPasswordSignUp);
    }

    //method for submit button, click and submit email & password
    public void submit(View view){
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please fill in email and password", Toast.LENGTH_LONG).show();
        }

        else{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                assert user != null;
                                String userEmail = user.getEmail();
                                Toast.makeText(LOGINPAGE.this, "Welcome back! " + userEmail, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LOGINPAGE.this, FirstPage.class);
                                startActivity(intent);
                            }

                            else{
                                Toast.makeText(LOGINPAGE.this, "Wrong user email or password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }
}
