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

import java.util.Map;

public class EquipmentInfo extends AppCompatActivity {

    private FirebaseUser user;

    private TextView mItemName;
    private TextView mItemDescription;
    private TextView mItemPrice;
    private TextView mSellerInfo;


    private String userEmail;
    private String userKey;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_info);

        Intent currentIntent = getIntent();
        String itemNameString = currentIntent.getStringExtra("key");

        TextView itemName = (TextView) findViewById(R.id.ItemName);
        itemName.setText(itemNameString);


        mDatabase = FirebaseDatabase.getInstance().getReference("Equipments").child(itemNameString);

        mItemName = findViewById(R.id.ItemName);
        mItemDescription= findViewById(R.id.ItemDescription);
        mItemPrice = findViewById(R.id.ItemPrice);
        mSellerInfo= findViewById(R.id.Seller);

       // Log.d("E_VALUE", "The key is " + userKey);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map =  (Map) dataSnapshot.getValue();
//                Log.d("E_Value", "get from data! " + dataSnapshot.getValue());
                String description = map.get("ItemDescription");
                String name = map.get("ItemName");
                String price = map.get("ItemPrice");
                String seller = map.get("SellerInfo");

                mItemName.setText(description);
                mItemDescription.setText(name);
                mItemPrice.setText(price);
                mSellerInfo.setText(seller);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("E_Value", "something went wrong", databaseError.toException());
            }
        });
    }

    public void equipmentChat(View view){
        Intent intent = new Intent(this, EditMeInformation.class);
        startActivity(intent);
    }
}
