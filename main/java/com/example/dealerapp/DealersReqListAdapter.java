package com.example.dealerapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealerapp.Utils.Users;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DealersReqListAdapter extends FirestoreRecyclerAdapter<Users, DealersReqListAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public DealersReqListAdapter(Context mContext, FirestoreRecyclerOptions<Users> options) {
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
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Users model) {

        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(model.getProfile_thumbUrl()).placeholder(R.drawable.avatar).into(holder.userImage);
        holder.name.setText(model.getUser_name());
        holder.email.setText(model.getUser_email());

        db = FirebaseFirestore.getInstance();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Dealers").document(model.getuId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.dealer_single_item, viewGroup, false);

        return new DealersReqListAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, email;
        private CircleImageView userImage;
        private Button delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.req_dealer_name);
            email = itemView.findViewById(R.id.req_dealer_email);
            userImage = itemView.findViewById(R.id.req_dealer_image);
            delete = itemView.findViewById(R.id.delete_dealer_btn);

        }

    }
}
