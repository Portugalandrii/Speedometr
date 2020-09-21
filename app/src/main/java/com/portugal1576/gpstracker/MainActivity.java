package com.portugal1576.gpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocListenerInterface {
    private LocationManager locationManager;
    private TextView tvDistance;
    private TextView tvVelocity;
    private TextView tvGeneral;
    private Location lastLocation;
    private MyLocListener myLocListener;
    private long distance;
    private long all_distance;
    private final static String KEY1 = "key1";
    private final static String KEY2 = "key2";
    private SharedPreferences pref;
    private long dataToSave1;
    private long dataToSave2;
    private Float distanceFloat;
    private Float all_distanceFloat;
    private String distanceFloatS;
    private String all_distanceFloatS;
    private int velocityS;
    private ImageView strelka;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("TABLE", MODE_PRIVATE);
        distance = pref.getLong(KEY2, 0);
        all_distance = pref.getLong(KEY1, 0);
        strelka = findViewById(R.id.strelka);
        init();
    }

    private void init() {
        tvDistance = findViewById(R.id.tvDistance);
        tvVelocity = findViewById(R.id.tvVelocity);
        tvGeneral = findViewById(R.id.tvGeneral);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyLocListener();
        myLocListener.setLocListenerInterface(this);
        checkPermission();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == RESULT_OK) {
            checkPermission();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, myLocListener);
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        if (loc.hasSpeed() && lastLocation != null) {
            distance += lastLocation.distanceTo(loc);
            all_distance += lastLocation.distanceTo(loc);
        }
        lastLocation = loc;
        distanceFloat = (float)distance/1000;
        all_distanceFloat = (float)all_distance/1000;
        distanceFloatS = String.format("%.2f", distanceFloat);
        all_distanceFloatS = String.format("%.2f", all_distanceFloat);
        tvDistance.setText((distanceFloatS + " КМ"));
        velocityS = (int) (loc.getSpeed()*3.6);
        tvVelocity.setText(String.valueOf(velocityS) );
        tvGeneral.setText((all_distanceFloatS + " КМ"));
        strelka.setRotation((float) (-134 + velocityS*2.23333333));
        saveData(all_distance, distance);
    }

    public void clear(View view) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(KEY2);
        editor.apply();
        distance = 0;
        tvDistance.setText(String.valueOf(distance));
    }
    private void saveData (long dataToSave1, long dataToSave2){
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.putLong(KEY1, dataToSave1);
        editor.putLong(KEY2, dataToSave2);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        saveData(all_distance, distance);
        super.onDestroy();
    }
}