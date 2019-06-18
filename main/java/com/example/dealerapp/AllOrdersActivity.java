package com.example.dealerapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dealerapp.Dealers.OrderAdapter;
import com.example.dealerapp.Dealers.OrdersActivity;
import com.example.dealerapp.Utils.ALlItems;
import com.example.dealerapp.Utils.Order;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AllOrdersActivity extends AppCompatActivity {

    RecyclerView mOrderList;
    List<Order> OrderList;
    AllOrderAdapter OrderAdapter;
    FirebaseUser firebaseUser;
    ProgressDialog pd;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        pd = new ProgressDialog(AllOrdersActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();

        Toolbar toolbar = findViewById(R.id.all_order_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mOrderList = (RecyclerView) findViewById(R.id.all_orders_list);
        mOrderList.setHasFixedSize(false);
        mOrderList.setLayoutManager(new LinearLayoutManager(this));

        OrderList = new ArrayList<>();

        db.collection("AllOrders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Order p = doc.toObject(Order.class);
                        OrderList.add(p);
                    }

                    OrderAdapter = new AllOrderAdapter(getApplicationContext(), OrderList);
                    mOrderList.setAdapter(OrderAdapter);

                    OrderAdapter.setOnItemClick(new AllOrderAdapter.OnItemClick() {
                        @Override
                        public void getPosition(String userId) {
                            db.collection("AllOrders").document(userId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AllOrdersActivity.this, "deleted", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    });
                    if(pd.isShowing())
                    {
                        pd.dismiss();
                    }
                }
            }
        });
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

        OrderList = new ArrayList<>();

        Query query = db.collection("AllOrders").orderBy("search").startAt(s).endAt(s+"\uf8ff");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    Order p = doc.toObject(Order.class);
                    OrderList.add(p);
                }

                OrderAdapter = new AllOrderAdapter(getApplicationContext(), OrderList);
                mOrderList.setAdapter(OrderAdapter);
            }
        });
    }

    private void searchData(String s) {

        OrderList = new ArrayList<>();

        Query query = db.collection("AllOrders").orderBy("search").startAt(s).endAt(s+"\uf8ff");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    Order p = doc.toObject(Order.class);
                    OrderList.add(p);
                }

                OrderAdapter = new AllOrderAdapter(getApplicationContext(), OrderList);
                mOrderList.setAdapter(OrderAdapter);
            }
        });

    }
}
