package com.thoughtworks.whatyourward.features.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.thoughtworks.whatyourward.Constants;
import com.thoughtworks.whatyourward.R;
import com.thoughtworks.whatyourward.features.base.BaseActivity;
import com.thoughtworks.whatyourward.features.home.HomeActivity;
import com.thoughtworks.whatyourward.injection.component.ActivityComponent;
import com.thoughtworks.whatyourward.util.ImageUtil;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by udhayakumarulaganathan on 03/01/18.
 */

public class SplashScreenActivity extends BaseActivity implements SplashScreenView {


    @Inject
    SplashScreenPresenter splashScreenPresenter;

    @BindView(R.id.img_splash)
    ImageView imgSplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashScreenPresenter.onViewReady();

    }

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        splashScreenPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        splashScreenPresenter.detachView();
    }


    @Override
    public void goToNextScreen() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));

            }
        }, Constants.SPLASH_TIME_OUT);


    }

    @Override
    public void onViewReady() {


        ImageUtil.loadImage(this,R.drawable.bg_splash,imgSplash);

        splashScreenPresenter.goToNextScreen();

    }

}

