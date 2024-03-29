package com.example.dealerapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dealerapp.Utils.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.TestViewHolder>{

    Context mCtx;
    List<Product> ProductsList;
    OnItemClick onItemClick;

    public ProductAdapter(Context mCtx, List<Product> ProductsList)
    {
        this.mCtx = mCtx;
        this.ProductsList = ProductsList;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(String key);

    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_product,
                parent, false);
        TestViewHolder testViewHolder = new TestViewHolder(view);
        return testViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TestViewHolder holder, int position) {
        final Product Product = ProductsList.get(position);

        holder.product_name.setText(Product.getItem_name());
        holder.price.setText(String.valueOf(Product.getItem_price()) + " ₹");
        holder.mrp.setText(String.valueOf(Product.getItem_mrp()));
        holder.discount.setText(String.valueOf(Product.getItem_discount()));
        holder.brand_name.setText(Product.getItem_brand());
        String image = Product.getItem_image();
        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(image).placeholder(R.drawable.products).into(holder.product_image);

        final String PushId = Product.getItem_id();
        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.getPosition(PushId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ProductsList.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder
    {
        TextView price,mrp,discount,brand_name,product_name;
        CardView card_layout;
        ImageView product_image;

        public TestViewHolder(View itemView) {
            super(itemView);
            mrp = (TextView) itemView.findViewById(R.id.mrp);
            price = (TextView) itemView.findViewById(R.id.price);
            discount = (TextView) itemView.findViewById(R.id.discount);
            brand_name = (TextView) itemView.findViewById(R.id.brand_name);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_image = (ImageView) itemView.findViewById(R.id.product_image);
            card_layout = (CardView) itemView.findViewById(R.id.card_layout);
        }
    }
}
