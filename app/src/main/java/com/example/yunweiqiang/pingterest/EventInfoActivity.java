package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Map;

public class EventInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView mEventKeyTextView;
    private TextView mEventHolderTextView;
    private TextView mEventTimeTextView;
    private TextView mEventLocationTextView;
    private TextView mEventParticipantTextView;

    private String userEmail;
    private String userKey;
    private String otherKey;

    private DatabaseReference mDatabase;

    private String eventKey;

    private Map<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Intent in = getIntent();
        eventKey = in.getStringExtra("key");
        Toast.makeText(this,eventKey,Toast.LENGTH_LONG).show();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user!=null) {
            userEmail = user.getEmail();
            userKey = userEmail.substring(0,userEmail.indexOf('@'));
        }
        else{
            Toast.makeText(this, "no user", Toast.LENGTH_LONG).show();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Events").child(eventKey);

        mEventKeyTextView = findViewById(R.id.textViewKey);
        mEventHolderTextView = findViewById(R.id.textViewHolder);
        mEventTimeTextView = findViewById(R.id.textViewTime);
        mEventLocationTextView = findViewById(R.id.textViewLocation);
        mEventParticipantTextView = findViewById(R.id.textViewParticipant);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map =  (Map) dataSnapshot.getValue();
//                Log.d("E_Value", "get from data! " + dataSnapshot.getValue());
                String eventKey = map.get("key");
                String eventHolder = map.get("holder");
                String eventTime = map.get("time");
                String eventLocation = map.get("location");
                String eventParticipant = map.get("participant");
                otherKey = eventHolder;

                mEventKeyTextView.setText(eventKey);
                mEventHolderTextView.setText(eventHolder);
                mEventTimeTextView.setText(eventTime);
                mEventLocationTextView.setText(eventLocation);
                mEventParticipantTextView.setText(eventParticipant);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("E_Value", "something went wrong", databaseError.toException());
            }
        });
    }

    public void chat(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        String[] temp = {userKey, otherKey};
        Arrays.sort(temp);
        String pass = temp[0] +"+"+ temp[1];
        intent.putExtra("key", pass);
        startActivity(intent);
    }

    public void reserve(View view){
        String cur = map.get("participant");
        if(cur.length()==0){
            mDatabase.child("participant").setValue(userKey);
            Toast.makeText(this, "reserve success", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "already taken", Toast.LENGTH_SHORT).show();
        }
    }
}
