package com.example.dealerapp.Dealers;

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

import com.example.dealerapp.R;
import com.example.dealerapp.Utils.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    Context mCtx;
    List<Order> OrderList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CartAdapter(Context mCtx, List<Order> OrderList)
    {
        this.mCtx = mCtx;
        this.OrderList = OrderList;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_cart_layout,
                parent, false);
        CartViewHolder CartViewHolder = new CartViewHolder(view);
        return CartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        final Order Order = OrderList.get(position);
        holder.price.setText("₹"+Order.getProduct_price());
        holder.orderid.setText(Order.getProduct_id());
        holder.name.setText(Order.getProduct_name());
        holder.quantity.setText("X "+Order.getQuantity());
        String status = Order.getStatus().toString();
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

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cancelButton.setClickable(false);
                holder.cancelButton.setText("CANCELED");
                db.collection("Users").document(Order.getDealer_id()).collection("Cart").document(Order.getProduct_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(mCtx,"SUCCESS DELETED", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return OrderList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView name,price,quantity,orderid,status;
        CardView card_layout;
        //AppCompatRatingBar ratingBar;
        Button callButton,billButton,cancelButton,ratingButton;
        public CartViewHolder(View itemView) {
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
            //ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.ratingBarMain);
        }
    }
}
