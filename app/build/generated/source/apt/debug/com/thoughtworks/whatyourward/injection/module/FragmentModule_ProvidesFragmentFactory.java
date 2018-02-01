package com.thoughtworks.whatyourward.injection.module;

import android.support.v4.app.Fragment;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class FragmentModule_ProvidesFragmentFactory implements Factory<Fragment> {
  private final FragmentModule module;

  public FragmentModule_ProvidesFragmentFactory(FragmentModule module) {
    this.module = module;
  }

  @Override
  public Fragment get() {
    return Preconditions.checkNotNull(
        module.providesFragment(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Fragment> create(FragmentModule module) {
    return new FragmentModule_ProvidesFragmentFactory(module);
  }

  public static Fragment proxyProvidesFragment(FragmentModule instance) {
    return instance.providesFragment();
  }
}
