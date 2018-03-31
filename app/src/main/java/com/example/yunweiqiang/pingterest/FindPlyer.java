package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FindPlyer extends AppCompatActivity {

//    private FirebaseAuth mAuth;
//    private FirebaseUser user;
//
//    private String userEmail;
//    private String userKey;
//
//    private DatabaseReference mDatabase;
//
//    private RecyclerView mUserInfoRecyclerView;

//    private FirebaseRecyclerAdapter<UserInfomation, FindPlyer.UserInfoViewHolder> adapter;

    private DatabaseReference mDatabase;
    private FirebaseListAdapter<EventInformation> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_plyer);

        mDatabase = FirebaseDatabase.getInstance().getReference("Events");

        Query query = mDatabase.orderByKey()
                .limitToLast(50);

        FirebaseListOptions<EventInformation> options = new FirebaseListOptions.Builder<EventInformation>()
                .setLayout(R.layout.single_event_layout)
                .setQuery(query, EventInformation.class)
                .build();

        adapter = new FirebaseListAdapter<EventInformation>(options) {
            @Override
            protected void populateView(View v, EventInformation model, int position) {
                TextView time = (TextView) v.findViewById(R.id.textViewSingleTime);
                TextView location = (TextView) v.findViewById(R.id.textViewSingleLocation);
                time.setText(model.getTime());
//                time.setText("heihei");
                location.setText(model.getLocation());
            }
        };

        ListView mEventListView = findViewById(R.id.listViewEvent);
        mEventListView.setAdapter(adapter);

        mEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("abc", "hahahaha");
//                Intent intent = new Intent(findplayer2.this, ChatActivity.class);
                Intent intent = new Intent(FindPlyer.this, EventInfoActivity.class);
                EventInformation itemRef = adapter.getItem(position);

//                Toast toast = Toast.makeText(findplayer2.this, itemRef.getKey(), Toast.LENGTH_SHORT);
//                toast.show();
                intent.putExtra("key", itemRef.getKey());
                startActivity(intent);
            }
        });
        /*
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mUserInfoRecyclerView = (RecyclerView) findViewById(R.id.userInfoRecyclerView);
        mUserInfoRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mUserInfoRecyclerView.setLayoutManager(linearLayoutManager);

        user = mAuth.getCurrentUser();
        assert user != null;
        userEmail = user.getEmail();
        assert userEmail != null;
        userKey = userEmail.substring(0,userEmail.indexOf('@'));

        Query query = mDatabase.orderByKey()
                .limitToLast(50);

        FirebaseRecyclerOptions<UserInfomation> options =
                new FirebaseRecyclerOptions.Builder<UserInfomation>()
                        .setQuery(query, UserInfomation.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<UserInfomation, UserInfoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserInfoViewHolder holder, int position, @NonNull UserInfomation model) {
                holder.setName(model.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("E", "onItemClickGeneral: position ");
                    }
                });
                holder.bind(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("E", "onItemClickGeneral: position 2");
                    }
                });
            }

            @Override
            public UserInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_layout, parent, false);
                return new FindPlyer.UserInfoViewHolder(view);
            }
        };

        mUserInfoRecyclerView.setAdapter(adapter);
        adapter.startListening();
        */


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
/*
    public static class UserInfoViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public UserInfoViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String Name) {
            TextView mUserInfoTextView = (TextView) mView.findViewById(R.id.textViewUserInfo);
            mUserInfoTextView.setText(Name);
        }

        public void bind(UserInfomation u, View.OnClickListener listener){
            mView.setOnClickListener(listener);
        }

    }
    */

    public void addEvent(View view){
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }
}


