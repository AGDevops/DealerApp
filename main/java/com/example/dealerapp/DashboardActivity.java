package com.example.dealerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealerapp.Fragments.ALlProductsFrag;
import com.example.dealerapp.Fragments.FilterProductsFrag;
import com.example.dealerapp.Utils.AllOrders;
import com.example.dealerapp.Utils.Categories;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class DashboardActivity extends AppCompatActivity {

    private TextInputEditText categoryInput;
    private Button addBtn;
    private FirebaseFirestore db;
    private ProgressDialog dialog;
    private Spinner spinner;

    private BarChart mBarChart;
    private LineChart mLineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryInput = findViewById(R.id.add_category_input);
        addBtn = findViewById(R.id.add_catogory_btn);
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        spinner = findViewById(R.id.category_list_spinner);


        final TextView graphChange = findViewById(R.id.change_graph_text);
        graphChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (graphChange.getText().equals("SEE LINE GRAPH")){
                    mLineChart.setVisibility(View.VISIBLE);
                    mBarChart.setVisibility(View.INVISIBLE);
                    graphChange.setText("SEE BAR GRAPH");
                }else if (graphChange.getText().equals("SEE BAR GRAPH")){
                    mLineChart.setVisibility(View.INVISIBLE);
                    mBarChart.setVisibility(View.VISIBLE);
                    graphChange.setText("SEE LINE GRAPH");
                }
            }
        });

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

        //--------------graph
        mBarChart = findViewById(R.id.barchart);
        mLineChart = findViewById(R.id.linechart);
//        mChart.setOnChartGestureListener(DashboardActivity.this);
//        mChart.setOnChartValueSelectedListener(DashboardActivity.this);

        mBarChart.setDragEnabled(true);
        mBarChart.setScaleEnabled(false);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat day = new SimpleDateFormat("dd");

        final String year_for = year.format(c);
        final String month_for = month.format(c);
        final String day_for = day.format(c);

        db.collection("Graph").document("2019").collection("Jun").orderBy("x_point").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<BarEntry> yValues = new ArrayList<>();
                ArrayList<Entry> yVal = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    long x = doc.getLong("x_point");
                    long y = doc.getLong("y_point");
                    yValues.add(new BarEntry(x, y));
                    yVal.add(new Entry(x, y));

                    BarDataSet set1 = new BarDataSet(yValues, "Data set 1");
                    LineDataSet set = new LineDataSet(yVal, "Data set 1");

                    set1.setBarBorderWidth(1);
                    set.setFillAlpha(110);

                    ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

                    dataSets1.add(set1);
                    dataSets.add(set);

                    BarData data1 = new BarData(dataSets1);
                    LineData data = new LineData(dataSets);

                    mBarChart.setData(data1);
                    mLineChart.setData(data);
                }
            }
        });

        spinnerClass();
    }

    private void spinnerClass() {

        db.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> cate = new ArrayList<>();
                    cate.add(0, "Select Category");

                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String name = doc.getData().get("item_category").toString();
                        cate.add(name);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, cate);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(adapter);
                }
            }
        });
        spinner.setSelection(0, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String cate = parent.getSelectedItem().toString();
                db.collection("Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            Categories categories = doc.toObject(Categories.class);
                            if (categories.getItem_category().equals(cate)){
                                db.collection("Categories").document(categories.getItem_id()).delete();
                                Toast.makeText(DashboardActivity.this, "category deleted", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
