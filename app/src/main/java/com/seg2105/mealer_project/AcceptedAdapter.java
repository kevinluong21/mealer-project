package com.seg2105.mealer_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class AcceptedAdapter extends RecyclerView.Adapter<AcceptedAdapter.ViewHolder>{

    private final Context context;
    private final ArrayList<AcceptedModel> acceptedModelArray;

    public AcceptedAdapter(Context context, ArrayList<AcceptedModel> acceptedModelArray){
        this.context = context;
        this.acceptedModelArray = acceptedModelArray;

    }
    @NonNull
    @Override
    public AcceptedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedAdapter.ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        AcceptedModel model = acceptedModelArray.get(position);
        holder.acceptedImage.setBackgroundResource(model.getImgSrc());
        holder.acceptedCustomerName.setText(model.getCustomerName());
        holder.acceptedCustomerEmail.setText(model.getCustomerEmail());
        holder.acceptedMealName.setText(model.getMealName());
        holder.acceptedMealPrice.setText(model.getMealPrice());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return acceptedModelArray.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView acceptedImage;
        private final TextView acceptedCustomerName;
        private final TextView acceptedCustomerEmail;
        private final TextView acceptedMealName;
        private final TextView acceptedMealPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            acceptedImage = (ImageView) itemView.findViewById(R.id.acceptedImage);
            acceptedCustomerName = (TextView) itemView.findViewById(R.id.acceptedCustomerName);
            acceptedCustomerEmail = (TextView) itemView.findViewById(R.id.acceptedCustomerEmail);
            acceptedMealName = (TextView) itemView.findViewById(R.id.acceptedMealName);
            acceptedMealPrice = (TextView) itemView.findViewById(R.id.acceptedMealPrice);
        }
    }
}

