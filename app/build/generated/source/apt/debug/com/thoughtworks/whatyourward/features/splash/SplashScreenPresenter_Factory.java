package com.thoughtworks.whatyourward.features.splash;

import com.thoughtworks.whatyourward.data.DataManager;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class SplashScreenPresenter_Factory implements Factory<SplashScreenPresenter> {
  private final Provider<DataManager> dataManagerProvider;

  public SplashScreenPresenter_Factory(Provider<DataManager> dataManagerProvider) {
    this.dataManagerProvider = dataManagerProvider;
  }

  @Override
  public SplashScreenPresenter get() {
    return new SplashScreenPresenter(dataManagerProvider.get());
  }

  public static Factory<SplashScreenPresenter> create(Provider<DataManager> dataManagerProvider) {
    return new SplashScreenPresenter_Factory(dataManagerProvider);
  }
}
