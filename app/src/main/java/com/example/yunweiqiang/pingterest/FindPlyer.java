package com.example.yunweiqiang.pingterest;

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

public class FindPlyer extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;

    private RecyclerView mUserInfoRecyclerView;

    private FirebaseRecyclerAdapter<UserInfomation, FindPlyer.UserInfoViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_plyer);

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
    }

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

    public void click(View view){
        Intent intent = new Intent(this, Me.class);
        startActivity(intent);
    }
}
