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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class OwnerInfoActivity extends AppCompatActivity {

    private TextInputEditText firstName, lastName, companyName, mobNo, add1, add2;
    private Button finishBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_info);

        firstName = findViewById(R.id.personal_first_name);
        lastName = findViewById(R.id.personal_last_name);
        companyName = findViewById(R.id.personal_company_name);
        mobNo = findViewById(R.id.personal_mobile_number);
        add1 = findViewById(R.id.personal_address_line1);
        add2 = findViewById(R.id.personal_address_line2);
        finishBtn = findViewById(R.id.finish_btn);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String currentUser = mAuth.getCurrentUser().getUid();

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = firstName.getText().toString();
                String last = lastName.getText().toString();
                String company = companyName.getText().toString();
                String mob = mobNo.getText().toString();
                String address1 = add1.getText().toString();
                String address2 = add2.getText().toString();

                if (!TextUtils.isEmpty(first) &&!TextUtils.isEmpty(last) &&!TextUtils.isEmpty(company) &&
                        !TextUtils.isEmpty(mob) &&!TextUtils.isEmpty(address1) &&!TextUtils.isEmpty(address2)){

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("first_name", first);
                    map.put("last_name", last);
                    map.put("company", company);
                    map.put("mob", mob);
                    map.put("address1", address1);
                    map.put("address2", address2);

                    db.collection("Users").document(currentUser).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(OwnerInfoActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(OwnerInfoActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(OwnerInfoActivity.this, "fill all the credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
