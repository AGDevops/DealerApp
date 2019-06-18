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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealerapp.Fragments.ALlProductsFrag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.zip.Inflater;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private Toolbar mToolbar;
    private CircleImageView profileImage, profileDrawerImage;
    private TextView headerUserName, headerUserEmail, headerUserType;
    private FirebaseFirestore db;
    private String currentUserId;
    private NavigationView navigationView;

    private CardView allItemCard, addItemCard, couponCard, dealerCard, dashboardCard, remItemCard, allOrdersCard;
    private ProgressDialog dialog;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        firebaseUser = mAuth.getCurrentUser();


        mToolbar = findViewById(R.id.main_layout_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        drawerLayout = findViewById(R.id.drawer_layout);
        profileImage = findViewById(R.id.main_profile_image);
        headerUserType = findViewById(R.id.header_user_type);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);

        profileDrawerImage = (CircleImageView)headerview.findViewById(R.id.header_profile_image);
        headerUserEmail = (TextView)headerview.findViewById(R.id.header_user_email);
        headerUserName = (TextView)headerview.findViewById(R.id.header_user_name);
        headerUserType = (TextView) headerview.findViewById(R.id.header_user_type);

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

                        if (id == R.id.profile_item){
                            startActivity(new Intent(MainActivity.this, OwnerProfileActivity.class));
                        }
                        else if (id == R.id.dealer_item){
                            startActivity(new Intent(MainActivity.this, AllDealersActivity.class));

                        }else if (id == R.id.coupons_item){
                            startActivity(new Intent(MainActivity.this, CouponsActivity.class));

                        }else if (id == R.id.seller_item){
                            startActivity(new Intent(MainActivity.this, SellersActivity.class));

                        }else if (id == R.id.location_item){
                            startActivity(new Intent(MainActivity.this, LocationActivity.class));

                        }else if (id == R.id.all_orders_item){
                            startActivity(new Intent(MainActivity.this, AllOrdersActivity.class));

                        }else if (id == R.id.setting_item){
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));

                        }else if (id == R.id.remove_item){
                            startActivity(new Intent(MainActivity.this, RemProductActivity.class));

                        }else if (id == R.id.add_item){
                            addingItem();

                        }else if (id == R.id.all_items){
                            startActivity(new Intent(MainActivity.this, AllItemsActivity.class));

                        }else if (id == R.id.dashboard_item){
                            startActivity(new Intent(MainActivity.this, DashboardActivity.class));

                        }else if (id == R.id.logout_item){
                            mAuth.signOut();
                            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                            startActivity(intent);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    }
                });

        //----------fetching profile
        if (firebaseUser != null){
            db.collection("Users").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()){
                        String profile = documentSnapshot.get("profile_thumbUrl").toString();
                        String name = documentSnapshot.get("user_name").toString();
                        String email = documentSnapshot.get("user_email").toString();
                        String type = documentSnapshot.get("type").toString();

                        if (type.equals("Dealer")){
                            startActivity(new Intent(MainActivity.this, DealerHomeActivity.class));
                            finish();
                        }

                        headerUserEmail.setText(email);
                        headerUserName.setText(name);
                        headerUserType.setText(type);
                        Picasso picasso = Picasso.get();
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(profile).placeholder(R.drawable.avatar).into(profileImage);
                        picasso.load(profile).placeholder(R.drawable.avatar).into(profileDrawerImage);


                    }else {
                        db.collection("Dealers").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                if (documentSnapshot.exists()){
                                    startActivity(new Intent(MainActivity.this, DealerHomeActivity.class));
                                    finish();
                                }
                            }
                        });
                    }
                }
            });
        }

        allItemCard = findViewById(R.id.all_items_card);
        addItemCard = findViewById(R.id.add_item_card);
        couponCard = findViewById(R.id.coupon_card);
        dealerCard = findViewById(R.id.all_dealers_card);
        dashboardCard = findViewById(R.id.dashboard_card);
        remItemCard = findViewById(R.id.remove_item_card);
        allOrdersCard = findViewById(R.id.all_order_card);
        allOrdersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllOrdersActivity.class));

            }
        });
        remItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RemProductActivity.class));

            }
        });
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

                addingItem();

            }
        });

    }

    private void addingItem() {

        dialog.setMessage("please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final String pushId = db.collection("AllItems").document().getId();

        HashMap<String, Object> addMap = new HashMap<>();
        addMap.put("item_image", "default");
        addMap.put("item_name", "Product Name");
        addMap.put("item_desc", "Product Description");
        addMap.put("item_brand", "Product Brand");
        addMap.put("item_price", "Product price");
        addMap.put("item_mrp", "Product Discount Price");
        addMap.put("uid", pushId);
        addMap.put("item_id", pushId);

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

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser == null){
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu_items, menu);
        return true;
    }

}
