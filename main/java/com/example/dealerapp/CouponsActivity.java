package com.example.dealerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class CouponsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        Toolbar toolbar = findViewById(R.id.coupons_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Send Coupon");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
