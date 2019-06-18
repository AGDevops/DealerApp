package com.example.dealerapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccFragment extends Fragment {

    private Button createAcc;
    private TextInputEditText userName, emailAddress, password, confmPassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog dialog;
    private String currentUserId;

    public CreateAccFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_acc, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(getActivity());

        createAcc = view.findViewById(R.id.create_account_btn);
        userName = (TextInputEditText) view.findViewById(R.id.sign_up_user_name);
        emailAddress = view.findViewById(R.id.sign_up_email_address);
        password = view.findViewById(R.id.sign_up_password);
        confmPassword = view.findViewById(R.id.sign_up_cnfm_password);


        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = userName.getText().toString();
                String email = emailAddress.getText().toString();
                String pass = password.getText().toString();
                String cnfmPass = confmPassword.getText().toString();

                if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(email) &&
                        !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(cnfmPass)){
                    if (pass.equals(cnfmPass)){
                        dialog.setMessage("Creating account...");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        creatingAccount(email, pass, user_name, view);
                    }else {
                        Toast.makeText(getActivity(), "password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "please fill all the credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void creatingAccount(final String email, String pass, final String user_name, final View view) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    currentUserId = mAuth.getCurrentUser().getUid();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("user_name", user_name);
                    hashMap.put("profile_pic", "default");
                    hashMap.put("user_email", email);
                    hashMap.put("type", "Who are you?");
                    hashMap.put("uId", currentUserId);



                    db.collection("Users").document(currentUserId).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dialog.dismiss();
                                Intent intent = new Intent(getActivity(), SetProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else {
                                dialog.hide();
                                Toast.makeText(getActivity(), "something is wrong please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
