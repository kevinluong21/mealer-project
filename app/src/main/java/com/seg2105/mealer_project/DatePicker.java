package com.seg2105.mealer_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import android.app.DatePickerDialog;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

//if fail put a textbox in
/*
public class DatePicker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        Button button = (Button) findViewById(R.id.btnPickDate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        Calendar c = Calendar.getInstance();

        c.set(Calender.YEAR, year);
        c.set(Calender.MONTH, month);
        c.set(Calender.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.tvDate);
        textView.setText(currentDate);
    }

}*/