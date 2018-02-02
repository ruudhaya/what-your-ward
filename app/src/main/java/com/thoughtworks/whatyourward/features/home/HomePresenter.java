package com.thoughtworks.whatyourward.features.home;

import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.thoughtworks.whatyourward.data.DataManager;
import com.thoughtworks.whatyourward.data.model.ward.Ward;
import com.thoughtworks.whatyourward.features.base.BasePresenter;
import com.thoughtworks.whatyourward.injection.ConfigPersistent;
import com.thoughtworks.whatyourward.interfaces.OnWardSuccess;

import java.util.ArrayList;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

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


    public void onViewReady(){
        getView().onViewReady();
    }


    public void loadWard(){

        dataManager.loadWard(new OnWardSuccess() {
            @Override
            public void onWardList(ArrayList<Ward> wardList) {


                getView().showCategoryList(wardList);

            }
        });
    }

    public void startAnimation(){

        getView().showAnimation();
    }


    public void stopAnimation(){

        getView().hideAnimation();

    }


//    public void stopAnimationAndFinish(){
//
//        getView().hideAnimationAndFinish();
//
//    }
}
