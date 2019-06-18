package com.example.dealerapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealerapp.Dealers.DescriptionActivity;
import com.example.dealerapp.Utils.ALlItems;
import com.example.dealerapp.Utils.Order;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.OrderViewHolder>{

    Context mCtx;
    List<Order> OrderList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(String userId);

    }
    AllOrderAdapter(Context mCtx, List<Order> OrderList)
    {
        this.mCtx = mCtx;
        this.OrderList = OrderList;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_layout_order,
                parent, false);
        OrderViewHolder OrderViewHolder = new OrderViewHolder(view);
        return OrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        final Order Order = OrderList.get(position);
        holder.price.setText("₹"+Order.getProduct_price());
        holder.orderid.setText(Order.getProduct_id());
        holder.name.setText(Order.getProduct_name());
        holder.quantity.setText("X "+Order.getQuantity());
        holder.status.setText(Order.getStatus());
        Picasso.get().load(Order.getProduct_image()).placeholder(R.drawable.orders).into(holder.imageView);

        final String PushId = OrderList.get(position).getProduct_id();
        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.card_layout.setClickable(false);
                Intent mIntent = new Intent(mCtx, DescriptionActivity.class);
                mIntent.putExtra("Key", PushId);
                mCtx.startActivity(mIntent);
                holder.card_layout.setClickable(true);
            }
        });

        db.collection("AllOrders").document(Order.getPush_id()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.get("status").toString().equals("Pending")){
                        holder.confirm.setText("Confirm");
                        holder.dltOrder.setVisibility(View.GONE);
                    }else {
                        holder.confirm.setText("Confirmed");
                        holder.cancelButton.setVisibility(View.GONE);
                        holder.dltOrder.setVisibility(View.VISIBLE);

                    }
                }

            }
        });

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cancelButton.setClickable(false);
                holder.cancelButton.setText("CANCELED");
                db.collection("Dealers").document(Order.getDealer_id()).collection("Orders").document(Order.getProduct_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            db.collection("AllOrders").document(Order.getPush_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(mCtx,"SUCCESS CANCELED", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status", "Confirmed");

                holder.confirm.setClickable(false);
                holder.confirm.setText("Confirmed");
                db.collection("Dealers").document(Order.getDealer_id()).collection("Orders").document(Order.getProduct_id()).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                        }
                    }
                });

                db.collection("AllOrders").document(Order.getPush_id()).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                SimpleDateFormat month = new SimpleDateFormat("MMM");
                SimpleDateFormat day = new SimpleDateFormat("dd");

                final String year_for = year.format(c);
                final String month_for = month.format(c);
                final String day_for = day.format(c);

                final int day_int = Integer.parseInt(day_for);
                db.collection("Graph").document(year_for).collection(month_for)
                        .document("day"+day_for).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            if (task.isSuccessful()) {
                                HashMap<String, Object> map = new HashMap<>();
                                long y_int = task.getResult().getLong("y_point");
                                long quan_int = Integer.parseInt(Order.getQuantity());
                                long val = y_int + quan_int;
                                map.put("x_point", day_int);
                                map.put("y_point", val);

                                db.collection("Graph").document(year_for).collection(month_for).document("day" + day_for).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(mCtx, "confirmed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            long y = 0;
                            long x = day_int;
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("x_point", day_int);
                            map.put("y_point", 1);
                            db.collection("Graph").document(year_for).collection(month_for).document("day" + day_for).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(mCtx, "confirmed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        holder.dltOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.getPosition(Order.getPush_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return OrderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView, dltOrder;
        TextView name,price,quantity,orderid,status;
        CardView card_layout;
        //AppCompatRatingBar ratingBar;
        Button callButton,confirm,cancelButton,ratingButton;
        public OrderViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.Order_image);

            name = (TextView) itemView.findViewById(R.id.product_name);

            //rating = (TextView) itemView.findViewById(R.id.Order_rating);
            card_layout = (CardView) itemView.findViewById(R.id.card_layout);
            orderid = (TextView) itemView.findViewById(R.id.product_id);
            quantity = (TextView) itemView.findViewById(R.id.product_quantity);
            price = (TextView) itemView.findViewById(R.id.product_price);
            status = (TextView) itemView.findViewById(R.id.status);
            cancelButton = (Button) itemView.findViewById(R.id.cancelButton);
            confirm = (Button) itemView.findViewById(R.id.billButton);
            dltOrder = itemView.findViewById(R.id.delete_order);
            //ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.ratingBarMain);
        }
    }
}
