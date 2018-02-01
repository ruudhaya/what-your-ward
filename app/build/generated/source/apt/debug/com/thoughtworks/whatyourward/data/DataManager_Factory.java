package com.thoughtworks.whatyourward.data;

import android.content.Context;
import com.github.polok.localify.LocalifyClient;
import com.thoughtworks.whatyourward.data.local.PreferencesHelper;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DataManager_Factory implements Factory<DataManager> {
  private final Provider<Context> contextProvider;

  private final Provider<LocalifyClient> localifyClientProvider;

  private final Provider<PreferencesHelper> preferencesHelperProvider;

  public DataManager_Factory(
      Provider<Context> contextProvider,
      Provider<LocalifyClient> localifyClientProvider,
      Provider<PreferencesHelper> preferencesHelperProvider) {
    this.contextProvider = contextProvider;
    this.localifyClientProvider = localifyClientProvider;
    this.preferencesHelperProvider = preferencesHelperProvider;
  }

  @Override
  public DataManager get() {
    return new DataManager(
        contextProvider.get(), localifyClientProvider.get(), preferencesHelperProvider.get());
  }

  public static Factory<DataManager> create(
      Provider<Context> contextProvider,
      Provider<LocalifyClient> localifyClientProvider,
      Provider<PreferencesHelper> preferencesHelperProvider) {
    return new DataManager_Factory(
        contextProvider, localifyClientProvider, preferencesHelperProvider);
  }
}
