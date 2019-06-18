package com.example.dealerapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealerapp.Utils.Users;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllDealersListAdapter extends FirestoreRecyclerAdapter<Users, AllDealersListAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public AllDealersListAdapter(Context mContext, FirestoreRecyclerOptions<Users> options) {
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

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.approve.setText("Approved");
                HashMap<String, Object> map = new HashMap();
                map.put("address1", model.getAddress1());
                map.put("address2", model.getAddress2());
                map.put("gst", "your GST number");
                map.put("company", model.getCompany());
                map.put("first_name", model.getFirst_name());
                map.put("last_name", model.getLast_name());
                map.put("mob", model.getMob());
                map.put("profile_pic", model.getProfile_pic());
                map.put("profile_thumbUrl", model.getProfile_thumbUrl());
                map.put("type", model.getType());
                map.put("uId", model.getuId());
                map.put("user_email", model.getUser_email());
                map.put("user_name", model.getUser_name());

                db.collection("Dealers").document(model.getuId()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext, "approved", Toast.LENGTH_SHORT).show();
                            db.collection("Users").document(model.getuId()).delete();
                        }
                    }
                });

            }
        });

        holder.cancelReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").document(model.getuId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+91"+model.getMob();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                mContext.startActivity(intent);
            }
        });


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dealer_single_req_item, viewGroup, false);

        return new AllDealersListAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, email;
        private CircleImageView userImage;
        private Button approve, call;
        private ImageView cancelReq;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.dealer_single_name);
            email = itemView.findViewById(R.id.dealer_single_email);
            userImage = itemView.findViewById(R.id.dealer_single_image);
            approve = itemView.findViewById(R.id.req_approve_btn);
            call = itemView.findViewById(R.id.req_call_btn);
            cancelReq =  itemView.findViewById(R.id.req_cancel_image);

        }

    }
}
