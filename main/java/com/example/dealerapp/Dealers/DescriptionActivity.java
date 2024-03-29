package com.example.dealerapp.Dealers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DescriptionActivity extends AppCompatActivity {

    ViewPager viewPager;
    int images[] = {R.drawable.products,R.drawable.avatar};
    MyCustomPagerAdapter myCustomPagerAdapter;
    ProgressDialog pd;
    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;
    TextView name,price,mrp,brand,quantity,description,savings;
    Button orderButton,cartButton;
    ImageButton addQ,delQ;
    LinearLayout description_view;
    HashMap<String,String> hashMap;
    String product_name,product_mrp,product_price,product_brand,image,product_id,product_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        setUpToolBar();


        pd = new ProgressDialog(DescriptionActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();


        description_view = (LinearLayout) findViewById(R.id.description_view);
        description_view.setVisibility(View.GONE);
        orderButton = (Button) findViewById(R.id.orderButton);
        cartButton = (Button) findViewById(R.id.cartButton);

        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        mrp = (TextView) findViewById(R.id.mrp);
        brand = (TextView) findViewById(R.id.brand);
        savings = (TextView) findViewById(R.id.saving);

        addQ = (ImageButton) findViewById(R.id.add_q);
        delQ = (ImageButton) findViewById(R.id.del_q);
        quantity = (TextView) findViewById(R.id.quantity);
        description = (TextView) findViewById(R.id.description);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        myCustomPagerAdapter = new MyCustomPagerAdapter(DescriptionActivity.this, images);
        viewPager.setAdapter(myCustomPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = Integer.parseInt(quantity.getText().toString());
                if(val > 0)
                    quantity.setText(String.valueOf(val + 1));
            }
        });
        delQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = Integer.parseInt(quantity.getText().toString());
                if(val > 1)
                    quantity.setText(String.valueOf(val - 1));
            }
        });

        String key = getIntent().getStringExtra("Key");

        db.collection("AllItems").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    pd.dismiss();

                    DocumentSnapshot documentSnapshot = task.getResult();
                    product_name = documentSnapshot.get("item_name").toString();
                    product_mrp = documentSnapshot.get("item_mrp").toString();
                    product_price = documentSnapshot.get("item_price").toString();
                    product_brand = documentSnapshot.get("item_brand").toString();
                    image = documentSnapshot.get("item_image").toString();
                    product_id = documentSnapshot.get("item_id").toString();
                    product_description = documentSnapshot.get("item_desc").toString();

                    name.setText(product_name);
                    brand.setText(product_brand);
                    mrp.setText("₹"+product_mrp);
                    mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    price.setText("₹"+product_price);
                    description.setText(product_description);
                    savings.setText(String.valueOf(Integer.parseInt(product_mrp) - Integer.parseInt(product_price)));
                    description_view.setVisibility(View.VISIBLE);
                }
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartButton.setClickable(false);
                pd = new ProgressDialog(DescriptionActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(true);
                pd.setTitle("Loading....");
                pd.setMessage("Please Wait");
                pd.show();

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                SimpleDateFormat month = new SimpleDateFormat("MMM");
                final String year_for = year.format(c);
                final String month_for = month.format(c);


                hashMap = new HashMap<String, String>();
                hashMap.put("product_name",name.getText().toString());
                hashMap.put("dealer_id",firebaseUser.getUid());
                hashMap.put("product_id",product_id);
                hashMap.put("status","Pending");
                hashMap.put("product_name",product_name);
                hashMap.put("product_price",String.valueOf(Integer.parseInt(product_price) * Integer.parseInt(quantity.getText().toString())));
                hashMap.put("product_image",image);
                hashMap.put("quantity",quantity.getText().toString());
                hashMap.put("year", year_for);
                hashMap.put("month", month_for);
                hashMap.put("search", product_name.toLowerCase());

                db.collection("Dealers").document(firebaseUser.getUid()).collection("Cart").document(product_id).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            String message = "You just received an order from "+firebaseUser.getUid().toString()+ " \n\nPRODUCT NAME : "+product_name + "\nQuantity : "+quantity.getText().toString();
                            //SendMail sm = new SendMail(DescriptionActivity.this, "devashisht2914@gmail.com", "ORDER FROM "+firebaseUser.getUid().toString(), message);
                            //Executing sendmail to send email
                            //sm.execute();
                            Toast.makeText(DescriptionActivity.this,"ADDED TO CART",Toast.LENGTH_LONG).show();
                            pd.dismiss();
                            //Intent intent = new Intent(DescriptionActivity.this,OrderConfirmedActivity.class);
                            //startActivity(intent);
                            //finish();
                        }
                        else
                        {
                            pd.dismiss();
                            cartButton.setClickable(true);
                            Toast.makeText(DescriptionActivity.this,"FAIL",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderButton.setClickable(false);
                pd = new ProgressDialog(DescriptionActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(true);
                pd.setTitle("Loading....");
                pd.setMessage("Please Wait");
                pd.show();

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                String push = db.collection("AllOrders").document().getId();

                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                SimpleDateFormat month = new SimpleDateFormat("MMM");
                final String year_for = year.format(c);
                final String month_for = month.format(c);

                hashMap = new HashMap<String, String>();
                hashMap.put("product_name",name.getText().toString());
                hashMap.put("dealer_id",firebaseUser.getUid());
                hashMap.put("product_id",product_id);
                hashMap.put("status","Pending");
                hashMap.put("product_name",product_name);
                hashMap.put("product_price",product_price);
                hashMap.put("product_image",image);
                hashMap.put("quantity",quantity.getText().toString());
                hashMap.put("year", year_for);
                hashMap.put("month", month_for);
                hashMap.put("push_id", push);
                hashMap.put("search", product_name.toLowerCase());

                db.collection("AllOrders").document(push).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            db.collection("Users").document(firebaseUser.getUid().toString()).collection("Orders").document(product_id).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    String message = "You just received an order from "+firebaseUser.getUid().toString()+ " \n\nPRODUCT NAME : "+product_name + "\nQuantity : "+quantity.getText().toString();
//                                    SendMail sm = new SendMail(DescriptionActivity.this, "devashisht2914@gmail.com", "ORDER FROM "+firebaseUser.getUid().toString(), message);
//                                    //Executing sendmail to send email
//                                    sm.execute();
                                    Toast.makeText(DescriptionActivity.this,"SUCCESS",Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                    Intent intent = new Intent(DescriptionActivity.this,OrderConfirmedActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else
                        {
                            pd.dismiss();
                            orderButton.setClickable(true);
                            Toast.makeText(DescriptionActivity.this,"FAIL",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    void setUpToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
