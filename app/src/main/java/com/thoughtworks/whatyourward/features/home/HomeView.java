package com.thoughtworks.whatyourward.features.home;

import com.thoughtworks.whatyourward.data.model.ward.Ward;
import com.thoughtworks.whatyourward.features.base.MvpView;

import java.util.ArrayList;

public interface HomeView extends MvpView {

    void onViewReady();

    void showCategoryList(ArrayList<Ward> wardArrayList);

    void showAnimation();

    void hideAnimation();

    void showWardDetailsBottomSheet(Ward ward);

    void checkLocationPermission();

    void getCurrentLocation();

    void showLocationPermissionError();

    void showWardDetailsNotFoundError();

//    void hideAnimationAndFinish();
}
