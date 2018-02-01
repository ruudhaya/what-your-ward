package com.thoughtworks.whatyourward.injection.module;

import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class FragmentModule_ProvidesContextFactory implements Factory<Context> {
  private final FragmentModule module;

  public FragmentModule_ProvidesContextFactory(FragmentModule module) {
    this.module = module;
  }

  @Override
  public Context get() {
    return Preconditions.checkNotNull(
        module.providesContext(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Context> create(FragmentModule module) {
    return new FragmentModule_ProvidesContextFactory(module);
  }

  public static Context proxyProvidesContext(FragmentModule instance) {
    return instance.providesContext();
  }
}
