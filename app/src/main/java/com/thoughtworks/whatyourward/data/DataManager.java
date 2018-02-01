package com.thoughtworks.whatyourward.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.github.polok.localify.LocalifyClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.whatyourward.Constants;
import com.thoughtworks.whatyourward.data.local.PreferencesHelper;
import com.thoughtworks.whatyourward.data.model.response.CategoryResponse;
import com.thoughtworks.whatyourward.injection.ApplicationContext;
import com.thoughtworks.whatyourward.interfaces.OnCategorySuccess;
import com.thoughtworks.whatyourward.util.Util;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Chandru on 09/11/17.
 */

@Singleton
public class DataManager {


    private LocalifyClient localifyClient;


    private PreferencesHelper preferencesHelper;


    private Context context;

    @Inject
    public DataManager(@ApplicationContext Context context,LocalifyClient localifyClient, PreferencesHelper preferencesHelper) {

        this.context = context;
        this.localifyClient = localifyClient;
        this.preferencesHelper = preferencesHelper;


    }



    public void loadCategory(OnCategorySuccess onCategorySuccess){

        localifyClient.localify()
                .rx()
                .loadAssetsFile(Constants.CATEGORY_OFFLINE_FILE)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, CategoryResponse>() {
                    @Override
                    public CategoryResponse call(String data) {
                        Gson gson = new GsonBuilder().create();
                        return gson.fromJson(data, CategoryResponse.class);
                    }
                }).subscribe(new Subscriber<CategoryResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(CategoryResponse categoryResponse) {

                onCategorySuccess.onCategorySuccess(categoryResponse);

            }
        });
    }



    public void trackScreen(int screedID){
         preferencesHelper.putInt(Constants.SHARED_PREF.SCREEN_ID, screedID);
    }


    public int loadScreen(int screedID){
        return preferencesHelper.getInt(Constants.SHARED_PREF.SCREEN_ID);
    }




    }

