package com.thoughtworks.whatyourward.injection.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.github.polok.localify.LocalifyClient;
import com.thoughtworks.whatyourward.data.DataManager;
import com.thoughtworks.whatyourward.data.DataManager_Factory;
import com.thoughtworks.whatyourward.data.local.PreferencesHelper;
import com.thoughtworks.whatyourward.data.local.PreferencesHelper_Factory;
import com.thoughtworks.whatyourward.injection.module.AppModule;
import com.thoughtworks.whatyourward.injection.module.AppModule_ProvideApplicationFactory;
import com.thoughtworks.whatyourward.injection.module.AppModule_ProvideContextFactory;
import com.thoughtworks.whatyourward.injection.module.AppModule_ProvideSharedPreferenceFactory;
import com.thoughtworks.whatyourward.injection.module.AppModule_ProvidesLocalifyClientFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerAppComponent implements AppComponent {
  private AppModule appModule;

  private Provider<Context> provideContextProvider;

  private Provider<LocalifyClient> providesLocalifyClientProvider;

  private Provider<SharedPreferences> provideSharedPreferenceProvider;

  private Provider<PreferencesHelper> preferencesHelperProvider;

  private Provider<DataManager> dataManagerProvider;

  private DaggerAppComponent(Builder builder) {
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {
    this.appModule = builder.appModule;
    this.provideContextProvider = AppModule_ProvideContextFactory.create(builder.appModule);
    this.providesLocalifyClientProvider =
        DoubleCheck.provider(
            AppModule_ProvidesLocalifyClientFactory.create(
                builder.appModule, provideContextProvider));
    this.provideSharedPreferenceProvider =
        AppModule_ProvideSharedPreferenceFactory.create(builder.appModule, provideContextProvider);
    this.preferencesHelperProvider =
        DoubleCheck.provider(PreferencesHelper_Factory.create(provideSharedPreferenceProvider));
    this.dataManagerProvider =
        DoubleCheck.provider(
            DataManager_Factory.create(
                provideContextProvider, providesLocalifyClientProvider, preferencesHelperProvider));
  }

  @Override
  public Context context() {
    return Preconditions.checkNotNull(
        AppModule_ProvideContextFactory.proxyProvideContext(appModule),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  @Override
  public Application application() {
    return Preconditions.checkNotNull(
        AppModule_ProvideApplicationFactory.proxyProvideApplication(appModule),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  @Override
  public DataManager dataManager() {
    return dataManagerProvider.get();
  }

  public static final class Builder {
    private AppModule appModule;

    private Builder() {}

    public AppComponent build() {
      if (appModule == null) {
        throw new IllegalStateException(AppModule.class.getCanonicalName() + " must be set");
      }
      return new DaggerAppComponent(this);
    }

    public Builder appModule(AppModule appModule) {
      this.appModule = Preconditions.checkNotNull(appModule);
      return this;
    }
  }
}
