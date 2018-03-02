package com.example.yunweiqiang.pingterest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

    private DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        mtypeInEditText = (EditText) findViewById(R.id.typeInEditText);
        mDatabase = FirebaseDatabase.getInstance().getReference("Chats").child("test");

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
            @Override
            protected void onBindViewHolder(@NonNull messageViewHolder holder, int position, @NonNull Message model) {
                    holder.setContent(model.getContent(), model.getUser(), model.getTime());
            }

            @Override
            public messageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlechatlayout, parent, false);
                return new messageViewHolder(view);
            }
        };
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
        }

        public void setContent(String content, String user, String time) {
            TextView mMessageTextView = (TextView) mView.findViewById(R.id.messagecontentTextView);
            mMessageTextView.setText(content);
            TextView mUserTextView = (TextView) mView.findViewById(R.id.userNameChatTextView);
            mUserTextView.setText(user);
            TextView mTimeTextView = (TextView) mView.findViewById(R.id.timeTextView);
            mTimeTextView.setText(time);

        }
    }
}
