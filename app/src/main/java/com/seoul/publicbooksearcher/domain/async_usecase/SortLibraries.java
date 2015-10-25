package com.seoul.publicbooksearcher.domain.async_usecase;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.seoul.publicbooksearcher.domain.exception.CantNotKnowLocationException;
import com.seoul.publicbooksearcher.domain.exception.NotGpsSettingsException;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import java.util.Timer;
import java.util.TimerTask;

import static android.support.v4.content.ContextCompat.checkSelfPermission;


public class SortLibraries implements AsyncUseCase<Void>, LocationListener {

    private LocationManager locationManager;
    private Context context;
    private AsyncUseCaseListener asyncUseCaseListener;
    private Timer timer = new Timer();

    public SortLibraries(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void execute(Void arg, final AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
            timer.schedule(new TimerTask(){
                public void run() {
                    try {
                        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, SortLibraries.this);
                        else
                            asyncUseCaseListener.onError(new CantNotKnowLocationException());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }}
                , 5000);

        }else{
            asyncUseCaseListener.onError(new NotGpsSettingsException());
        }
    }

    private boolean foundByGps = false;
    @Override
    public void onLocationChanged(Location location) {
        asyncUseCaseListener.onAfter(new com.seoul.publicbooksearcher.domain.Location(location.getLatitude(), location.getLongitude()));
        if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(SortLibraries.this);
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

