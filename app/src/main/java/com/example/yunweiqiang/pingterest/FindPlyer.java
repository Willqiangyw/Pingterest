package com.example.yunweiqiang.pingterest;


        import android.app.Activity;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

        import java.util.ArrayList;

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


    public  ArrayList<String> searchRes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_plyer);

        mDatabase = FirebaseDatabase.getInstance().getReference("Events");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEvent);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Query query = mDatabase.orderByKey()
                .limitToLast(50);

        FirebaseListOptions<EventInformation> options = new FirebaseListOptions.Builder<EventInformation>()
                .setLayout(R.layout.single_event_layout)
                .setQuery(query, EventInformation.class)
                .build();

        adapter = new FirebaseListAdapter<EventInformation>(options) {
            @Override
            protected void populateView(View v, EventInformation model, int position) {
                EventInformation e = getItem(position);
                Log.d("MyActivity", "currently populating event is "+e.getKey());
                TextView time = (TextView) v.findViewById(R.id.textViewSingleTime);
                time.setText(model.getTime());
                TextView location = (TextView) v.findViewById(R.id.textViewSingleLocation);
                location.setText(model.getLocation());
                TextView name = (TextView) v.findViewById(R.id.textViewSingleEventName);
                name.setText(model.getKey());
                TextView holder = (TextView) v.findViewById(R.id.textViewSingleEventHolder);
                holder.setText(model.getHolder());
                TextView desc = (TextView) v.findViewById(R.id.textViewSingleEventDescription);
                desc.setText(model.getDescription());
            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                EventInformation u = getItem(position);
                final LayoutInflater something = getLayoutInflater();
                if(checkHide(u)){
                    Log.d("MyActivity", "currently discarding event is "+u.getKey());
//                    LayoutInflater lf = LayoutInflater.cloneInContext(something.getContext());
                    View tempview = something.inflate(R.layout.row_null,null);
//                    populateView(tempview, u, position);
                    return tempview;
                }
                else{
                    View tempview = something.inflate(R.layout.single_event_layout,null);
                    populateView(tempview, u, position);
                    return tempview;
                }
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

    public void eventSearch(View view){
        Intent intent = new Intent(FindPlyer.this, FindEvent.class);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                searchRes = data.getStringArrayListExtra("result");
                Toast.makeText(this, searchRes.toString(), Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Toast.makeText(FindPlyer.this, "get result failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkHide(EventInformation user){
        if(searchRes.size()==0)
            return false;
        if(!searchRes.contains(user.getKey()))
            return true;
        return false;
    }
}


