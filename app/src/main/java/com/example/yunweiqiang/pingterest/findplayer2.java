package com.example.yunweiqiang.pingterest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class findplayer2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    public  ArrayList<String> searchRes = new ArrayList<>();

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;
    private ListView mListView;

    private FirebaseListAdapter<UserInfomation> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findplayer2);

        Intent cur = getIntent();
//        searchRes = cur.getStringArrayListExtra("name");

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCoach);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
                UserInfomation u = getItem(position);
//                if(u.getName().equals(Main2Activity.CURRENT_USER_NAME)){
//                    return;
//                }
                Log.d("MyActivity", "currently populating name is "+u.getName());
                TextView name = v.findViewById(R.id.textViewUserInfo);
                name.setText(model.getName());
                TextView rate = v.findViewById(R.id.textViewSingleUserRate);
                rate.setText("Rate: " + model.getRating());
                TextView level = v.findViewById(R.id.textViewSingleUserLevel);
                level.setText(model.getLevel());
                TextView desc = v.findViewById(R.id.textViewSingleUserDesc);
                desc.setText(model.getDescription());
                TextView location = v.findViewById(R.id.textViewSingleUserLocation);
                location.setText(model.getCity()+","+model.getState());
//                if( name = "Yunwei Qiang")
//                    v.
            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                UserInfomation u = getItem(position);
                final LayoutInflater something = getLayoutInflater();
                if(u.getName().equals(Main2Activity.CURRENT_USER_NAME) || checkHide(u)){
                    Log.d("MyActivity", "currently discarding name is "+u.getName());
//                    LayoutInflater lf = LayoutInflater.cloneInContext(something.getContext());
                    View tempview = something.inflate(R.layout.row_null,null);
//                    populateView(tempview, u, position);
                    return tempview;
                }
                else{
                    View tempview = something.inflate(R.layout.single_user_layout,null);
                    populateView(tempview, u, position);
                    return tempview;
                }
//                if (view == null) {
//                    view = LayoutInflater.from(viewGroup.getContext()).inflate(mLayout, viewGroup, false);
//                }
//
//                UserInfomation model = getItem(position);
//
//                // Call out to subclass to marshall this model into the provided view
//                View convertView;
//                if(model.getName().equals(Main2Activity.CURRENT_USER_NAME)){
//                    convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_null, viewGroup, false);
//                    populateView(convertView, model, position);
//                }
//                else{
//                    convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_null, viewGroup, false);
//                    populateView(view, model, position);
//                }
//
//                return view;
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

    public void trySearch(View view){
        Intent intent = new Intent(findplayer2.this, FindCoach.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                 searchRes = data.getStringArrayListExtra("result");
                 Log.d("getFromSearch", searchRes.toString());
                Toast.makeText(this, searchRes.toString(), Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Toast.makeText(findplayer2.this, "get result failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkHide(UserInfomation user){
        if(searchRes.size()==0)
            return false;
        if(!searchRes.contains(user.getKey()))
            return true;
        return false;
    }
}
