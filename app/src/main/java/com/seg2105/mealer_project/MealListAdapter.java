//adapter class for displaying meals in the recyclerview of a profile
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

import java.util.*;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.ViewHolder>{

    private final Context context;
    private final ArrayList<MealListModel> mealProfileModels;

    public MealListAdapter(Context context, ArrayList<MealListModel> mealProfileModels) {
        this.context = context;
        this.mealProfileModels = mealProfileModels;
    }

    @NonNull
    @Override
    public MealListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealListAdapter.ViewHolder holder, int position) {
        //image view is not set yet
        MealListModel model = mealProfileModels.get(position);
        holder.imageMeal.setBackgroundResource(model.getImageSrc());
        holder.textMealName.setText(model.getMealName());
        holder.textPrice.setText(model.getPrice());
        holder.textMealRating.setText(model.getRating());
        holder.textMealsSold.setText(Integer.toString(model.getMealsSold()));
    }

    @Override
    public int getItemCount() {
        return mealProfileModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageMeal;
        private final TextView textMealName;
        private final TextView textPrice;
        private final TextView textMealRating;
        private final TextView textMealsSold;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMeal = (ImageView) itemView.findViewById(R.id.imageMeal);
            textMealName = (TextView) itemView.findViewById(R.id.textMealName);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            textMealRating = (TextView) itemView.findViewById(R.id.textMealRating);
            textMealsSold = (TextView) itemView.findViewById(R.id.textMealsSold);
        }
    }
}
