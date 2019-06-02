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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dealerapp.Utils.AllOrders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.HashMap;

import javax.annotation.Nullable;

public class DashboardActivity extends AppCompatActivity {

    private TextInputEditText categoryInput;
    private Button addBtn;
    private FirebaseFirestore db;
    private ProgressDialog dialog;
    private GraphView graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        categoryInput = findViewById(R.id.add_category_input);
        addBtn = findViewById(R.id.add_catogory_btn);
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cate = categoryInput.getText().toString();
                if (!TextUtils.isEmpty(cate)){
                    dialog.setMessage("adding");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    updateCategory(cate);
                }else {
                    Toast.makeText(DashboardActivity.this, "please type category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        graph = (GraphView) findViewById(R.id.graph);

        db.collection("AllOrders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                DataPoint[] points = new DataPoint[queryDocumentSnapshots.size()];
                int index = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    AllOrders orders = documentSnapshot.toObject(AllOrders.class);

                    int x = Integer.parseInt(orders.getX_point());
                    int y = Integer.parseInt(orders.getY_point());
                    points[index] = new DataPoint(x, y);
                    index++;
                }
            }
        });
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 16)
        });
        graph.addSeries(series);


    }

    private void updateCategory(String cate) {

        String uid = db.collection("Categories").document().getId().toString();
        HashMap<String, Object> addMap = new HashMap<>();
        addMap.put("item_category", cate);
        addMap.put("item_id", uid);

        db.collection("Categories").document(uid).set(addMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                    Toast.makeText(DashboardActivity.this, "successful", Toast.LENGTH_SHORT).show();
                }else {
                    dialog.hide();
                    Toast.makeText(DashboardActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
