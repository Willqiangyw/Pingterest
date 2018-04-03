package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;

    private EditText mtypeInEditText;
    private RecyclerView mChatRecyclerView;

    private FirebaseRecyclerAdapter<Message, messageViewHolder> adapter;
    private FirebaseRecyclerAdapter<Message, messageViewHolder> adapter2;

    private DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

//    public static boolean flag = false;
    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        mtypeInEditText = (EditText) findViewById(R.id.typeInEditText);
        Intent in = getIntent();
        key = in.getStringExtra("key");
        mDatabase = FirebaseDatabase.getInstance().getReference("Chats").child(key);

        mChatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        mChatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mChatRecyclerView.setLayoutManager(linearLayoutManager);


        //get user information
        user = mAuth.getCurrentUser();
        assert user != null;
        userEmail = user.getEmail();
        assert userEmail != null;
        userKey = userEmail.substring(0,userEmail.indexOf('@'));

        //try everything here

        Query query = mDatabase.orderByKey()
                .limitToLast(50);

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Message, messageViewHolder>(
                options
        ) {

            boolean flag2 = false;
//            ViewGroup temp1;
//            int temp2;
//            protected boolean checkFlag()

            @Override
            protected void onBindViewHolder(@NonNull messageViewHolder holder, int position, @NonNull Message model) {
                    holder.setContent(model.getContent(), model.getUser(), model.getTime());
//                    holder.se
//                    if(model.getUser().equals(userKey))
//                        flag2 = true;
//                    else
//                        flag2 = false;
//                    onCreateViewHolder(temp1, temp2)
            }

            @Override
            public messageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                temp1 = parent;
//                temp2 = viewType;
//                checkFlag();
                View view;
//                if(!flag2) {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlechatlayout, parent, false);
//                }
//                else {
//                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlechatlayout2, parent, false);
////                    LayoutInflater.from(parent.getContext()).inflate(1)
//                }
                Log.d("HEYTHERE",parent.getContext().toString());
                return new messageViewHolder(view);
            }
        };
//        adapter2 = new FirebaseRecyclerAdapter<Message, messageViewHolder>(
//                options
//        ) {
//            @Override
//            protected void onBindViewHolder(@NonNull messageViewHolder holder, int position, @NonNull Message model) {
//                holder.setContent(model.getContent(), model.getUser(), model.getTime());
//            }
//
//            @Override
//            public messageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlechatlayout2, parent, false);
//                return new messageViewHolder(view);
//            }
//        };
        mChatRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
    }

    public void send(View view){
        String message = mtypeInEditText.getText().toString();
        if(!TextUtils.isEmpty(message)){
            Date currentTime = Calendar.getInstance().getTime();
            final DatabaseReference newMessage = mDatabase.push();
            newMessage.child("content").setValue(message);
            newMessage.child("user").setValue(userKey);
//            newMessage.child("time").setValue(currentTime);
            newMessage.child("time").setValue(df.format(currentTime));
            mtypeInEditText.setText("");
        }
    }

    public static class messageViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public messageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
//            mView.layout
        }

        public void setContent(String content, String user, String time) {
            TextView mMessageTextView = (TextView) mView.findViewById(R.id.messagecontentTextView);
            mMessageTextView.setText(content);
            TextView mUserTextView = (TextView) mView.findViewById(R.id.userNameChatTextView);
            mUserTextView.setText(user);
            TextView mTimeTextView = (TextView) mView.findViewById(R.id.timeTextView);
            mTimeTextView.setText(time);
//            if(user.equals(userKey))

        }
    }

}

