package com.portugal1576.gpstracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocListener implements LocationListener {
    private LocListenerInterface locListenerInterface;
    @Override
    public void onLocationChanged(Location location) {
        locListenerInterface.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void setLocListenerInterface(LocListenerInterface locListenerInterface) {
        this.locListenerInterface = locListenerInterface;
    }
}
