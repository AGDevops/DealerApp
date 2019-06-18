package com.example.dealerapp;

import android.content.Context;
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

import com.example.dealerapp.Utils.ALlItems;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class AllDltItemsListAdapter extends FirestoreRecyclerAdapter<ALlItems, AllDltItemsListAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public AllDltItemsListAdapter(Context mContext, FirestoreRecyclerOptions<ALlItems> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(int id, String userId);

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final ALlItems model) {

        holder.name.setText(model.getItem_name());
        holder.price.setText(String.valueOf(model.getItem_price())+ " Rs");
        holder.available.setText("Available : " + String.valueOf(model.getItem_quantity()));
        holder.mrp.setText(String.valueOf(model.getItem_mrp()));
        holder.discount.setText(String.valueOf(model.getItem_discount()) + " % off");

        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(model.getItem_image()).into(holder.itemImage);

        db = FirebaseFirestore.getInstance();

        holder.deltItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("AllItems").document(model.getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_item_with_desc_layout, viewGroup, false);

        return new AllDltItemsListAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, available, price, discount, mrp;
        private ImageView itemImage;
        private Button deltItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.d_item_name);
            price = itemView.findViewById(R.id.d_item_selling_price);
            itemImage = itemView.findViewById(R.id.d_item_image);
            mrp = itemView.findViewById(R.id.d_item_mrp);
            discount = itemView.findViewById(R.id.d_item_discount);
            deltItem = itemView.findViewById(R.id.d_item_delete_btn);
            available = itemView.findViewById(R.id.d_item_availability);

        }

    }
}
