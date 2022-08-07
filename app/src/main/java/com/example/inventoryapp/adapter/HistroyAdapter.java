package com.example.inventoryapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryapp.R;
import com.example.inventoryapp.model.AssetDetails;

import java.util.ArrayList;

public class HistroyAdapter extends RecyclerView.Adapter<HistroyAdapter.MyViewHolder> {

    ArrayList<AssetDetails> data;
    Context context;
    String TAG = "CategoryAdapter";

    public HistroyAdapter(ArrayList<AssetDetails> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.histroy_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AssetDetails assetDetails = data.get(position);
        holder.tvTagNumber.setText(assetDetails.getTag_number());
        holder.tvAssetName.setText(assetDetails.getAsset());
        holder.tvLocation.setText(assetDetails.getLocation());
        holder.tvSubLocation.setText(assetDetails.getSub_location());
        holder.tvActualQuantity.setText(assetDetails.getQty());
        holder.tvAvailableQty.setText(assetDetails.getAvailable_qty());
        holder.tvcategory.setText(assetDetails.getCategory());
        holder.tvuom.setText(assetDetails.getUom());
        holder.tvItemCode.setText(assetDetails.getDate());

            String type;
        if (assetDetails.getMain_category().equals("W"))
            type="Warehouse";
        else
            type="Store";
        holder.tvMainCategory.setText(type);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTagNumber, tvAssetName, tvActualQuantity, tvAvailableQty, tvLocation, tvSubLocation, tvcategory, tvuom,tvItemCode,tvMainCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTagNumber = itemView.findViewById(R.id.tvTagNumber);
            tvAssetName = itemView.findViewById(R.id.tvAssetName);
            tvActualQuantity = itemView.findViewById(R.id.tvActualQuantity);
            tvAvailableQty = itemView.findViewById(R.id.tvAvailableQty);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSubLocation = itemView.findViewById(R.id.tvSubLocation);
            tvcategory = itemView.findViewById(R.id.tvcategory);
            tvuom = itemView.findViewById(R.id.tvuom);
            tvItemCode = itemView.findViewById(R.id.tvItemCode);
            tvMainCategory = itemView.findViewById(R.id.tvMainCategory);

        }
    }
}
