package com.example.login;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "pttt";
    private static final String IPDEVICE = "192.168.1.26";
    private static final String CITYASHQELON = "אשקלון";
    private static final String CITYTELAVIV = "תל אביב";
    private static final String PASSWORD = "205516479";
    private static final String CONTAINER = "services_container";
    private static final int PERMISSION_REGULAR_LOCATION_REQUEST_CODE = 133;
    private static final int PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE = 134;

    private int batteryLevel = 0;
    private String ip = "";
    private String city = "";
    private String deviceVolume = "";
    private int intDeviceVolume = 0;

    private EditText inputText;
    private MaterialButton submitBtn;


    private Services container;


    private boolean FORCE = true;
    private boolean CAN_GRANT_MANUALLY = false;

    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            container.setBatteryLevel(batteryLevel);
            Log.d(TAG, "onReceive: ");

        }
    };


    private void check() {

        getIpOfDevice();
        getCurrentVolume();

        Log.d(TAG, "check: " + batteryLevel);
        Log.d(TAG, "check: " + isValidDayAndHour().booleanValue());
        Log.d(TAG, "check: " + intDeviceVolume);


        if (inputText.getText().toString().equals(PASSWORD)
                && city.equals(CITYASHQELON) || city.equals(CITYTELAVIV)
                && batteryLevel > 50
                && (isValidDayAndHour())
                && intDeviceVolume > 50
                && ip.equals(IPDEVICE)) {


            Gson gson = new Gson();
            Intent intent = new Intent(MainActivity.this, ConnectionSuccessfulActivity.class);
            intent.putExtra(CONTAINER, gson.toJson(container));
            startActivity(intent);
            finish();
        } else {
            submitBtn.setBackgroundColor(getColor(R.color.red));
            submitBtn.setText("Try again");
        }


        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        container = new Services();
        inputText = findViewById(R.id.main_EDT_inputBox);
        submitBtn = findViewById(R.id.main_BTN_submitBtn);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        request();


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    getCity();
                    check();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


    }

    public void getCity() throws IOException {

        Log.d(TAG, "getCity: ");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        Log.d(TAG, "getCity: " + address);
        city = addresses.get(0).getLocality();
        Log.d("pttt", "getCity: " + city);
        container.setLoginLocation(city);

    }



    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLocation = location;
                            // Logic to handle location object
                        }else {
                            return;
                        }
                    }
                });


        Log.d("pttt", "Location Success !!!");
    }

    private void requestFirstLocationPermission() {
        // Regular location permissions
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REGULAR_LOCATION_REQUEST_CODE);
    }

    private void requestSecondLocationPermission() {
        // Background location permissions
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE);
    }

    private void request() {
        boolean per1 = ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean per2 = ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean per3 = android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q  ||  ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!per1  ||  !per2) {
            // if i can ask for permission
            requestFirstLocationPermission();
        } else if (!per3) {
            // if i can ask for permission
            requestSecondLocationPermission();
        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REGULAR_LOCATION_REQUEST_CODE: {
                request();
                return;
            }
            case PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE: {
                request();
                return;
            }
        }
    }


    //The method check if range of hours is valid and The connection did not happen on Saturday
    public Boolean isValidDayAndHour(){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Log.d("pttt", "isValidDayAndHour: true"+hour);
        if(cal.get(Calendar.DAY_OF_WEEK)==7)
        {
            return false;
        }
        if(cal.get(Calendar.DAY_OF_WEEK)==6)
        {
            if(cal.get(Calendar.HOUR_OF_DAY)>17)
            return false;

        }
        if (hour > 1 && hour < 6) {
            return false;
        }

        return true;
    }
    private void getCurrentVolume() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        deviceVolume = "" + (100 * currentVolume / maxVolume);
        intDeviceVolume = (100 * currentVolume / maxVolume);
        Log.d(TAG, "getCurrentVolume: " + deviceVolume + "%");
        container.setDeviceVolume(intDeviceVolume);
    }
    public void getIpOfDevice(){

        @SuppressLint("WifiManagerLeak") WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        Log.d(TAG, "getIpOfDevice: "+ip);
        container.setIpDevice(ip);
    }



}