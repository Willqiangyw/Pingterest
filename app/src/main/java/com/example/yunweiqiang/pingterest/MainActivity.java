package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    public void signIn (View view){
        //FOR SignIn Button, click and open another activity for signing in
        Intent intent = new Intent (this, LOGINPAGE.class);
        startActivity(intent);
    }

    public void signUp (View view){
        //FOR SignIn Button, click and open another activity for signing in
        Intent intent = new Intent (this, SignUpPage.class);
        startActivity(intent);
    }

    public void sampleButton (View view){
        //FOR SignIn Button, click and open another activity for signing in
        Intent intent = new Intent (this, Main2Activity.class);
        startActivity(intent);
    }

    public void checkStatus (View view){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user!=null){
            userEmail = user.getEmail().toString();
            Toast.makeText(this, "Hey " + userEmail, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, FirstPage.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Please sign in", Toast.LENGTH_LONG).show();
        }
    }

    public void signOut (View view){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user!=null) {
            mAuth.signOut();
            Toast.makeText(this, "Successfully signed out", Toast.LENGTH_LONG).show();
            //refresh this activity
//            finish();
//            startActivity(getIntent());
        }
        else{
            Toast.makeText(this, "You didn't sign in", Toast.LENGTH_LONG).show();
        }
    }

    public void chat (View view){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user!=null) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("key", "test");
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "You didn't sign in", Toast.LENGTH_LONG).show();
        }
    }
}
