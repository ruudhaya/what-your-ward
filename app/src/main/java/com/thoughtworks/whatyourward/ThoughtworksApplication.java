package com.thoughtworks.whatyourward;

import android.app.Application;
import android.content.Context;


import com.thoughtworks.whatyourward.injection.component.AppComponent;
import com.thoughtworks.whatyourward.injection.component.DaggerAppComponent;
import com.thoughtworks.whatyourward.injection.module.AppModule;

import io.realm.Realm;
import timber.log.Timber;

public class ThoughtworksApplication extends Application {

    private AppComponent appComponent;

    public static ThoughtworksApplication get(Context context) {
        return (ThoughtworksApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Realm.init(this);

    }

    public AppComponent getComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }
}
