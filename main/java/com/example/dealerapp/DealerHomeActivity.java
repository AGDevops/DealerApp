package com.example.dealerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dealerapp.Dealers.CartActivity;
import com.example.dealerapp.Dealers.Category;
import com.example.dealerapp.Dealers.CategoryAdapter;
import com.example.dealerapp.Dealers.DescriptionActivity;
import com.example.dealerapp.Dealers.FilterActivity;
import com.example.dealerapp.Dealers.OrdersActivity;
import com.example.dealerapp.Dealers.ProfileActivity;
import com.example.dealerapp.Utils.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class DealerHomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    RecyclerView mProductList;
    List<Product> ProductList;
    ProductAdapter productAdapter;

    RecyclerView mCategoryList;
    List<Category> CategoryList;
    com.example.dealerapp.Dealers.CategoryAdapter CategoryAdapter;
    CardView sortCard,filterCard,categoryCard;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog pd;
    private FirebaseAuth mAuth;
    String sort_choice = "";
    private CircleImageView profileToolImage, profileDrawerImage;
    private TextView headerUserEmail, headerUserName, headerUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_home);

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();

        Toolbar  toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        profileToolImage = findViewById(R.id.main_profile_image_dealer);
        sortCard = (CardView) findViewById(R.id.sort_card);
        filterCard = (CardView) findViewById(R.id.filter_card);
        categoryCard = (CardView) findViewById(R.id.categoryCard);


        mAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerview = navigationView.getHeaderView(0);
        drawerLayout = findViewById(R.id.drawerlayout);

        profileDrawerImage = (CircleImageView)headerview.findViewById(R.id.header_profile_image);
        headerUserEmail = (TextView)headerview.findViewById(R.id.header_user_email);
        headerUserName = (TextView)headerview.findViewById(R.id.header_user_name);
        headerUserType = (TextView) headerview.findViewById(R.id.header_user_type);

        navigationView.setItemIconTintList(null);

        profileToolImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.openDrawer(Gravity.START);
                }else {
                    drawerLayout.closeDrawer(Gravity.END);
                }
            }
        });

        if (mAuth.getCurrentUser() != null){
            db.collection("Users").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (documentSnapshot.exists()){
                        String profile = documentSnapshot.get("profile_thumbUrl").toString();
                        String name = documentSnapshot.get("user_name").toString();
                        String email = documentSnapshot.get("user_email").toString();
                        String type = documentSnapshot.get("type").toString();
                        headerUserEmail.setText(email);
                        headerUserName.setText(name);
                        headerUserType.setText(type);
                        Picasso picasso = Picasso.get();
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(profile).placeholder(R.drawable.avatar).into(profileToolImage);
                        picasso.load(profile).placeholder(R.drawable.avatar).into(profileDrawerImage);
                    }
                }
            });

            db.collection("Dealers").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (documentSnapshot.exists()){
                        String profile = documentSnapshot.get("profile_thumbUrl").toString();
                        String name = documentSnapshot.get("user_name").toString();
                        String email = documentSnapshot.get("user_email").toString();
                        String type = documentSnapshot.get("type").toString();
                        headerUserEmail.setText(email);
                        headerUserName.setText(name);
                        headerUserType.setText(type);
                        Picasso picasso = Picasso.get();
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(profile).placeholder(R.drawable.avatar).into(profileToolImage);
                        picasso.load(profile).placeholder(R.drawable.avatar).into(profileDrawerImage);

                    }
                }
            });
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case (R.id.home):
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(DealerHomeActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case (R.id.profile):
                        drawerLayout.closeDrawers();
                        Intent intentPro = new Intent(DealerHomeActivity.this, ProfileActivity.class);
                        startActivity(intentPro);
                        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case (R.id.Orders):
                        drawerLayout.closeDrawers();
                        Intent intentCategory = new Intent(DealerHomeActivity.this, OrdersActivity.class);
                        startActivity(intentCategory);
                        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case (R.id.logout_dealer):
                        drawerLayout.closeDrawers();
                        mAuth.signOut();
                        Intent intent1 = new Intent(DealerHomeActivity.this, AuthActivity.class);
                        startActivity(intent1);
                        break;

                }
                return false;
            }
        });

        mProductList = (RecyclerView) findViewById(R.id.product_list);
        mProductList.setHasFixedSize(true);
        mProductList.setLayoutManager(new GridLayoutManager(this, 2));
        final TextView textView = findViewById(R.id.profile_review_text);

        db.collection("Dealers").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshot.exists()){
                    mProductList.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }else {
                    mProductList.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                }
            }
        });

        ProductList = new ArrayList<>();

        db.collection("AllItems").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Product p = doc.toObject(Product.class);
                        ProductList.add(p);
                    }

                    productAdapter = new ProductAdapter(DealerHomeActivity.this, ProductList);
                    mProductList.setAdapter(productAdapter);
                    productAdapter.setOnItemClick(new ProductAdapter.OnItemClick() {
                        @Override
                        public void getPosition(String key) {
                            Intent mIntent = new Intent(DealerHomeActivity.this, DescriptionActivity.class);
                            mIntent.putExtra("Key", key);
                            startActivity(mIntent);
                        }
                    });

                    //If ProgressDialog is showing Dismiss it
                    if(pd.isShowing())
                    {
                        pd.dismiss();
                    }

                }
            }
        });

        mCategoryList = (RecyclerView) findViewById(R.id.category_list);
        mCategoryList.setHasFixedSize(false);
        mCategoryList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        CategoryList = new ArrayList<>();

        db.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Category p = doc.toObject(Category.class);
                        CategoryList.add(p);
                    }

                    CategoryAdapter = new CategoryAdapter(DealerHomeActivity.this, CategoryList);
                    mCategoryList.setAdapter(CategoryAdapter);

                    //If ProgressDialog is showing Dismiss it
                    if(pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    CategoryAdapter.setOnItemClick(new CategoryAdapter.OnItemClick() {
                        @Override
                        public void getPosition(String push_id) {
                            ProductList = new ArrayList<>();

                            db.collection("AllItems").whereEqualTo("item_category", push_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot doc : task.getResult()){
                                            Product p = doc.toObject(Product.class);
                                            ProductList.add(p);
                                        }

                                        productAdapter = new ProductAdapter(DealerHomeActivity.this, ProductList);
                                        mProductList.setAdapter(productAdapter);
                                        productAdapter.setOnItemClick(new ProductAdapter.OnItemClick() {
                                            @Override
                                            public void getPosition(String key) {
                                                Intent mIntent = new Intent(DealerHomeActivity.this, DescriptionActivity.class);
                                                mIntent.putExtra("Key", key);
                                                startActivity(mIntent);
                                            }
                                        });

                                    }
                                }
                            });

                        }
                    });

                }
            }
        });

        filterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DealerHomeActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });

        sortCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] listitem = new String[]{"High to Low","Low to High"};
                AlertDialog.Builder sort_alert= new AlertDialog.Builder(DealerHomeActivity.this);
                sort_alert.setTitle("Sort by price");
                sort_alert.setIcon(R.drawable.sort);
                sort_alert.setSingleChoiceItems(listitem, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ProductList.clear();
                        pd = new ProgressDialog(DealerHomeActivity.this);
                        pd.setCanceledOnTouchOutside(false);
                        pd.setCancelable(true);
                        pd.setTitle("Loading....");
                        pd.setMessage("Please Wait");
                        pd.show();
                        db.collection("AllItems").orderBy("item_price", listitem[which]=="Low to High" ? Query.Direction.ASCENDING : Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot doc : task.getResult()){
                                        Product p = doc.toObject(Product.class);
                                        ProductList.add(p);
                                    }

                                    productAdapter = new ProductAdapter(DealerHomeActivity.this, ProductList);
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
                });
                sort_alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog ad = sort_alert.create();
                ad.show();
            }
        });

        categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCategoryList.isShown()){
                    mCategoryList.setVisibility(View.GONE);
                    ProductList = new ArrayList<>();

                    db.collection("AllItems").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot doc : task.getResult()){
                                    Product p = doc.toObject(Product.class);
                                    ProductList.add(p);
                                }

                                productAdapter = new ProductAdapter(DealerHomeActivity.this, ProductList);
                                mProductList.setAdapter(productAdapter);
                                productAdapter.setOnItemClick(new ProductAdapter.OnItemClick() {
                                    @Override
                                    public void getPosition(String key) {
                                        Intent mIntent = new Intent(DealerHomeActivity.this, DescriptionActivity.class);
                                        mIntent.putExtra("Key", key);
                                        startActivity(mIntent);
                                    }
                                });

                            }
                        }
                    });
                }
                else{
                    mCategoryList.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    void setUpToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarDrawerToggle =  new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu,menu);
        MenuItem item = menu.findItem(R.id.search);
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

    private void searchLiveData(String s) {

        ProductList = new ArrayList<>();

        Query query = db.collection("AllItems").orderBy("search").startAt(s).endAt(s + "\uf8ff");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Product p = doc.toObject(Product.class);
                    ProductList.add(p);
                }

                productAdapter = new ProductAdapter(DealerHomeActivity.this, ProductList);
                mProductList.setAdapter(productAdapter);
                productAdapter.setOnItemClick(new ProductAdapter.OnItemClick() {
                    @Override
                    public void getPosition(String key) {
                        Intent mIntent = new Intent(DealerHomeActivity.this, DescriptionActivity.class);
                        mIntent.putExtra("Key", key);
                        startActivity(mIntent);
                    }
                });

                //If ProgressDialog is showing Dismiss it
                if (pd.isShowing()) {
                    pd.dismiss();
                }

            }
        });
    }


        private void searchData (String s){

            Query query = db.collection("AllItems").orderBy("search").startAt(s).endAt(s + "\uf8ff");

            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Product p = doc.toObject(Product.class);
                        ProductList.add(p);
                    }

                    productAdapter = new ProductAdapter(DealerHomeActivity.this, ProductList);
                    mProductList.setAdapter(productAdapter);
                    productAdapter.setOnItemClick(new ProductAdapter.OnItemClick() {
                        @Override
                        public void getPosition(String key) {
                            Intent mIntent = new Intent(DealerHomeActivity.this, DescriptionActivity.class);
                            mIntent.putExtra("Key", key);
                            startActivity(mIntent);
                        }
                    });

                    //If ProgressDialog is showing Dismiss it
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }

                }
            });
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.search):
//                Intent intent = new Intent(DealerHomeActivity.this,SearchActivity.class);
//                startActivity(intent);
                break;
            case (R.id.cart):
                Intent intentC = new Intent(DealerHomeActivity.this, CartActivity.class);
                startActivity(intentC);
                break;
        }
        return true;
    }

}
