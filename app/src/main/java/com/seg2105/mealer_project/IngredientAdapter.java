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

import java.util.ArrayList;
import java.util.Observable;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder>{
    private final Context context;
    private final ArrayList<IngredientModel> ingredientModels;

    public IngredientAdapter(Context context, ArrayList<IngredientModel> ingredientModels) {
        this.context = context;
        this.ingredientModels = ingredientModels;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pill_list_green, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        IngredientModel model = ingredientModels.get(position);
        holder.ingredient.setText(model.getIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredientModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView ingredient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient = (TextView) itemView.findViewById(R.id.textItem);
        }
    }
}
