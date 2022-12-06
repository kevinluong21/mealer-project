//code for this adapter comes from https://www.geeksforgeeks.org/custom-arrayadapter-with-listview-in-android/

package com.seg2105.mealer_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends ArrayAdapter<Meal> implements Filterable {

    ArrayList<Meal> originalMealList; //source list must be cleared so that it can be filtered, so this keeps a copy of meals to
    //be added back

    public SearchResultAdapter(@NonNull Context context, ArrayList<Meal> list) {
        super(context, 0, list);
        originalMealList = new ArrayList<Meal>(list); //creates a copy of meals
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.search_result, parent, false);
        }

        Meal currentNumberPosition = getItem(position);
        ImageView imageMeal = currentItemView.findViewById(R.id.imageMealResult);
        assert currentNumberPosition != null;
//        imageMeal.setImageResource(currentNumberPosition.get); //no image for meal class is set yet

        TextView textMealName = currentItemView.findViewById(R.id.textMealNameResult);
        textMealName.setText(currentNumberPosition.getName());

        TextView textCookAddress = currentItemView.findViewById(R.id.textCookAddress);
        textCookAddress.setText(currentNumberPosition.getCookAddress().formatAddress());

        TextView textRating = currentItemView.findViewById(R.id.textRating);
        textRating.setText(currentNumberPosition.displayRating());

        TextView textPrice = currentItemView.findViewById(R.id.textPriceResult);
        textPrice.setText(currentNumberPosition.displayPrice());

        TextView textMealType = currentItemView.findViewById(R.id.textMealTypeResult);
        textMealType.setText(currentNumberPosition.getMealType());

        TextView textCuisineType = currentItemView.findViewById(R.id.textCuisineTypeResult);
        textCuisineType.setText(currentNumberPosition.getCuisineType());

        return currentItemView;
    }

    //search filtering based on multiple query types
    //code from https://stackoverflow.com/questions/19122848/custom-getfilter-in-custom-arrayadapter-in-android
    //code from https://stackoverflow.com/questions/16063338/android-filtering-a-list-of-objects
    Filter filter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Meal> filteredList = new ArrayList<>();

            for (Meal meal : originalMealList) {
                //these conditions are all in else if statements because, though they are not mutually exclusive, it only needs
                //to show the result once
                if (meal.getName().contains(constraint)) { //filter by name
                    filteredList.add(meal);
                }
                else if (meal.getMealType().contains(constraint)) { //filter by cuisine type
                    filteredList.add(meal);
                }
                else if (meal.getCuisineType().contains(constraint)) { //filter by cuisine type
                    filteredList.add(meal);
                }
            }

            results.count = filteredList.size();
            results.values = filteredList;

            return results; //search results
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Meal> arrayList;
            if (results != null && results.values != null) {
                arrayList = (ArrayList<Meal>) results.values;
                clear();
                addAll(arrayList);
                notifyDataSetChanged();
            }
        }
    };

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }
}
