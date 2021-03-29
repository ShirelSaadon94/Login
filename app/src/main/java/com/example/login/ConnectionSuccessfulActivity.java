package com.example.login;

import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

public class ConnectionSuccessfulActivity extends AppCompatActivity {
    private static final String TAG = "pttt";
    private static final String CONTAINER = "services_container";

    private Services container;
    private TextView batteryLbl;
    private TextView dateLbl;
    private TextView cityLbl;
    private TextView ipLbl;
    private TextView volumeLbl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Gson gson = new Gson();
        container = gson.fromJson(getIntent().getStringExtra(CONTAINER), Services.class);
        Log.d(TAG, "onCreate: Got container: " + container.toString());
        initViews();

    }

    private void initViews() {

        batteryLbl = findViewById(R.id.success_LBL_batteryLevel);
        batteryLbl.setText("Battery: " + container.getBatteryLevel() + "%");
        ipLbl = findViewById(R.id.success_LBL_ip);
        ipLbl.setText("IP: " + container.getIpDevice());
        dateLbl = findViewById(R.id.success_LBL_DATE);
        dateLbl.setText("Time:" + container.getDayAndHour());
        cityLbl = findViewById(R.id.success_LBL_location);
        cityLbl.setText("City: " + container.getLoginLocation());
        volumeLbl = findViewById(R.id.success_LBL_deviceVolume);
        volumeLbl.setText("Volume: " + container.getDeviceVolume() + "%");


    }
}