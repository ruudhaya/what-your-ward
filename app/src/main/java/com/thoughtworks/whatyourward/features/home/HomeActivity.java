package com.thoughtworks.whatyourward.features.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlLayer;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.thoughtworks.whatyourward.R;
import com.thoughtworks.whatyourward.data.model.ward.Ward;
import com.thoughtworks.whatyourward.features.base.BaseActivity;
import com.thoughtworks.whatyourward.injection.component.ActivityComponent;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


public class HomeActivity extends BaseActivity implements HomeView, OnMapReadyCallback {

    @Inject
    HomePresenter homePresenter;

    private static final int defaultZoom = 13;

//    private static LatLng currentLocation = new LatLng(13.0827, 80.2707);

    @BindView(R.id.btn_next)
    Button btnNext;

    private KmlLayer kmlLayer;


    private RxPermissions rxPermissions;

    private RxLocation rxLocation;

    private LatLng latLng;
    private SupportMapFragment mapFragment;
    private LocationRequest locationRequest;
    private CompositeDisposable disposable;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
        googleMap.getUiSettings().setZoomControlsEnabled(false);


//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
//                PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
//                        PackageManager.PERMISSION_GRANTED) {
//            googleMap.setMyLocationEnabled(true);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//        } else {
//            // reload activity
//            ActivityCompat.requestPermissions(this, new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        }
    }


    @Override
    public void onViewReady() {

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        requestLocationPermission();
        homePresenter.loadWard();
    }

    private void requestLocationPermission() {

        rxPermissions = new RxPermissions(HomeActivity.this); // where this is an Activity instance

        disposable = new CompositeDisposable();

        rxLocation = new RxLocation(HomeActivity.this);

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);

        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {


                        disposable.add(
                                rxLocation.settings().checkAndHandleResolution(locationRequest)
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(success-> {

                                            Timber.i("Location enabled success");

                                            onLocationUpdated();
                                        }, throwable ->

                                                Timber.i("Location Permissions Not enabled", throwable))
                        );




                    } else {

                        Toast.makeText(HomeActivity.this,
                                "Sorry! Map cannot be loaded. Please enable the location permission",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void showCategoryList(ArrayList<Ward> wardArrayList) {

        Timber.i("loaded size "+wardArrayList.size());
    }

    @OnClick(R.id.btn_next)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_next:

                View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);

                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);
                dialog.show();

                break;
        }
    }

    public void onLocationUpdated() {

        Timber.i("onLocation Updated called");

        rxLocation.location().updates(locationRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {

            latLng = new LatLng(location.getLatitude(), location.getLongitude());


            Timber.i("Lat lng inside onLocationUpdated");
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.


            mapFragment.getMapAsync(this);


                    Timber.i("getMapAsync() called in onLocationUpdated");


                }, throwable -> Timber.e("Unable to fetch locations", throwable));



    }
}
