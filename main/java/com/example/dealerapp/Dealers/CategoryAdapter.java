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
import android.widget.TextView;

import com.example.dealerapp.AllDealersListAdapter;
import com.example.dealerapp.R;
import com.example.dealerapp.Utils.Users;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    Context mCtx;
    List<Category> CategoryList;

    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(String push_id);

    }
    public CategoryAdapter(Context mCtx, List<Category> CategoryList)
    {
        this.mCtx = mCtx;
        this.CategoryList = CategoryList;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_category,
                parent, false);
        CategoryViewHolder CategoryViewHolder = new CategoryViewHolder(view);
        return CategoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
        final Category category = CategoryList.get(position);
        holder.name.setText(category.getItem_category());
        final String PushId = category.getItem_category();
        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.getPosition(PushId);
//                holder.card_layout.setClickable(false);
//                Intent mIntent = new Intent(mCtx, CategoryItemDisplayActivity.class);
//                mIntent.putExtra("Key", PushId);
//                mCtx.startActivity(mIntent);
//                holder.card_layout.setClickable(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CategoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        CardView card_layout;
        //AppCompatRatingBar ratingBar;
        Button callButton,billButton,cancelButton,ratingButton;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            card_layout = (CardView) itemView.findViewById(R.id.sort_card);
            name = (TextView) itemView.findViewById(R.id.category_name);
            //ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.ratingBarMain);
        }
    }
}
