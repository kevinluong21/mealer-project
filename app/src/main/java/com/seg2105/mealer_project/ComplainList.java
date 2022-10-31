package com.seg2105.mealer_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class ComplainList extends ArrayAdapter<Complaint> implements Serializable {

    private Activity context;
    List<Complaint> complaints;

    public ComplainList(Activity context, List<Complaint> complaints) {
        super(context, R.layout.layout_complaint_list, complaints);
        this.context = context;
        this.complaints = complaints;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_complaint_list, null, true);

        TextView textViewComplaintName = (TextView) listViewItem.findViewById(R.id.textViewComplaintName);
        TextView textViewComplaintDescription = (TextView) listViewItem.findViewById(R.id.txtViewComplaintDescription);

        Complaint complaint = complaints.get(position);
        //textViewComplaintName.setText(String.valueOf("Complaint filed by " + complaint.getClientEmail() +" for " + complaint.getCookEmail()));
        textViewComplaintName.setText(String.valueOf(complaint.getId()));
        textViewComplaintDescription.setText(String.valueOf(complaint.getDescription()));
        return listViewItem;
    }
}
