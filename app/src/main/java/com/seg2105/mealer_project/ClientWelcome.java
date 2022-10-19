package com.seg2105.mealer_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ClientWelcome extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientwelcome);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clientLogOffID:
                clientLogOff(v);
                break;
        }
    }

    public void clientLogOff (View v) {
        //CRASHES ONLY WHEN ASKED TO GO TO MAIN ACTIVITY
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
