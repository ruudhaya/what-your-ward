package com.thoughtworks.whatyourward.features.home;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.net.Uri;
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
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class HomeActivity extends BaseActivity implements HomeView, OnMapReadyCallback
        , GoogleApiClient.OnConnectionFailedListener
        , GoogleApiClient.ConnectionCallbacks
            ,ResultCallback<LocationSettingsResult>{

    @Inject
    HomePresenter homePresenter;

    private static final int defaultZoom = 13;

//    private static LatLng currentLocation = new LatLng(13.0827, 80.2707);

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


    private LatLng latLng;
    private SupportMapFragment mapFragment;

    private GoogleMap mGoogleMap;
    private ArrayList<Ward> mWardArrayList;
    private AnimationDrawable loadingAnimationDrawable;


    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    private int REQUEST_CHECK_SETTINGS = 100;


    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private Location location;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initGoogleApiClient();

        homePresenter.startAnimation();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);



        mapFragment.getMapAsync(this);



        homePresenter.loadWard();
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


    private void setDefaultConfig() {

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);


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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

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
            }
        },5000);



    }


    @Override
    public void onViewReady() {


    }

    private void requestLocationPermission() {

        rxPermissions = new RxPermissions(HomeActivity.this); // where this is an Activity instance

        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {


                        Timber.i("Location enabled success");

                        updateLocation();

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


                Ward ward = getWardDetails();

                if(ward != null) {

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


                    setText("WARD: " + ward.getWardName(), txtWardName);
                    setText("WARD OFFICE ADDRESS \n" + ward.getWardOfficeAddress(), txtWardAddress);
                    setText(ward.getWardNo(), txtWardId);
                    setText("CONTACT: " + ward.getWardOfficePhone(), txtWardMobile);
                    setText("EMAIL: " + ward.getWardOfficeEmail(), txtWardEmail);

                    setText("ZONE: " + zoneInfo.getZoneName(), txtZoneName);
                    setText(zoneInfo.getZoneNo(), txtZoneNumber);
                    setText("ZONAL OFFICE ADDRESS \n" + zoneInfo.getZonalOfficeAddress(), txtZoneAddress);
                    setText("CONTACT: " + zoneInfo.getZonalOfficePhone(), txtZoneMobile);

                    llWhatsappGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            IntentUtil.joinWhatsappGroup(HomeActivity.this, ward.getWardWhatsappGroupLink());
                        }
                    });


                    txtWardMobile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IntentUtil.makeCallWard(HomeActivity.this, ward.getWardOfficePhone());
                        }
                    });

                    txtZoneMobile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            IntentUtil.makeCallZone(HomeActivity.this, zoneInfo.getZonalOfficePhone());
                        }
                    });

                    txtWardEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        sendEmail(ward.getWardOfficeEmail());
                        }
                    });

                    BottomSheetDialog dialog = new BottomSheetDialog(this);
                    dialog.setContentView(view);
                    dialog.show();

                }else{

                    Toast.makeText(this, "No ward details found for this area", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void updateLocation() {

        Timber.i("onLocation Updated called");

        location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (location != null) {

            latLng = new LatLng(location.getLatitude(), location.getLongitude());


            Timber.i("Lat lng inside onLocationUpdated");
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.


//            mapFragment.getMapAsync(this);

            setDefaultConfig();

            Timber.i("getMapAsync() called in onLocationUpdated");

        } else {

            mGoogleApiClient.connect();
        }






    }


    public LatLng getCenterOfMap() {
        if (mGoogleMap != null)
            return mGoogleMap.getCameraPosition().target;
        return new LatLng(120.0, 80.0);
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


        if(!TextUtils.isEmpty(wardNo)) {
            for (Ward ward : mWardArrayList) {

                if (ward != null && wardNo.equalsIgnoreCase(ward.getWardNo())) {
                    return ward;
                }
            }

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
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FATEST_INTERVAL);
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
                // NO need to show the dialog;

//                updateLocation();

                requestLocationPermission();

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show dialog
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }



//    private void sendEmail(String wardOfficeEmail) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setData()
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {


                requestLocationPermission();

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

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
