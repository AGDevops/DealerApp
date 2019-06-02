package com.example.dealerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dealerapp.Fragments.ALlProductsFrag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private Toolbar mToolbar;
    private CircleImageView profileImage, profileDrawerImage;
    private FirebaseFirestore db;
    private String currentUserId;
    private NavigationView navigationView;

    private CardView allItemCard, addItemCard, couponCard, dealerCard, dashboardCard;
    private String pushId;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);

        mToolbar = findViewById(R.id.main_layout_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        drawerLayout = findViewById(R.id.drawer_layout);
        profileImage = findViewById(R.id.main_profile_image);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);

        profileDrawerImage = (CircleImageView)headerview.findViewById(R.id.header_profile_image);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.openDrawer(Gravity.START);
                }else {
                    drawerLayout.closeDrawer(Gravity.END);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        allItemCard = findViewById(R.id.all_items_card);
        addItemCard = findViewById(R.id.add_item_card);
        couponCard = findViewById(R.id.coupon_card);
        dealerCard = findViewById(R.id.all_dealers_card);
        dashboardCard = findViewById(R.id.dashboard_card);
        dashboardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            }
        });
        dealerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllDealersActivity.class));

            }
        });
        couponCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CouponsActivity.class));

            }
        });

        allItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllItemsActivity.class));
            }
        });

        addItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setMessage("please wait");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                HashMap<String, Object> addMap = new HashMap<>();
                addMap.put("item_image", "default");
                addMap.put("item_name", "Product Name");
                addMap.put("item_desc", "Product Description");
                addMap.put("item_brand", "Product Brand");
                addMap.put("item_price", "Product price");
                addMap.put("item_mrp", "Product Discount Price");

                pushId = db.collection("AllItems").document().getId().toString();

                db.collection("AllItems").document(pushId).set(addMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                            intent.putExtra("push_id", pushId);
                            startActivity(intent);

                        }
                        else {
                            dialog.hide();
                            Toast.makeText(MainActivity.this, "failed to adding item please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null){
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return onOptionsItemSelected(item);
    }

}
