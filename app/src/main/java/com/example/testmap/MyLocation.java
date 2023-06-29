package com.example.testmap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MyLocation {

    private LocationManager lm;
    private Context ctx;
    private MutableLiveData<Location> _location = new MutableLiveData<>();

    public LiveData<Location> location = _location;

    public MyLocation(Context context) {
        ctx = context;
    }

    private final LocationListener gpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            lm.removeUpdates(networkListener);
            lm.removeUpdates(gpsListener);
            _location.setValue(location);
        }
    };

    private final LocationListener networkListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            lm.removeUpdates(networkListener);
            lm.removeUpdates(gpsListener);
            _location.setValue(location);
        }
    };

    public Response loadLocation() {
        if (lm == null) {
            lm = ((LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE));
        }
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {}

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {}

        if (!gpsEnabled && !networkEnabled) {
            return Response.GPS_NOT_ENABLED;
        }

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return Response.PERMISSIONS_NOT_GRANTED;
        }
        if(gpsEnabled) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0,
                    gpsListener);
        }

        if(networkEnabled) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0,
                    networkListener);
        }
        return Response.OK;
    }

    public enum Response{
        GPS_NOT_ENABLED,
        NETWORK_NOT_ENABLED,
        PERMISSIONS_NOT_GRANTED,
        OK
    }

}
