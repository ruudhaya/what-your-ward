package com.thoughtworks.whatyourward.data.local;

import android.content.SharedPreferences;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class PreferencesHelper_Factory implements Factory<PreferencesHelper> {
  private final Provider<SharedPreferences> sharedPreferencesProvider;

  public PreferencesHelper_Factory(Provider<SharedPreferences> sharedPreferencesProvider) {
    this.sharedPreferencesProvider = sharedPreferencesProvider;
  }

  @Override
  public PreferencesHelper get() {
    return new PreferencesHelper(sharedPreferencesProvider.get());
  }

  public static Factory<PreferencesHelper> create(
      Provider<SharedPreferences> sharedPreferencesProvider) {
    return new PreferencesHelper_Factory(sharedPreferencesProvider);
  }

  public static PreferencesHelper newPreferencesHelper(SharedPreferences sharedPreferences) {
    return new PreferencesHelper(sharedPreferences);
  }
}
