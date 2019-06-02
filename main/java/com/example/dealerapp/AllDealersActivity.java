package com.example.dealerapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.dealerapp.Fragments.MyPageAdapter;

public class AllDealersActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager mainPager;
    private MyPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_dealers);

        Toolbar toolbar = findViewById(R.id.dealer_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dealers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new MyPageAdapter(getSupportFragmentManager());

        tabLayout = findViewById(R.id.dealer_tablayout);
        mainPager = findViewById(R.id.dealer_viewpager);
        tabLayout.setupWithViewPager(mainPager);
        mainPager.setAdapter(adapter);

    }
}
