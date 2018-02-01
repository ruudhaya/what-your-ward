package com.thoughtworks.whatyourward.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import com.github.polok.localify.LocalifyClient;
import com.thoughtworks.whatyourward.Constants;
import com.thoughtworks.whatyourward.injection.ApplicationContext;
import com.thoughtworks.whatyourward.util.Util;

import javax.inject.Singleton;

import static com.thoughtworks.whatyourward.Constants.PREF_FILE_NAME;

@Module
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    SharedPreferences provideSharedPreference(@ApplicationContext Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }


    @Provides
    @Singleton
    LocalifyClient providesLocalifyClient(@ApplicationContext Context context) {
        return new LocalifyClient.Builder()
                .withAssetManager(context.getAssets())
                .build();
    }

    @Provides
    @Singleton
    Realm providesRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                 .name(Constants.DATABASE_NAME)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        return Realm.getInstance(config);
    }


}
