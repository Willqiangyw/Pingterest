package com.example.yunweiqiang.pingterest;

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

import org.w3c.dom.Text;

public class SignUpPage extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordCheckEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = (EditText) findViewById(R.id.editTextEmailSignUp);
        mPasswordEditText = (EditText) findViewById(R.id.editTextPasswordSignUp);
        mPasswordCheckEditText = (EditText) findViewById(R.id.editTextPasswordSignUpCheck);
    }

    //method for submit button, click and submit email & password & passwordCheck
    public void submit(View view){
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String passwordCheck = mPasswordCheckEditText.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordCheck)){

            Toast.makeText(this, "Please fill in all blanks", Toast.LENGTH_LONG).show();

        }

        else if(!password.equals(passwordCheck)){
            Toast.makeText(this, "Password not match", Toast.LENGTH_LONG).show();
        }

        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(SignUpPage.this, "Congratulation, you have successfully signed up", Toast.LENGTH_LONG).show();

                            }

                            else{
                                Toast.makeText(SignUpPage.this, "Signing up fails, please try again later", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
