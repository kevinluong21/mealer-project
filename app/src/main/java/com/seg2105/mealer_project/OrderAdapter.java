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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private final Context context;
    private final ArrayList<OrderModel> orderModelArray;

    public OrderAdapter(Context context, ArrayList<OrderModel> orderModelArray){
        this.context = context;
        this.orderModelArray = orderModelArray;

    }
    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        OrderModel model = orderModelArray.get(position);
        holder.orderImage.setBackgroundResource(model.getImgSrc());
        holder.orderCustomerName.setText(model.getCustomerName());
        holder.orderCustomerEmail.setText(model.getCustomerEmail());
        holder.orderMealName.setText(model.getMealName());
        holder.orderMealPrice.setText(model.getMealPrice());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return orderModelArray.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView orderImage;
        private final TextView orderCustomerName;
        private final TextView orderCustomerEmail;
        private final TextView orderMealName;
        private final TextView orderMealPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = (ImageView) itemView.findViewById(R.id.orderImage);
            orderCustomerName = (TextView) itemView.findViewById(R.id.orderCustomerName);
            orderCustomerEmail = (TextView) itemView.findViewById(R.id.orderCustomerEmail);
            orderMealName = (TextView) itemView.findViewById(R.id.orderMealName);
            orderMealPrice = (TextView) itemView.findViewById(R.id.orderMealPrice);
        }
    }
}


