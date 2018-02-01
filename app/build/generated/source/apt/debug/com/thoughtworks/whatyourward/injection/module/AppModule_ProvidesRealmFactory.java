package com.thoughtworks.whatyourward.injection.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.realm.Realm;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AppModule_ProvidesRealmFactory implements Factory<Realm> {
  private final AppModule module;

  public AppModule_ProvidesRealmFactory(AppModule module) {
    this.module = module;
  }

  @Override
  public Realm get() {
    return Preconditions.checkNotNull(
        module.providesRealm(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Realm> create(AppModule module) {
    return new AppModule_ProvidesRealmFactory(module);
  }

  public static Realm proxyProvidesRealm(AppModule instance) {
    return instance.providesRealm();
  }
}
