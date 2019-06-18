package com.example.dealerapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dealerapp.AllDealersListAdapter;
import com.example.dealerapp.DealersReqListAdapter;
import com.example.dealerapp.R;
import com.example.dealerapp.Utils.Users;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllDealersFrag extends Fragment {


    private RecyclerView allDealersList;
    private LinearLayoutManager layoutManager;

    private FirebaseFirestore db;
    private DealersReqListAdapter adapter;

    public AllDealersFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_dealers2, container, false);

        allDealersList = view.findViewById(R.id.all_dealers_list);
        layoutManager = new LinearLayoutManager(getActivity());
        allDealersList.setLayoutManager(layoutManager);
        allDealersList.setItemAnimator(new DefaultItemAnimator());

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Dealers");
        final FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();

        adapter = new DealersReqListAdapter(getContext(), options);
        allDealersList.setAdapter(adapter);


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
