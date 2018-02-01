package com.thoughtworks.whatyourward.injection.component;

import com.thoughtworks.whatyourward.data.DataManager;
import com.thoughtworks.whatyourward.features.splash.SplashScreenActivity;
import com.thoughtworks.whatyourward.features.splash.SplashScreenActivity_MembersInjector;
import com.thoughtworks.whatyourward.features.splash.SplashScreenPresenter;
import com.thoughtworks.whatyourward.features.splash.SplashScreenPresenter_Factory;
import com.thoughtworks.whatyourward.injection.module.ActivityModule;
import com.thoughtworks.whatyourward.injection.module.FragmentModule;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerConfigPersistentComponent implements ConfigPersistentComponent {
  private Provider<DataManager> dataManagerProvider;

  private Provider<SplashScreenPresenter> splashScreenPresenterProvider;

  private DaggerConfigPersistentComponent(Builder builder) {
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {
    this.dataManagerProvider =
        new com_thoughtworks_whatyourward_injection_component_AppComponent_dataManager(
            builder.appComponent);
    this.splashScreenPresenterProvider =
        DoubleCheck.provider(SplashScreenPresenter_Factory.create(dataManagerProvider));
  }

  @Override
  public ActivityComponent activityComponent(ActivityModule activityModule) {
    return new ActivityComponentImpl(activityModule);
  }

  @Override
  public FragmentComponent fragmentComponent(FragmentModule fragmentModule) {
    return new FragmentComponentImpl(fragmentModule);
  }

  public static final class Builder {
    private AppComponent appComponent;

    private Builder() {}

    public ConfigPersistentComponent build() {
      if (appComponent == null) {
        throw new IllegalStateException(AppComponent.class.getCanonicalName() + " must be set");
      }
      return new DaggerConfigPersistentComponent(this);
    }

    public Builder appComponent(AppComponent appComponent) {
      this.appComponent = Preconditions.checkNotNull(appComponent);
      return this;
    }
  }

  private static class com_thoughtworks_whatyourward_injection_component_AppComponent_dataManager
      implements Provider<DataManager> {
    private final AppComponent appComponent;

    com_thoughtworks_whatyourward_injection_component_AppComponent_dataManager(
        AppComponent appComponent) {
      this.appComponent = appComponent;
    }

    @Override
    public DataManager get() {
      return Preconditions.checkNotNull(
          appComponent.dataManager(), "Cannot return null from a non-@Nullable component method");
    }
  }

  private final class ActivityComponentImpl implements ActivityComponent {
    private final ActivityModule activityModule;

    private ActivityComponentImpl(ActivityModule activityModule) {
      this.activityModule = Preconditions.checkNotNull(activityModule);
    }

    @Override
    public void inject(SplashScreenActivity splashScreenActivity) {
      injectSplashScreenActivity(splashScreenActivity);
    }

    private SplashScreenActivity injectSplashScreenActivity(SplashScreenActivity instance) {
      SplashScreenActivity_MembersInjector.injectSplashScreenPresenter(
          instance, DaggerConfigPersistentComponent.this.splashScreenPresenterProvider.get());
      return instance;
    }
  }

  private final class FragmentComponentImpl implements FragmentComponent {
    private final FragmentModule fragmentModule;

    private FragmentComponentImpl(FragmentModule fragmentModule) {
      this.fragmentModule = Preconditions.checkNotNull(fragmentModule);
    }
  }
}
