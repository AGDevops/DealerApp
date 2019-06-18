package com.example.dealerapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dealerapp.AddItemActivity;
import com.example.dealerapp.AllItemsListAdapter;
import com.example.dealerapp.R;
import com.example.dealerapp.Utils.ALlItems;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterProductsFrag extends Fragment {

    private RecyclerView allItemsList;
    private LinearLayoutManager layoutManager;

    private FirebaseFirestore db;
    private AllItemsListAdapter adapter;

    public FilterProductsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_products, container, false);

        String value = getArguments().getString("filter");

        allItemsList = view.findViewById(R.id.filtered_products_list);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        allItemsList.setLayoutManager(layoutManager);
        allItemsList.setItemAnimator(new DefaultItemAnimator());

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("AllItems").whereEqualTo("item_category", value);
        final FirestoreRecyclerOptions<ALlItems> options = new FirestoreRecyclerOptions.Builder<ALlItems>()
                .setQuery(query, ALlItems.class)
                .build();

        adapter = new AllItemsListAdapter(getContext(), options);
        allItemsList.setAdapter(adapter);

//        adapter.setOnItemClick(new AllItemsListAdapter.OnItemClick() {
//            @Override
//            public void getPosition(int id, String userId) {
//                startActivity(new Intent(getActivity(), AddItemActivity.class));
//            }
//        });


        return view;
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

}
