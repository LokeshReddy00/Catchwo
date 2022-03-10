package com.buddies.catchwo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.buddies.catchwo.ui.AppointHistoryFragment;
import com.buddies.catchwo.ui.YourAppointFragment;
import com.buddies.catchwo.ui.home.HomeFragment;

public class AppointScreen extends AppCompatActivity {

    Button appoints,history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_screen);

        history = findViewById(R.id.history);
        appoints = findViewById(R.id.appoints);

        getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, new YourAppointFragment()).commit();



        history.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, new AppointHistoryFragment()).commit());

        appoints.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, new YourAppointFragment()).commit());

    }

    @Override
    protected void onStart() {
        super.onStart();
        appoints.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, new YourAppointFragment()).commit());
    }
}