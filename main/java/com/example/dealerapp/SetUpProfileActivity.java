package com.example.dealerapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetUpProfileActivity extends Fragment {

    private TextView selectText;

    public SetUpProfileActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_up_profile, container, false);

        selectText = view.findViewById(R.id.select_who_text);
        selectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFrag fragment = new BottomSheetFrag();
                fragment.show(getFragmentManager(), "TAG");
            }
        });

        return view;
    }

}
