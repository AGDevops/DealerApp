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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    private TextInputEditText emailInput, passInput;
    private Button signBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;


    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailInput = view.findViewById(R.id.sign_in_email_address);
        passInput = view.findViewById(R.id.sign_in_password);
        signBtn = view.findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getActivity());

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String pass = passInput.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

                    dialog.setMessage("Logging");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    signInUser(email, pass);

                }else {
                    Toast.makeText(getActivity(), "please fill all the credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void signInUser(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    dialog.hide();
                    Toast.makeText(getActivity(), "error please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
