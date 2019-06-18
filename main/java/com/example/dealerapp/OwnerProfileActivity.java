package com.example.dealerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.dealerapp.Utils.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView profileImage;
    private TextView userName, userEmail;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);

        toolbar = findViewById(R.id.owner_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        profileImage = findViewById(R.id.owner_profile_user_image);
        userName = findViewById(R.id.owner_profile_user_name);
        userEmail = findViewById(R.id.owner_profile_user_email);

        db.collection("Users").document(currentUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Users users = documentSnapshot.toObject(Users.class);
                Picasso picasso = Picasso.get();
                picasso.load(users.getProfile_thumbUrl()).placeholder(R.drawable.avatar)
                        .into(profileImage);
                picasso.setIndicatorsEnabled(false);

                userName.setText(users.getUser_name());
                userEmail.setText(users.getUser_email());
            }
        });

    }
}
