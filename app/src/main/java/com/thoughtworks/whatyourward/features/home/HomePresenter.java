package com.thoughtworks.whatyourward.features.home;

import android.text.TextUtils;

import com.thoughtworks.whatyourward.data.DataManager;
import com.thoughtworks.whatyourward.data.model.ward.Ward;
import com.thoughtworks.whatyourward.features.base.BasePresenter;
import com.thoughtworks.whatyourward.injection.ConfigPersistent;
import com.thoughtworks.whatyourward.interfaces.OnWardSuccess;

import java.util.ArrayList;

import javax.inject.Inject;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeView> {

    private final DataManager dataManager;


    @Inject
    public HomePresenter(DataManager dataManager) {
        this.dataManager = dataManager;

    }


    @Override
    public void attachView(HomeView mvpView) {

        super.attachView(mvpView);


    }


    public void onViewReady() {
        getView().onViewReady();
    }


    public void loadWard() {

        dataManager.loadWard(new OnWardSuccess() {
            @Override
            public void onWardList(ArrayList<Ward> wardList) {


                getView().showCategoryList(wardList);

            }
        });
    }

    public void startAnimation() {

        getView().showAnimation();
    }


    public void stopAnimation() {

        getView().hideAnimation();

    }

    public void showWardDetailsBottomSheet(Ward ward) {


        getView().showWardDetailsBottomSheet(ward);
    }

    public void checkLocationPermission() {

        getView().checkLocationPermission();
    }

    public void handleLocationPermission(boolean isGranted) {

        if(isGranted) {
            getView().getCurrentLocation();
        }else{

            getView().showLocationPermissionError();
        }
    }

    public void handleWardDetails(String wardNo, ArrayList<Ward> wardList) {

        if(!TextUtils.isEmpty(wardNo)) {
            for (Ward ward : wardList) {

                if (ward != null && wardNo.equalsIgnoreCase(ward.getWardNo())) {

                    getView().showWardDetailsBottomSheet(ward);
                }else{

                    getView().showWardDetailsNotFoundError();
                }
            }

        }
    }


//    public void stopAnimationAndFinish(){
//
//        getView().hideAnimationAndFinish();
//
//    }
}
