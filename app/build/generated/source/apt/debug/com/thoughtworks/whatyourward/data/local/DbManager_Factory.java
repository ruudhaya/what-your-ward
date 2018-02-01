package com.thoughtworks.whatyourward.data.local;

import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DbManager_Factory implements Factory<DbManager> {
  private static final DbManager_Factory INSTANCE = new DbManager_Factory();

  @Override
  public DbManager get() {
    return new DbManager();
  }

  public static Factory<DbManager> create() {
    return INSTANCE;
  }
}
