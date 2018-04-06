package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    static private String userKey;

    private DatabaseReference mDatabase;

    private EditText mtypeInEditText;
    private RecyclerView mChatRecyclerView;

    private FirebaseRecyclerAdapter<Message, messageViewHolder> adapter;
    private FirebaseRecyclerAdapter<Message, messageViewHolder> adapter2;
    private Query query;

    private DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

//    public static boolean flag = false;
    private String key;
    private String otherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAuth = FirebaseAuth.getInstance();
        mtypeInEditText = (EditText) findViewById(R.id.typeInEditText);
        Intent in = getIntent();
        key = in.getStringExtra("key");
        otherName = in.getStringExtra("otherkey");
        mDatabase = FirebaseDatabase.getInstance().getReference("Chats").child(key);

        mChatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        mChatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mChatRecyclerView.setLayoutManager(linearLayoutManager);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //get user information
        user = mAuth.getCurrentUser();
        assert user != null;
        userEmail = user.getEmail();
        assert userEmail != null;
        userKey = userEmail.substring(0,userEmail.indexOf('@'));



        TextView title = (TextView) findViewById(R.id.textViewToolbarChat) ;
        title.setText(otherName);



        //try everything here

        query = mDatabase.orderByKey()
                .limitToLast(50);

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Message, messageViewHolder>(
                options
        ) {

//            boolean flag2 = false;
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
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mChatRecyclerView.smoothScrollToPosition(adapter.getItemCount()+1);
        }
    }

    public class messageViewHolder extends RecyclerView.ViewHolder{

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

            TextView mTimeTextView = (TextView) mView.findViewById(R.id.timeTextView);

//            ConstraintLayout constraintLayout = (ConstraintLayout) mView.findViewById(R.id.singleChatLayout);
            LinearLayout linearLayout1 = mView.findViewById(R.id.singleChatLayout1);
            LinearLayout linearLayout2 = mView.findViewById(R.id.singleChatLayout2);
            LinearLayout linearLayout3 = mView.findViewById(R.id.singleChatLayout3);
            if(user!=null && !user.equals(ChatActivity.userKey)){
                mUserTextView.setTextColor(getResources().getColor(R.color.black));
                mTimeTextView.setTextColor(getResources().getColor(R.color.black));
//                constraintLayout.setBackgroundResource(R.drawable.bubble_out);
                mMessageTextView.setBackgroundResource(R.drawable.bubble_final_2);

                linearLayout2.setGravity(Gravity.LEFT);
                linearLayout1.setGravity(Gravity.LEFT);
                mUserTextView.setText(user);
                mTimeTextView.setText(time);
            }
            else{
                mUserTextView.setTextColor(getResources().getColor(R.color.chatBlue));
                mTimeTextView.setTextColor(getResources().getColor(R.color.chatBlue));
//                constraintLayout.setBackgroundResource(R.drawable.bubble_in);
                mMessageTextView.setBackgroundResource(R.drawable.bubble_final_1);
                linearLayout2.setGravity(Gravity.RIGHT);
                linearLayout1.setGravity(Gravity.RIGHT);
//                linearLayout3.setGravity(Gravity.RIGHT);
                mUserTextView.setText(time);
                mTimeTextView.setText(user);
            }



        }
    }

}

