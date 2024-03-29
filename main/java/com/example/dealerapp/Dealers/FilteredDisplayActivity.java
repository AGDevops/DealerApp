package com.example.dealerapp.Dealers;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.dealerapp.ProductAdapter;
import com.example.dealerapp.R;
import com.example.dealerapp.Utils.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilteredDisplayActivity extends AppCompatActivity {

    RecyclerView mProductList;
    List<Product> ProductList;
    ProductAdapter productAdapter;
    ProgressDialog pd;
    ImageButton searchButton;
    EditText searchtext;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_display);

        final HashMap<String, Integer> hashMapObject = (HashMap<String, Integer>) getIntent().getSerializableExtra("map");

        pd = new ProgressDialog(FilteredDisplayActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();



        mProductList = (RecyclerView) findViewById(R.id.product_list);
        mProductList.setHasFixedSize(true);
        mProductList.setLayoutManager(new GridLayoutManager(this, 2));

        ProductList = new ArrayList<>();

        db.collection("AllItems").whereGreaterThan("item_price",hashMapObject.get("price_low")).whereLessThan("item_price",hashMapObject.get("price_high")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Product p = doc.toObject(Product.class);
                        ProductList.add(p);
                    }

                    productAdapter = new ProductAdapter(FilteredDisplayActivity.this, ProductList);
                    mProductList.setAdapter(productAdapter);

                    //If ProgressDialog is showing Dismiss it
                    if(pd.isShowing())
                    {
                        pd.dismiss();
                    }

                }
            }
        });
    }
}
