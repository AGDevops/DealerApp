package com.example.dealerapp;

import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private RecyclerView allItemsList;
    private LinearLayoutManager layoutManager;

    private FirebaseFirestore db;
    private AllItemsListAdapter adapter;
    private Spinner spinner;
    private String filter;
    private static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);


        Toolbar toolbar = findViewById(R.id.all_item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

        db = FirebaseFirestore.getInstance();
        spinner = findViewById(R.id.all_items_list_spinner);

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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, cate);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(adapter);
                }
            }
        });
        spinner.setSelection(0, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter = parent.getSelectedItem().toString();
                if (parent.getItemAtPosition(position).equals("All Products")){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.items_container, new ALlProductsFrag()).commit();

                }else {
                    db.collection("AllItems").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                                String value = doc.getData().get("item_category").toString();
                                if (value.equals(filter)){
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    FilterProductsFrag frag = new FilterProductsFrag();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("filter", value);
                                    frag.setArguments(bundle);
                                    transaction.replace(R.id.items_container, frag).commit();

                                }
                            }
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
