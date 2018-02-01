package com.thoughtworks.whatyourward.features.maps;

import com.thoughtworks.whatyourward.data.DataManager;
import com.thoughtworks.whatyourward.features.base.BasePresenter;
import com.thoughtworks.whatyourward.injection.ConfigPersistent;

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


    public void onViewReady(){
        getView().onViewReady();

    }


}
