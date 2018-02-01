package com.thoughtworks.whatyourward.features.splash;

import com.thoughtworks.whatyourward.data.model.response.Categories;
import com.thoughtworks.whatyourward.features.base.MvpView;

import java.util.List;

public interface SplashScreenView extends MvpView {



    void goToNextScreen();

    void onViewReady();


}
