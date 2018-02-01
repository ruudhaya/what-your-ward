package com.thoughtworks.whatyourward.features.splash;

import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class SplashScreenActivity_MembersInjector
    implements MembersInjector<SplashScreenActivity> {
  private final Provider<SplashScreenPresenter> splashScreenPresenterProvider;

  public SplashScreenActivity_MembersInjector(
      Provider<SplashScreenPresenter> splashScreenPresenterProvider) {
    this.splashScreenPresenterProvider = splashScreenPresenterProvider;
  }

  public static MembersInjector<SplashScreenActivity> create(
      Provider<SplashScreenPresenter> splashScreenPresenterProvider) {
    return new SplashScreenActivity_MembersInjector(splashScreenPresenterProvider);
  }

  @Override
  public void injectMembers(SplashScreenActivity instance) {
    injectSplashScreenPresenter(instance, splashScreenPresenterProvider.get());
  }

  public static void injectSplashScreenPresenter(
      SplashScreenActivity instance, SplashScreenPresenter splashScreenPresenter) {
    instance.splashScreenPresenter = splashScreenPresenter;
  }
}
