package com.example.yunweiqiang.pingterest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String CURRENT_USER_NAME;
    public static String CURRENT_USER_ADDR;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userEmail;
    private String userKey;
    private String userName;

    Button buttonFindCoach;
    Button buttonFindPlayer;
    Button buttonEquipment;

    TextView myName;
    TextView myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user==null){
            Intent intent = new Intent (this, LOGINPAGE.class);
            startActivity(intent);
            finish();
            return;
        }
        assert user != null;
        userEmail = user.getEmail();
        assert userEmail != null;
        userKey = userEmail.substring(0,userEmail.indexOf('@'));

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View v = navView.getHeaderView(0);
        myName = (TextView) v.findViewById(R.id.textViewUserNameHeader);
        myEmail = (TextView) v.findViewById(R.id.textViewUserEmailHeader);
//        Toast.makeText(this,myName.getText(),Toast.LENGTH_LONG).show();
        setTexts(userKey);
//        myName.setText(getName(userKey));
//        myEmail.setText(userEmail);

        buttonFindCoach = findViewById(R.id.buttonFindCouchMain);
        buttonFindCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, findplayer2.class);
                startActivity(intent);
            }
        });

        buttonFindPlayer = findViewById(R.id.buttonFindPlayerMain);
        buttonFindPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, FindPlyer.class);
                startActivity(intent);
            }
        });

        buttonEquipment = findViewById(R.id.buttonEquipmentMain);
        buttonEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, SearchActivity.class);
                intent.putExtra("searchKey","");
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = getIntent();
            finish();
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_find_coach) {
            Intent intent = new Intent(this, findplayer2.class);
            startActivity(intent);
        } else if (id == R.id.nav_find_player) {
            Intent intent = new Intent(this, FindPlyer.class);
            startActivity(intent);
        } else if (id == R.id.nav_equipments) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("searchKey","");
            startActivity(intent);
        } else if (id == R.id.nav_my_info) {
            Intent intent = new Intent(this, Me.class);
            startActivity(intent);
        } else if (id == R.id.nav_edit_info) {
            Intent intent = new Intent(this, EditMeInformation.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {

            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            if(user!=null) {
                mAuth.signOut();
                Toast.makeText(this, "Successfully signed out", Toast.LENGTH_LONG).show();
                //open login this activity
                finish();
                Intent intent = new Intent(this, LOGINPAGE.class);
                startActivity(intent);
//                startActivity(getIntent());
            }
            else{
                Toast.makeText(this, "You didn't sign in", Toast.LENGTH_LONG).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setTexts(String key){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(key);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map =  (Map) dataSnapshot.getValue();
                CURRENT_USER_NAME = map.get("name");
                CURRENT_USER_ADDR = map.get("city")+", "+map.get("state");
                myName.setText(CURRENT_USER_NAME);
                myEmail.setText(userEmail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //do nothing
            }
        });
    }

    public String[] getUserInfoOnce(String key){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(key);
        final String[] targetInfo = {"",""};
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map =  (Map) dataSnapshot.getValue();
                targetInfo[0] = map.get("name");
                targetInfo[1] = map.get("city")+", "+map.get("state");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //do nothing
            }
        });
        return targetInfo;
    }
}
