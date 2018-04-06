package com.example.yunweiqiang.pingterest;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

public class SearchActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ListView listView;
//    private ArrayList<String> equipmentItem;
    private FirebaseListAdapter<EquipmentInformation> adapter;

    private EditText searchField;
    private String searchString;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent curIntent = getIntent();
        searchString = curIntent.getStringExtra("searchKey");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEquipment);
        toolbar.setNavigationIcon(R.drawable.returnbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView = (ListView) findViewById(R.id.EquipmentListView);
        mDatabase = FirebaseDatabase.getInstance().getReference("Equipments");
        searchField = (EditText) findViewById(R.id.editTextEquipmentSearch);

//        equipmentItem = new ArrayList<>();
//        ArrayAdapter<String> adapterSearch  = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipmentItem);
//        listView.setAdapter(adapterSearch);

//        equipmentItem.add("Ball");
//        equipmentItem.add("Pad");
//        equipmentItem.add("Supplements");
//        equipmentItem.add("Desk");

        Query query = mDatabase.orderByKey()
                .startAt(searchString).endAt(searchString+"\uf8ff")
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
                TextView equipmentPrice = v.findViewById(R.id.textViewPrice);
                equipmentPrice.setText(model.getNewPrice());
                TextView sellTime = v.findViewById(R.id.textViewsingleEquipTime);
                sellTime.setText(model.getSellTime());
                TextView sellLocation = v.findViewById(R.id.textViewSingleEquipLocation);
                sellLocation.setText(model.getNewPrice());
//                ImageView image = v.findViewById(R.id.imageViewEquipment);
//                image.setImageURI();
//                if(!equipmentItem.contains(model.getKey()))
//                    equipmentItem.add(model.getKey());
//                Toast.makeText(SearchActivity.this, "" + equipmentItem , Toast.LENGTH_SHORT).show();
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

//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//
//
//        MenuItem searchItem = menu.findItem(R.id.equipment_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
//            @Override
//            public boolean onQueryTextChange(String newText){
//                ArrayList<String> templist = new ArrayList<>();
//               // for(String temp: equipmentItem){
//                for(String temp: equipmentItem){
//                    if(temp.toLowerCase().contains(newText.toLowerCase())){
//                        templist.add(temp);
//                    }
//
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, templist);
//                listView.setAdapter(adapter);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String query){
//                return false;
//            }
//
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }

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

    public void sell(View view){
        Intent intent = new Intent(this, SellEquipmentActivity.class);
        startActivity(intent);
    }

    public void search(View view){
        searchString = searchField.getText().toString();
        Intent intent = getIntent();
        intent.putExtra("searchKey",searchString);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}



