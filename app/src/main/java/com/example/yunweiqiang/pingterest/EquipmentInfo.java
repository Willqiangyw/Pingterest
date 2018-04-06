package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class EquipmentInfo extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private ImageView image;

    private TextView mItemUserdPrice;
    private TextView mItemDescription;
    private TextView mItemName;
    private TextView mItemNewPrice;
    private TextView mSellerName;
    private TextView mSellTime;
    private TextView mSellAddr;

    private String usedPrice, newPrice, seller, sellTime, sellAddr, itemName, itemDescription;
    private String sellerKey;

    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_info);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user!=null) {
            userEmail = user.getEmail();
            userKey = userEmail.substring(0,userEmail.indexOf('@'));
        }
        else{
            Toast.makeText(this, "no user", Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEquipmentInfo);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent currentIntent = getIntent();
        String itemNameString = currentIntent.getStringExtra("key");

        image = (ImageView) findViewById(R.id.imageViewEquipmentUser);


        mItemName = (TextView) findViewById(R.id.textViewEquipmentName);
        mItemUserdPrice = (TextView) findViewById(R.id.textViewUsedPrice);
        mItemNewPrice = (TextView) findViewById(R.id.textViewCurPrice);
        mSellerName = (TextView) findViewById(R.id.textViewSellerName);
        mSellTime = (TextView) findViewById(R.id.textViewSellTime);
        mSellAddr = (TextView) findViewById(R.id.textViewSellLocation);
        mItemDescription = (TextView) findViewById(R.id.textViewEquipmentDesc2);


        mDatabase = FirebaseDatabase.getInstance().getReference("Equipments").child(itemNameString);

       // Log.d("E_VALUE", "The key is " + userKey);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map =  (Map) dataSnapshot.getValue();
//                Log.d("E_Value", "get from data! " + dataSnapshot.getValue());
                usedPrice = map.get("usedPrice");
                newPrice = map.get("newPrice");
                seller = map.get("sellerInfo");
                sellTime = map.get("sellTime");
                sellAddr = map.get("sellAddr");
                itemName = map.get("itemName");
                itemDescription = map.get("itemDescription");
                sellerKey = map.get("sellerKey");

                mItemName.setText(itemName);
                mItemUserdPrice.setText("$"+usedPrice);
                mItemUserdPrice.setPaintFlags(mItemUserdPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mItemNewPrice.setText("$"+newPrice);
                mSellerName.setText(seller);
                mSellAddr.setText(sellAddr);
                mSellTime.setText(sellTime);
                mItemDescription.setText(itemDescription);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("E_Value", "something went wrong", databaseError.toException());
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EquipmentInfo.this, checkOthersInformation.class);
                intent.putExtra("key", sellerKey);
                startActivity(intent);
            }
        });

    }

    public void equipmentChat(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        String[] temp = {userKey, sellerKey};
        Arrays.sort(temp);
        String pass = temp[0] +"+"+ temp[1];
        intent.putExtra("key", pass);
        intent.putExtra("otherkey",sellerKey);
        startActivity(intent);
    }
}
