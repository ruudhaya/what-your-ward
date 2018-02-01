package com.thoughtworks.whatyourward.injection.module;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AppModule_ProvideSharedPreferenceFactory implements Factory<SharedPreferences> {
  private final AppModule module;

  private final Provider<Context> contextProvider;

  public AppModule_ProvideSharedPreferenceFactory(
      AppModule module, Provider<Context> contextProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
  }

  @Override
  public SharedPreferences get() {
    return Preconditions.checkNotNull(
        module.provideSharedPreference(contextProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<SharedPreferences> create(
      AppModule module, Provider<Context> contextProvider) {
    return new AppModule_ProvideSharedPreferenceFactory(module, contextProvider);
  }

  public static SharedPreferences proxyProvideSharedPreference(
      AppModule instance, Context context) {
    return instance.provideSharedPreference(context);
  }
}
