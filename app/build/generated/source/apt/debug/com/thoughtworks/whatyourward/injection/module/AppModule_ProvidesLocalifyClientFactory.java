package com.thoughtworks.whatyourward.injection.module;

import android.content.Context;
import com.github.polok.localify.LocalifyClient;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AppModule_ProvidesLocalifyClientFactory implements Factory<LocalifyClient> {
  private final AppModule module;

  private final Provider<Context> contextProvider;

  public AppModule_ProvidesLocalifyClientFactory(
      AppModule module, Provider<Context> contextProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
  }

  @Override
  public LocalifyClient get() {
    return Preconditions.checkNotNull(
        module.providesLocalifyClient(contextProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<LocalifyClient> create(
      AppModule module, Provider<Context> contextProvider) {
    return new AppModule_ProvidesLocalifyClientFactory(module, contextProvider);
  }

  public static LocalifyClient proxyProvidesLocalifyClient(AppModule instance, Context context) {
    return instance.providesLocalifyClient(context);
  }
}
