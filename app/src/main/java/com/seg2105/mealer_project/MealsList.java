package com.seg2105.mealer_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;


public class MealsList extends ArrayAdapter<Meal> implements Serializable {

    private Activity context;
    List<Meal> mealList;

    public MealsList(Activity context, List<Meal> meals) {
        super(context, R.layout.layout_meal_list, meals);
        this.context = context;
        this.mealList = meals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_complaint_list, null, true);

        TextView textViewMealName = (TextView) listViewItem.findViewById(R.id.textViewMealName);
        TextView txtViewMealType = (TextView) listViewItem.findViewById(R.id.txtViewMealType);

       // Complaint complaint = complaints.get(position);
        Meal meals = mealList.get(position);
        //textViewComplaintName.setText(String.valueOf("Complaint filed by " + complaint.getClientEmail() +" for " + complaint.getCookEmail()));
        textViewMealName.setText(String.valueOf(meals.getName()));
        txtViewMealType.setText(String.valueOf(meals.getDescription()));
        return listViewItem;
    }


}
