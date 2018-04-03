package com.example.yunweiqiang.pingterest;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ListView listView;
    private ArrayList<String> equipmentItem;
    private FirebaseListAdapter<EquipmentInformation> adapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        listView = (ListView) findViewById(R.id.EquipmentListView);
        mDatabase = FirebaseDatabase.getInstance().getReference("Equipments");

        equipmentItem = new ArrayList<>();
//        ArrayAdapter<String> adapterSearch  = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipmentItem);
//        listView.setAdapter(adapterSearch);

//        equipmentItem.add("Ball");
//        equipmentItem.add("Pad");
//        equipmentItem.add("Supplements");
//        equipmentItem.add("Desk");

        Query query = mDatabase.orderByKey()
                .limitToLast(50);

        FirebaseListOptions<EquipmentInformation> options = new FirebaseListOptions.Builder<EquipmentInformation>()
                .setLayout(R.layout.single_equipment_layout)
                .setQuery(query, EquipmentInformation.class)
                .build();

        adapter = new FirebaseListAdapter<EquipmentInformation>(options) {
            @Override
            protected void populateView(View v, EquipmentInformation model, int position) {
                // Bind the Chat to the view
                // ...
                TextView equipmentKey = v.findViewById(R.id.textViewKeyInfo);
                equipmentKey.setText(model.getKey());
                TextView equipmentSeller = v.findViewById(R.id.textViewSeller);
                equipmentSeller.setText(model.getSellerInfo());
                TextView equipmentBrand = v.findViewById(R.id.textViewBrand);
                equipmentBrand.setText(model.getLocation());
                TextView equipmentPrice = v.findViewById(R.id.textViewPrice);
                equipmentPrice.setText(model.getItemPrice());
                ImageView image = v.findViewById(R.id.imageView);
//                image.setImageURI();
                if(!equipmentItem.contains(model.getKey()))
                    equipmentItem.add(model.getKey());
                Toast.makeText(SearchActivity.this, "" + equipmentItem , Toast.LENGTH_SHORT).show();
            }
        };

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipmentItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("abc", "hahahaha");
//                Intent intent = new Intent(findplayer2.this, ChatActivity.class);
                Intent intent = new Intent(SearchActivity.this, EquipmentInfo.class);
                EquipmentInformation itemRef = adapter.getItem(position);

//                Toast toast = Toast.makeText(findplayer2.this, itemRef.getKey(), Toast.LENGTH_SHORT);
//                toast.show();
                intent.putExtra("key", itemRef.getKey());
                startActivity(intent);


//                String text = listView.getItemAtPosition(position).toString();
//                Intent myIntent = new Intent(view.getContext(), EquipmentInfo.class);
//                myIntent.putExtra("key",text);
//                startActivityForResult(myIntent, 0);
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String text = listView.getItemAtPosition(i).toString();
//                Intent myIntent = new Intent(view.getContext(), EquipmentInfo.class);
//                myIntent.putExtra("key",text);
//                startActivityForResult(myIntent, 0);
//              //  Toast.makeText(SearchActivity.this, "" + text, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);


        MenuItem searchItem = menu.findItem(R.id.equipment_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText){
                ArrayList<String> templist = new ArrayList<>();
               // for(String temp: equipmentItem){
                for(String temp: equipmentItem){
                    if(temp.toLowerCase().contains(newText.toLowerCase())){
                        templist.add(temp);
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, templist);
                listView.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }

        });

        return super.onCreateOptionsMenu(menu);
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

}



