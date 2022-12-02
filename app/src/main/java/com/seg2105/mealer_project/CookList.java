package com.seg2105.mealer_project;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class CookList extends ArrayAdapter<Cook> implements Serializable {

    private Activity context;
    List<Cook> cooks;

    public CookList(Activity context, List<Cook> cooks) {
        //NEED TO CHANGE
        //AP reusing the layout maybe?
        super(context, R.layout.layout_complaint_list, cooks);
        this.context = context;
        this.cooks = cooks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_complaint_list, null, true);

        TextView textViewComplaintName = (TextView) listViewItem.findViewById(R.id.textViewComplaintName);
        TextView textViewComplaintDescription = (TextView) listViewItem.findViewById(R.id.txtViewComplaintDescription);

        Cook cook = cooks.get(position);
        //textViewComplaintName.setText(String.valueOf("Complaint filed by " + complaint.getClientEmail() +" for " + complaint.getCookEmail()));
        textViewComplaintName.setText(String.valueOf(cook.getFirstName())+" "+ String.valueOf(cook.getLastName()));
        textViewComplaintDescription.setText(String.valueOf(cook.getEmailAddress()));
        return listViewItem;
    }
}
