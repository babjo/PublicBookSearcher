package com.seoul.publicbooksearcher.domain.async_usecase;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class SortLibraries implements AsyncUseCase<Void>, LocationListener {

    private final LocationManager locationManager;
    private Context context;
    private AsyncUseCaseListener asyncUseCaseListener;

    public SortLibraries(Context context) {
        this.context = context;
        this.locationManager = (LocationManager)  context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void execute(Void arg, AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;
        registerLocationUpdates();
    }

    private void registerLocationUpdates() {
        if (locationManager != null) {
            if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        asyncUseCaseListener.onAfter(new com.seoul.publicbooksearcher.domain.Location(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

