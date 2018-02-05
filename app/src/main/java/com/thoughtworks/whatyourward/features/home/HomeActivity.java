package com.thoughtworks.whatyourward.features.home;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.thoughtworks.whatyourward.Constants;
import com.thoughtworks.whatyourward.R;
import com.thoughtworks.whatyourward.data.model.ward.Ward;
import com.thoughtworks.whatyourward.data.model.ward.ZoneInfo;
import com.thoughtworks.whatyourward.features.base.BaseActivity;
import com.thoughtworks.whatyourward.injection.component.ActivityComponent;
import com.thoughtworks.whatyourward.util.IntentUtil;
import com.thoughtworks.whatyourward.util.KmlUtil;
import com.thoughtworks.whatyourward.util.ParseUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;


public class HomeActivity extends BaseActivity implements HomeView, OnMapReadyCallback
        , GoogleApiClient.OnConnectionFailedListener
        , GoogleApiClient.ConnectionCallbacks
            ,ResultCallback<LocationSettingsResult>{

    @Inject
    HomePresenter homePresenter;



    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.img_loading)
    ImageView imgLoading;
    @BindView(R.id.img_map_marker)
    ImageView imgMapMarker;

    @BindView(R.id.view_loading)
    LinearLayout viewLoading;

    @BindView(R.id.ll_footer)
    LinearLayout llFooter;

    private KmlLayer kmlLayer;


    private RxPermissions rxPermissions;


    private SupportMapFragment mapFragment;

    private GoogleMap mGoogleMap;
    private ArrayList<Ward> mWardArrayList;
    private AnimationDrawable loadingAnimationDrawable;


    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;



    private static String ATTRIBUTE_KML_NAME = "name";

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


    }


    private void configureMapAndAddLayer(Location location) {

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), Constants.DEFAULT.MAP_ZOOM));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);

        new Handler().postDelayed(() -> {

            try {

                homePresenter.stopAnimation();

                Timber.i("Kml loading");

                kmlLayer = new KmlLayer(mGoogleMap, R.raw.chennai_wards, HomeActivity.this);
                kmlLayer.addLayerToMap();

                Timber.i("Kml loaded");
                //            homePresenter.stopAnimation();


            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        },5000);



    }


    @Override
    public void onViewReady() {


        initGoogleApiClient();

        homePresenter.startAnimation();

        initGoogleMaps();

        homePresenter.loadWard();
    }



    private void initGoogleMaps() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void checkAndHandleLocationPermission() {

        rxPermissions = new RxPermissions(HomeActivity.this); // where this is an Activity instance

        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {

                        homePresenter.handleLocationPermission(granted);

                });
    }


    @Override
    public void showCategoryList(ArrayList<Ward> wardArrayList) {

        mWardArrayList = wardArrayList;
        Timber.i("loaded size " + wardArrayList.size());


    }

    @Override
    public void showAnimation() {

        viewLoading.setVisibility(View.VISIBLE);
        imgMapMarker.setVisibility(View.GONE);
        llFooter.setVisibility(View.GONE);

         loadingAnimationDrawable
                = (AnimationDrawable)imgLoading.getDrawable();

        imgLoading.post(
                new Runnable(){

                    @Override
                    public void run() {
                        loadingAnimationDrawable.start();
                    }
                });


    }

    @Override
    public void hideAnimation() {

        loadingAnimationDrawable.stop();

        viewLoading.setVisibility(View.GONE);
        llFooter.setVisibility(View.VISIBLE);

        imgMapMarker.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_next)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_next:


                LatLng centerLatLngPosition = getMapCenterPosition();

                String wardNo = getWardNum(centerLatLngPosition);

                homePresenter.handleWardDetails(wardNo, mWardArrayList);

                break;
        }
    }


    @Override
    public void showWardDetailsBottomSheet(Ward ward){

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
        TextView txtWardEmail = view.findViewById(R.id.txt_ward_email);
        LinearLayout llWhatsappGroup = view.findViewById(R.id.ll_whatsapp_group);


        setWardDetailsText(ward, zoneInfo, txtZoneName, txtZoneAddress, txtZoneNumber, txtZoneMobile, txtWardName, txtWardAddress, txtWardId, txtWardMobile, txtWardEmail);

        llWhatsappGroup.setOnClickListener(v -> IntentUtil.joinWhatsappGroup(HomeActivity.this, ward.getWardWhatsappGroupLink()));

        txtWardMobile.setOnClickListener(v -> IntentUtil.makeCallWard(HomeActivity.this, ward.getWardOfficePhone()));

        txtZoneMobile.setOnClickListener(v -> IntentUtil.makeCallZone(HomeActivity.this, zoneInfo.getZonalOfficePhone()));

        txtWardEmail.setOnClickListener(v -> {
        });

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

    }

    private void setWardDetailsText(Ward ward, ZoneInfo zoneInfo, TextView txtZoneName, TextView txtZoneAddress, TextView txtZoneNumber, TextView txtZoneMobile, TextView txtWardName, TextView txtWardAddress, TextView txtWardId, TextView txtWardMobile, TextView txtWardEmail) {
        setText(getString(R.string.text_ward_name_hint) + ward.getWardName(), txtWardName);
        setText(getString(R.string.text_ward_address_hint) + ward.getWardOfficeAddress(), txtWardAddress);
        setText(ward.getWardNo(), txtWardId);
        setText(getString(R.string.text_ward_contact_hint) + ward.getWardOfficePhone(), txtWardMobile);
        setText(getString(R.string.text_ward_email_hint) + ward.getWardOfficeEmail(), txtWardEmail);
        setText(getString(R.string.text_zone_name_hint) + zoneInfo.getZoneName(), txtZoneName);
        setText(zoneInfo.getZoneNo(), txtZoneNumber);
        setText(getString(R.string.text_zone_address_hint)+ zoneInfo.getZonalOfficeAddress(), txtZoneAddress);
        setText(getString(R.string.text_zone_contact_hint) + zoneInfo.getZonalOfficePhone(), txtZoneMobile);
    }

    @Override
    public void getCurrentLocation() {

        Timber.i("Location enabled success");

        Timber.i("onLocation Updated called");

        Location location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (location != null) {

            Timber.i("Lat lng inside onLocationUpdated");

            configureMapAndAddLayer(location);

            Timber.i("getMapAsync() called in onLocationUpdated");

        } else {

            mGoogleApiClient.connect();
        }






    }

    @Override
    public void showLocationPermissionError() {
        Toast.makeText(HomeActivity.this,
                "Sorry! Map cannot be loaded. Please enable the location permission",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWardDetailsNotFoundError() {
        Toast.makeText(this, "No ward details found for this area", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGpsPermissionEnabled() {

        Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();

        homePresenter.checkAndHandleLocationPermission();

    }

    @Override
    public void onGpsPermissionDenied() {

        Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();

    }


    private LatLng getMapCenterPosition() {
        if (mGoogleMap != null)
            return mGoogleMap.getCameraPosition().target;

        return new LatLng(120.0, 80.0);
    }


    private String getWardNum(LatLng latLng) {
        KmlPlacemark kmlPlacemark = KmlUtil.containsInAnyPolygon(kmlLayer, latLng);
        if (kmlPlacemark != null) {
            String wardName = kmlPlacemark.getProperty(ATTRIBUTE_KML_NAME);
            String wardNo = ParseUtil.getWardNum(wardName);
            return wardNo;
        }
        return null;
    }




    private void setText(String text, TextView textView) {

        if (TextUtils.isEmpty(text)) {
            textView.setText("-");
        } else {
            textView.setText(text);
        }

    }




    private void initGoogleApiClient(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(Constants.INTERVAL_IN_MS.UPDATE_LOCATION);
        locationRequest.setFastestInterval(Constants.INTERVAL_IN_MS.FATEST_LOCATION);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);

    }

    @Override
    public void onConnectionSuspended(int i) {

        Timber.i("Google Api client connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Timber.i("Google Api client connection failed");


    }

    @Override
    public void onResult(@NonNull LocationSettingsResult result) {

        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:


                homePresenter.checkAndHandleLocationPermission();

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {

                    status.startResolutionForResult(HomeActivity.this, Constants.REQUEST_CODES.CHECK_GPS_PERMISSION);

                } catch (IntentSender.SendIntentException e) {

                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODES.CHECK_GPS_PERMISSION) {

                homePresenter.handleGpsPermissionState(resultCode);

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

}