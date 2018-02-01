package com.thoughtworks.whatyourward.injection.component;

import dagger.Subcomponent;

import com.thoughtworks.whatyourward.features.splash.SplashScreenActivity;
import com.thoughtworks.whatyourward.injection.PerActivity;
import com.thoughtworks.whatyourward.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashScreenActivity splashScreenActivity);


}
