package com.thoughtworks.whatyourward.features.home;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.thoughtworks.whatyourward.R;
import com.thoughtworks.whatyourward.data.model.ward.Ward;
import com.thoughtworks.whatyourward.data.model.ward.ZoneInfo;
import com.thoughtworks.whatyourward.features.base.BaseActivity;
import com.thoughtworks.whatyourward.injection.component.ActivityComponent;
import com.thoughtworks.whatyourward.util.KmlUtil;
import com.thoughtworks.whatyourward.util.ParseUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
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
    private GoogleMap mGoogleMap;
    private ArrayList<Ward> mWardArrayList;

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

        mGoogleMap = googleMap;

        Timber.i("on Map ready called");

        setDefaultConfig(googleMap);

        try {

            Timber.i("Kml loading");

            kmlLayer = new KmlLayer(googleMap, R.raw.chennai_wards, this);
            kmlLayer.addLayerToMap();

            Timber.i("Kml loaded");

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
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(success -> {

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

        mWardArrayList = wardArrayList;
        Timber.i("loaded size " + wardArrayList.size());


    }

    @OnClick(R.id.btn_next)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_next:


                Ward ward = getWardDetails();
                ZoneInfo zoneInfo = ward.getZoneInfo();

                View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);


                TextView txtZoneName = view.findViewById(R.id.txt_zone_name);
                TextView txtZoneAddress = view.findViewById(R.id.txt_zone_address);
                TextView txtZoneNumber = view.findViewById(R.id.txt_zone_number);
                TextView txtZoneMobile = view.findViewById(R.id.txt_zone_mobile);
                TextView txtWardName = view.findViewById(R.id.txt_ward_name);
                TextView txtWardAddress = view.findViewById(R.id.txt_ward_address);
                TextView txtWardId = view.findViewById(R.id.txt_ward_id);
                TextView txtWardMobile = view.findViewById(R.id.txt_ward_mobile);

                LinearLayout llWhatsappGroup = view.findViewById(R.id.ll_whatsapp_group);


                setText(ward.getWardName(), txtWardName);
                setText(ward.getWardOfficeAddress(), txtWardAddress);
                setText(ward.getWardNo(), txtWardId);
                setText(ward.getWardOfficePhone(), txtWardMobile);


                setText(zoneInfo.getZoneName(), txtZoneName);
                setText(zoneInfo.getZoneNo(), txtZoneNumber);
                setText(zoneInfo.getZonalOfficeAddress(), txtZoneAddress);
                setText(zoneInfo.getZonalOfficePhone(), txtZoneMobile);


                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);
                dialog.show();

                break;
        }
    }

    public void onLocationUpdated() {

        Timber.i("onLocation Updated called");

        rxLocation.location().lastLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {

                    latLng = new LatLng(location.getLatitude(), location.getLongitude());


                    Timber.i("Lat lng inside onLocationUpdated");
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.


                    mapFragment.getMapAsync(this);


                    Timber.i("getMapAsync() called in onLocationUpdated");


                }, throwable -> Timber.e("Unable to fetch locations", throwable));

    }


    public LatLng getCenterOfMap() {
        return mGoogleMap.getCameraPosition().target;
    }


    private String getWardNum(LatLng latLng) {
        KmlPlacemark kmlPlacemark = KmlUtil.containsInAnyPolygon(kmlLayer, latLng);
        if (kmlPlacemark != null) {
            String wardName = kmlPlacemark.getProperty("name");
            String wardNo = ParseUtil.getWardNum(wardName);
            return wardNo;
        }
        return null;
    }


    private Ward getWardDetails() {

        LatLng center = getCenterOfMap();
        String wardNo = getWardNum(center);


        for (Ward ward : mWardArrayList) {

            if (wardNo.equalsIgnoreCase(ward.getWardNo())) {
                return ward;
            }
        }

        return null;
    }


    private void setText(String text, TextView textView) {

        if(TextUtils.isEmpty(text)){
            textView.setText("-");
        }else{
            textView.setText(text);
        }

    }
}
