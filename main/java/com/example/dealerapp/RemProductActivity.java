package com.example.dealerapp;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dealerapp.Utils.ALlItems;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RemProductActivity extends AppCompatActivity {

    private RecyclerView allItemsList;
    private LinearLayoutManager layoutManager;

    private FirebaseFirestore db;
    private AllDltItemsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rem_product);

        Toolbar toolbar = findViewById(R.id.rem_item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allItemsList = findViewById(R.id.delete_items_list);
        layoutManager = new LinearLayoutManager(this);
        allItemsList.setLayoutManager(layoutManager);
        allItemsList.setItemAnimator(new DefaultItemAnimator());

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("AllItems");
        final FirestoreRecyclerOptions<ALlItems> options = new FirestoreRecyclerOptions.Builder<ALlItems>()
                .setQuery(query, ALlItems.class)
                .build();

        adapter = new AllDltItemsListAdapter(getApplicationContext(), options);
        allItemsList.setAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.all_item_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchData(s.toLowerCase());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchLiveData(s.toLowerCase());
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting){
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void searchLiveData(String s) {

        Query query = db.collection("AllItems").orderBy("search").startAt(s).endAt(s+"\uf8ff");
        final FirestoreRecyclerOptions<ALlItems> options = new FirestoreRecyclerOptions.Builder<ALlItems>()
                .setQuery(query, ALlItems.class)
                .build();

        adapter = new AllDltItemsListAdapter(getApplicationContext(), options);
        allItemsList.setAdapter(adapter);

        adapter.startListening();


    }

    private void searchData(String s) {
        allItemsList.setVisibility(View.VISIBLE);
        Query query = db.collection("AllItems").orderBy("search").startAt(s).endAt(s+"\uf8ff");
        final FirestoreRecyclerOptions<ALlItems> options = new FirestoreRecyclerOptions.Builder<ALlItems>()
                .setQuery(query, ALlItems.class)
                .build();

        adapter = new AllDltItemsListAdapter(getApplicationContext(), options);
        allItemsList.setAdapter(adapter);
        adapter.startListening();

    }
}
