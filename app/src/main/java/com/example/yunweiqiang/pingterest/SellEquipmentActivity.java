package com.example.yunweiqiang.pingterest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class SellEquipmentActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userEmail;
    private String userKey;

    private EditText mTitle, mDescription, mUsedPrice, mNewPrice;
    private String title, description, usedPrice, newPrice, seller, sellerKey, sellAddr, sellTime;


    private DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_equipment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSellEquipment);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            userEmail = user.getEmail();
            userKey = userEmail.substring(0, userEmail.indexOf('@'));
        } else {
            Toast.makeText(this, "no user", Toast.LENGTH_LONG).show();
        }


        mTitle = (EditText) findViewById(R.id.editTextSellTitle);
        mDescription = (EditText) findViewById(R.id.editTextSellDescription);
        mUsedPrice = (EditText) findViewById(R.id.editTextSellUsedPrice);
        mNewPrice = (EditText) findViewById(R.id.editTextSellNewPrice);
    }

    public void confirm(View view) throws IOException {
        if (user == null) {
            Toast.makeText(this, "please sign in and try again", Toast.LENGTH_LONG).show();
            return;
        }

        title = mTitle.getText().toString();
        description = mDescription.getText().toString();
        usedPrice = mUsedPrice.getText().toString();
        newPrice = mNewPrice.getText().toString();
        seller = Main2Activity.CURRENT_USER_NAME;
        sellerKey = userKey;
        sellAddr = Main2Activity.CURRENT_USER_ADDR;
        Date currentTime = Calendar.getInstance().getTime();
        sellTime = df.format(currentTime);
        if(TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(usedPrice) || TextUtils.isEmpty(newPrice)){
            Toast.makeText(SellEquipmentActivity.this, "please fill in correct information", Toast.LENGTH_LONG).show();
        }
        else{
            mDatabase = FirebaseDatabase.getInstance().getReference("Equipments").child(title);
            mDatabase.child("itemDescription").setValue(description);
            mDatabase.child("itemName").setValue(title);
            mDatabase.child("key").setValue(title);
            mDatabase.child("newPrice").setValue(newPrice);
            mDatabase.child("usedPrice").setValue(usedPrice);
            mDatabase.child("sellAddr").setValue(sellAddr);
            mDatabase.child("sellTime").setValue(sellTime);
            mDatabase.child("sellerInfo").setValue(seller);
            mDatabase.child("sellerKey").setValue(sellerKey);
            Toast.makeText(SellEquipmentActivity.this, "Successfully submit", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
