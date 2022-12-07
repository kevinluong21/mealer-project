//this class is intended for displaying the ingredients and allergens of a meal
//code implemented from https://www.geeksforgeeks.org/cardview-using-recyclerview-in-android-with-example/

package com.seg2105.mealer_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ClientOrderAdapter extends RecyclerView.Adapter<ClientOrderAdapter.ViewHolder>{
    private final Context context;
    private final ArrayList<ClientOrderModel> clientOrderModels;

    public ClientOrderAdapter(Context context, ArrayList<ClientOrderModel> clientOrderModels) {
        this.context = context;
        this.clientOrderModels = clientOrderModels;
    }

    @NonNull
    @Override
    public ClientOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_orders_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientOrderAdapter.ViewHolder holder, int position) {
        ClientOrderModel model = clientOrderModels.get(position);
        holder.imageMeal.setBackgroundResource(model.getImageSrc());
        holder.textMealName.setText(model.getMealName());
        holder.textAddress.setText(model.getAddress().formatAddress());
        holder.textOrderProgress.setText(model.getProgress());
    }

    @Override
    public int getItemCount() {
        return clientOrderModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textMealName;
        private final TextView textAddress;
        private final TextView textOrderProgress;
        private final ImageView imageMeal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMeal = (ImageView) itemView.findViewById(R.id.imageMeal);
            textMealName = (TextView) itemView.findViewById(R.id.textMealName);
            textAddress = (TextView) itemView.findViewById(R.id.textAddress);
            textOrderProgress = (TextView) itemView.findViewById(R.id.textOrderProgress);
        }
    }
}
