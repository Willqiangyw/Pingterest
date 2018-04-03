package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class findplayer2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;
    private ListView mListView;

    private FirebaseListAdapter<UserInfomation> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findplayer2);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCoach);
//        toolbar.setTitle("Coaches");
//        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mListView = (ListView) findViewById(R.id.userListView);

        user = mAuth.getCurrentUser();
        assert user != null;
        userEmail = user.getEmail();
        assert userEmail != null;
        userKey = userEmail.substring(0,userEmail.indexOf('@'));

        Query query = mDatabase.orderByKey()
                .limitToLast(50);

        FirebaseListOptions<UserInfomation> options = new FirebaseListOptions.Builder<UserInfomation>()
                .setLayout(R.layout.single_user_layout)
                .setQuery(query, UserInfomation.class)
                .build();

        adapter = new FirebaseListAdapter<UserInfomation>(options) {
            @Override
            protected void populateView(View v, UserInfomation model, int position) {
                TextView name = v.findViewById(R.id.textViewUserInfo);
                name.setText(model.getName());
                TextView rate = v.findViewById(R.id.textViewSingleUserRate);
                rate.setText("Rate: " + model.getLevel());
                TextView location = v.findViewById(R.id.textViewSingleUserLocation);
                location.setText(model.getCity()+","+model.getState());
            }
        };

        ListView mUserListView = findViewById(R.id.userListView);
        mUserListView.setAdapter(adapter);

//        mUserListView.setClickable(true);
        mUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("abc", "hahahaha");
//                Intent intent = new Intent(findplayer2.this, ChatActivity.class);
                Intent intent = new Intent(findplayer2.this, checkOthersInformation.class);
                UserInfomation itemRef = adapter.getItem(position);

//                Toast toast = Toast.makeText(findplayer2.this, itemRef.getKey(), Toast.LENGTH_SHORT);
//                toast.show();
                intent.putExtra("key", itemRef.getKey());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void tryClick(View view){
        Intent intent = new Intent(findplayer2.this, ChatActivity.class);
        startActivity(intent);
    }

}
