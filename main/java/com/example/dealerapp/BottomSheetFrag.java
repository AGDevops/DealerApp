package com.example.dealerapp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetFrag extends BottomSheetDialogFragment {


    private RelativeLayout companyOwner, dealer;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(view);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    String state = "";

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            state = "DRAGGING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            state = "SETTLING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            state = "EXPANDED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            state = "COLLAPSED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            dismiss();
                            state = "HIDDEN";
                            break;
                        }
                    }

                    Toast.makeText(getContext(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        companyOwner = view.findViewById(R.id.company_owner_relative);
        dealer = view.findViewById(R.id.dealer_relative);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String currentUser = mAuth.getCurrentUser().getUid();
        dialog = new ProgressDialog(getActivity());

        companyOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("please wait");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                HashMap<String, Object> map = new HashMap<>();
                map.put("type", "Owner");
                db.collection("Users").document(currentUser).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "selected", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dismiss();
                        }else {
                            dialog.hide();
                            Toast.makeText(getActivity(), "error in selecting type please try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        dealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("please wait");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                HashMap<String, Object> map = new HashMap<>();
                map.put("type", "Dealer");
                db.collection("Users").document(currentUser).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "selected", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dismiss();
                        }else {
                            dialog.hide();
                            Toast.makeText(getActivity(), "error in selecting type please try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });



        return view;
    }
}
