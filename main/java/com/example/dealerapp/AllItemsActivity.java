package com.example.dealerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dealerapp.Fragments.ALlProductsFrag;
import com.example.dealerapp.Fragments.FilterProductsFrag;
import com.example.dealerapp.Utils.ALlItems;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AllItemsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Spinner spinner;

    private RecyclerView allItemsList;
    private LinearLayoutManager layoutManager;
    private RelativeLayout container;

    private AllItemsListAdapter adapter;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);


        Toolbar toolbar = findViewById(R.id.all_item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();
        spinner = findViewById(R.id.all_items_list_spinner);

        allItemsList = findViewById(R.id.items_list_in_container);
        layoutManager = new GridLayoutManager(this, 2);
        allItemsList.setLayoutManager(layoutManager);

        spinner.setSelection(0, false);

        db.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> cate = new ArrayList<>();
                    cate.add(0, "All Products");

                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String name = doc.getData().get("item_category").toString();
                        cate.add(name);
                    }
                    ArrayAdapter<String> madapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, cate);

                    madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(madapter);
                }
            }
        });

        Query query = db.collection("AllItems");
        final FirestoreRecyclerOptions<ALlItems> options = new FirestoreRecyclerOptions.Builder<ALlItems>()
                .setQuery(query, ALlItems.class)
                .build();

        adapter = new AllItemsListAdapter(getApplicationContext(), options);
        allItemsList.setAdapter(adapter);

        adapter.setOnItemClick(new AllItemsListAdapter.OnItemClick() {
            @Override
            public void getPosition(String item_id) {
                Intent intent = new Intent(AllItemsActivity.this, EditProductActivity.class);
                intent.putExtra("push_id", item_id);
                startActivity(intent);
            }
        });

        //-----spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                db.collection("AllItems").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                final String value = doc.getData().get("item_category").toString();
                                String filter = parent.getSelectedItem().toString();
                                if (value.equals(filter)){
                                    Toast.makeText(AllItemsActivity.this, filter, Toast.LENGTH_SHORT).show();

                                    Query query = db.collection("AllItems").whereEqualTo("item_category", filter);
                                    final FirestoreRecyclerOptions<ALlItems> options = new FirestoreRecyclerOptions.Builder<ALlItems>()
                                            .setQuery(query, ALlItems.class)
                                            .build();

                                    adapter = new AllItemsListAdapter(getApplicationContext(), options);
                                    allItemsList.setAdapter(adapter);

                                    adapter.setOnItemClick(new AllItemsListAdapter.OnItemClick() {
                                        @Override
                                        public void getPosition(String item_id) {
                                            Intent intent = new Intent(AllItemsActivity.this, EditProductActivity.class);
                                            intent.putExtra("push_id", item_id);
                                            startActivity(intent);
                                        }
                                    });

                                    adapter.startListening();

                                }else if (filter.equals("All Products")){
                                    Query query = db.collection("AllItems");
                                    final FirestoreRecyclerOptions<ALlItems> options = new FirestoreRecyclerOptions.Builder<ALlItems>()
                                            .setQuery(query, ALlItems.class)
                                            .build();

                                    adapter = new AllItemsListAdapter(getApplicationContext(), options);
                                    allItemsList.setAdapter(adapter);

                                    adapter.setOnItemClick(new AllItemsListAdapter.OnItemClick() {
                                        @Override
                                        public void getPosition(String item_id) {
                                            Intent intent = new Intent(AllItemsActivity.this, EditProductActivity.class);
                                            intent.putExtra("push_id", item_id);
                                            startActivity(intent);
                                        }
                                    });

                                    adapter.startListening();
                                }
                            }
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStart() {
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

        adapter = new AllItemsListAdapter(getApplicationContext(), options);
        allItemsList.setAdapter(adapter);

        adapter.setOnItemClick(new AllItemsListAdapter.OnItemClick() {
            @Override
            public void getPosition(String item_id) {
                Intent intent = new Intent(AllItemsActivity.this, EditProductActivity.class);
                intent.putExtra("push_id", item_id);
                startActivity(intent);
            }
        });

        adapter.startListening();


    }

    private void searchData(String s) {
        dialog.setMessage("Searching");
        dialog.show();
        Query query = db.collection("AllItems").orderBy("search").startAt(s).endAt(s+"\uf8ff");
        final FirestoreRecyclerOptions<ALlItems> options = new FirestoreRecyclerOptions.Builder<ALlItems>()
                .setQuery(query, ALlItems.class)
                .build();

        adapter = new AllItemsListAdapter(getApplicationContext(), options);
        allItemsList.setAdapter(adapter);

        adapter.setOnItemClick(new AllItemsListAdapter.OnItemClick() {
            @Override
            public void getPosition(String item_id) {
                Intent intent = new Intent(AllItemsActivity.this, EditProductActivity.class);
                intent.putExtra("push_id", item_id);
                startActivity(intent);
            }
        });
        adapter.startListening();

    }

}
