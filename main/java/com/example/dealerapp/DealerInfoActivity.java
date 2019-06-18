package com.example.dealerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dealerapp.Utils.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

import javax.annotation.Nullable;

public class DealerInfoActivity extends AppCompatActivity {

    private TextInputEditText firstName, lastName, mobNo, add1, add2;
    private Button finishBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_info);

        firstName = findViewById(R.id.personal_dealer_first_name);
        lastName = findViewById(R.id.personal_dealer_last_name);
        mobNo = findViewById(R.id.personal_dealer_mobile_number);
        add1 = findViewById(R.id.personal_dealer_address_line1);
        add2 = findViewById(R.id.personal_dealer_address_line2);
        finishBtn = findViewById(R.id.finish_dealer_btn);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String currentUser = mAuth.getCurrentUser().getUid();

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String first = firstName.getText().toString();
                String last = lastName.getText().toString();
                final String mob = mobNo.getText().toString();
                final String address1 = add1.getText().toString();
                final String address2 = add2.getText().toString();

                if (!TextUtils.isEmpty(first) &&!TextUtils.isEmpty(last) &&
                        !TextUtils.isEmpty(mob) &&!TextUtils.isEmpty(address1) &&!TextUtils.isEmpty(address2)){

                    final HashMap<String, Object> map = new HashMap<>();
                    map.put("first_name", first);
                    map.put("last_name", last);
                    map.put("company", "default");
                    map.put("mob", mob);
                    map.put("address1", address1);
                    map.put("address2", address2);
                    map.put("type", "Dealer");


                    db.collection("Users").document(currentUser).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(DealerInfoActivity.this, DealerHomeActivity.class);
                                    startActivity(intent);
                                }
                            }else {
                                Toast.makeText(DealerInfoActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(DealerInfoActivity.this, "fill all the credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
