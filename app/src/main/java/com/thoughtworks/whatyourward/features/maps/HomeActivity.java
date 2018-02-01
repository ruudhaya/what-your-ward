package com.thoughtworks.whatyourward.features.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlLayer;
import com.thoughtworks.whatyourward.R;
import com.thoughtworks.whatyourward.features.base.BaseActivity;
import com.thoughtworks.whatyourward.features.splash.SplashScreenPresenter;
import com.thoughtworks.whatyourward.injection.component.ActivityComponent;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import javax.inject.Inject;


public class HomeActivity extends BaseActivity implements HomeView, OnMapReadyCallback {

    @Inject
    HomePresenter homePresenter;

    private static final int defaultZoom = 13;

    private static LatLng currentLocation = new LatLng(13.0827, 80.2707);

    private KmlLayer kmlLayer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        homePresenter.onViewReady();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        homePresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        homePresenter.detachView();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        setDefaultConfig(googleMap);

        try {
            kmlLayer = new KmlLayer(googleMap, R.raw.chennai_wards, this);
            kmlLayer.addLayerToMap();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

    }


    private void setDefaultConfig(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, defaultZoom));
        googleMap.getUiSettings().setZoomControlsEnabled(false);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            // reload activity
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        }
    }


    @Override
    public void onViewReady() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
    }
}
